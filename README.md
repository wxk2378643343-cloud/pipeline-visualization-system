# 管线定位系统

![Java 17](https://img.shields.io/badge/Java%2017-2563EB?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-0F766E?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-334155?style=flat-square)
![微信小程序](https://img.shields.io/badge/%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F-B45309?style=flat-square)

## 📌 项目简介

为中铁三局定制的施工现场地下管网可视化定位工具。施工人员可以在微信小程序地图上实时定位、查看地下管线走向与井位分布、点选查询点位与管线属性，并通过 Excel 批量导入更新管网数据。

---

## 🏗️ 系统架构与端云数据流

- **微信小程序端**：负责定位、按井号顺序绘制管线折线、本地缓存空间索引与点击命中最近邻查询。
- **Spring Boot 后端**：负责解析 Excel、统一坐标体系、拆段构建面向井位与管段的 K-D 树变体空间索引并持久化至 MySQL。
- 分端开发与联调步骤见 [后端 README](pipe-springboot/README.md) 和 [小程序 README](pipes-wechat-minimum-programming/README.md)。

```text
【数据更新阶段（低频 · 后端完成）】
Excel 导入 ➔ WGS84 转 GCJ-02 ➔ 折线按相邻井拆段 ➔ 交替经纬度中位井构建 K-D 树变体 ➔ 缓存树根 (/data/tree)

【地图交互阶段（高频 · 小程序本地完成）】
下载索引并缓存 ➔ 用户 bindtap 点击地图拿到 (lat, lng)
  ├─► Step 1：K-D 树回溯剪枝查最近井位（容差 0.00002° 内）── 命中 ➔ 打开井位详情（井位优先）
  └─► Step 2：未命中井位 ➔ K-D 树回溯剪枝搜局部候选管段 ➔ 逐段算点到有限线段距离 ➔ 命中并高亮整条管线
```

---

## 🧠 核心算法解析

微信小程序 `<map>` 组件的 `bindtap` 仅返回点击位置的经纬度，不提供原生“点击细线”事件。系统将问题拆解为两个互补部分：**K-D 树空间索引与回溯剪枝**负责快速缩小候选集，**点到有限线段的平面几何距离**负责精准判定命中对象。

### 一、后端算法（数据导入与空间索引构建）

1. **坐标系统一（WGS84 → GCJ-02）与有序管线拆段**
   - Excel 导入时将测绘 WGS84 坐标统一转换为 GCJ-02（与小程序定位坐标系对齐），消除定位偏移；
   - 对每条由有序井号列表构成的管线（如 `[3, 8, 12]`），拆分为相邻两井之间的独立有限线段（`3—8`、`8—12`）并保留所属管线编号，便于前端按单段算距离、命中后反查整条管线。
   - 源码：[Excel 坐标转换](pipe-springboot/src/main/java/tyut/selab/a2/pipes/utils/ExcelUtil.java#L230-L249) · [管线拆段逻辑](pipe-springboot/src/main/java/tyut/selab/a2/pipes/service/impl/DataServiceImpl.java#L225-L260)

2. **面向井位与管段的 K-D 树变体（二维空间划分）**
   - 按经度 / 纬度交替排序取**中位井**作为分割节点递归划分平面，使左右子树井位数量均衡（树高 $O(\log N)$），避免因数据输入顺序退化；
   - 内部节点保存分割井位，叶节点保存该子空间的候选管段；若某管段横跨分割线，则分配到两侧叶节点以保证任一侧点击均可命中；建树结果缓存在内存中供 `/data/tree` 下发。
   - 源码：[建树与缓存](pipe-springboot/src/main/java/tyut/selab/a2/pipes/service/impl/DataServiceImpl.java#L101-L149) · [交替维度递归建树](pipe-springboot/src/main/java/tyut/selab/a2/pipes/service/impl/DataServiceImpl.java#L152-L215) · [管段区域分配](pipe-springboot/src/main/java/tyut/selab/a2/pipes/service/impl/DataServiceImpl.java#L273-L441)

### 二、前端算法（折线渲染与本地点击命中）

1. **按井号顺序生成地图折线**
   - 复用 Excel 拓扑顺序，依次取出 `wellsId` 对应井位经纬度拼成折线并按管线类型着色，交由微信地图组件绘制。
   - 源码：[折线生成](pipes-wechat-minimum-programming/miniprogram/pages/index/methods/SetPolyline.ts#L12-L36)

2. **井位优先 + 带容差（`0.00002°`）的 K-D 树回溯剪枝最近邻搜索**
   - 点击地图后先在容差内查最近井位，命中即打开井位详情（防止在管线交汇处误点管线）；未命中再查候选管段；
   - 查树时优先沿点击点所在一侧下探，回溯时比较点击点到当前分割线的距离与当前搜索半径——若大于半径则直接剪枝跳过另一侧子树，大幅减少距离计算。
   - 源码：[地图点击入口](pipes-wechat-minimum-programming/miniprogram/pages/index/index.ts#L329-L368) · [井位优先规则](pipes-wechat-minimum-programming/miniprogram/pages/index/methods/ClickType.ts#L13-L33) · [最近邻与剪枝](pipes-wechat-minimum-programming/miniprogram/utils/FindNearestInfo.ts#L28-L168) · [命中反查管线](pipes-wechat-minimum-programming/miniprogram/pages/index/methods/MapClick.ts#L17-L73)

3. **点到有限线段的平面几何最短距离**
   - 井位采用欧氏距离；管段采用**余弦定理**判断垂足是否落在线段内部：
     - 垂足落在线段内（两底角均为锐角/直角）：用三角形面积公式反推高 $h = \frac{2S}{|AB|}$ 作为垂直距离，点在长管线中段也能精准选中；
     - 垂足落在线段外（存在钝角）：取点击点到较近端点的距离，避免点击延长线时误选中管线。
   - 源码：[平面几何距离](pipes-wechat-minimum-programming/miniprogram/utils/CalculateUtils.ts#L6-L50)

```text
  情况 1：垂足落在线段内（取垂直距离 h）        情况 2：垂足落在延长线上（取较近端点 |PB|）
              P                                                   P
              | \                                               /
            h |   \                                           /
              |     \                                       /
      A ------+------- B                        A --------- B ······· +
            垂足 H                                                  延长线垂足
```

---

## 📊 算法复杂度与工程权衡

记井位总数为 $N$，拆段后的相邻管段总数为 $M$，回溯剪枝后命中的局部候选管段数为 $k$（通常 $k \ll M$），跨分割线重复引用后的总管段数为 $M_{\text{dup}}$。

| 阶段 / 算法 | 平均时间复杂度 | 最坏时间复杂度 | 空间复杂度 | 说明 |
| :--- | :---: | :---: | :---: | :--- |
| 后端：坐标转换 + 管线拆段 | $O(N + M)$ | $O(N + M)$ | $O(N + M)$ | 仅在 Excel 导入时执行一次 |
| 后端：K-D 树递归建树（每层排序） | $O(N \log^2 N)$ | $O(N \log^2 N)$ | $O(N)$ | 树高 $O(\log N)$；若改用快速选择求中位数可降至 $O(N \log N)$ |
| 后端：管段分配至叶节点 | $O(M \log N)$ | $O(M \cdot N)$ | $O(M_{\text{dup}})$ | 跨分割线管段双侧分配，以少量存储冗余换取查询正确性 |
| 前端：单次点到井 / 点到有限线段距离 | $O(1)$ | $O(1)$ | $O(1)$ | 纯平面几何解析计算 |
| 前端：全图暴力遍历点击命中（对比基线） | $O(N + M)$ | $O(N + M)$ | $O(1)$ | 每次点击都需遍历全部井位与管段 |
| **前端：K-D 树回溯剪枝点击命中（本方案）** | **$O(\log N + k)$** | $O(N + M)$ | $O(\log N)$ | 剪枝跳过远处无关子树，仅对局部候选计算几何距离 |

### 设计优点与已知边界
- **优点**：将建树开销前置到低频的数据导入阶段并缓存；高频地图点选完全在小程序本地完成，**零网络往返、适配施工现场弱网环境**；结合空间剪枝、有限线段几何距离与井位优先，兼顾响应速度与交互准确度。
- **已知边界（后续优化方向）**：
  1. [点击容差配置](pipes-wechat-minimum-programming/miniprogram/app.ts#L36-L37) 中的 `0.00002` 直接作用于经纬度差（单位为度），不同纬度下东西向对应的实际物理米数会随 $\cos\varphi$ 变化，后续可结合地图缩放级别动态换算；
  2. [分割线相等分支](pipe-springboot/src/main/java/tyut/selab/a2/pipes/service/impl/DataServiceImpl.java#L308-L330) 中管段端点恰好落在分割线上时的部分判定分支使用了另一坐标轴，后续可统一改为“压线即双侧分配”以消除极端漏检边界。

---

## 📁 项目结构

| 路径 | 用途 |
| --- | --- |
| [`pipe-springboot`](pipe-springboot) | 后端、数据库脚本与 K-D 树空间索引构建 |
| [`pipes-wechat-minimum-programming`](pipes-wechat-minimum-programming) | 原生微信小程序（折线绘制、本地空间剪枝查询与几何距离） |
| [`docker`](docker) | 后端镜像和本地运行配置 |

---

## 🚀 如何启动

先用 Docker 启动后端与数据库，再打开微信开发者工具联调。

### Docker 本地启动后端

安装 Docker 和 Compose v2。在仓库根目录创建 `.env`，用独立密码替换示例值。

```dotenv
DB_ROOT_PASSWORD=replace_with_a_mysql_root_password
DB_PASSWORD=replace_with_an_app_database_password
APP_PORT=23317
```

```sh
docker compose config --quiet
docker compose up -d --build
docker compose ps
curl --fail http://localhost:23317/data/get
```

MySQL 首次启动时创建 `pipes` 数据库和两张空表。后续启动保留数据库卷。使用 `docker compose logs --tail=100 backend` 查看后端日志，使用 `docker compose down` 停止服务。镜像使用 [独立配置](docker/application.yml)，本地 API 只监听回环地址并使用 HTTP。

小程序在 [微信开发者工具](pipes-wechat-minimum-programming/README.md) 中打开。模拟器联调时，按前端文档修改 API 地址和端口。正式小程序环境需单独配置可访问的 HTTPS API 域名。
