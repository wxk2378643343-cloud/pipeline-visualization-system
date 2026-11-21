# 管道与井位空间定位管理系统 - 后端

本项目是受 **中铁三局** 委托开发的内部施工管理系统后端。通过数字化手段将施工现场的管道、井位数据进行地图化管理，利用空间索引算法实现周边点位的快速检索，有效替代传统纸质图纸，减少施工交底中的信息损耗。

## 核心技术栈
- **后端框架**：Spring Boot 2.7.5
- **持久层**：MyBatis Plus 3.5.2 (支持 JSON 类型处理器)
- **开发语言**：Java 17
- **数据库**：MySQL 8.0
- **文档处理**：Apache POI (Excel 解析)
- **安全传输**：支持 HTTPS (SSL/TLS)

## 核心功能详解

### 1. 高效数据导入与校验
- **多模式 Excel 解析**：支持多种 Excel 模板识别，具备动态表头匹配能力。
- **数据完整性校验**：在导入过程中自动检测“孤立管道”或“缺失井位”，确保空间拓扑关系的正确性。
- **批量处理**：通过 MyBatis Plus 的 `insertBatch` 实现海量施工数据的高效入库。

### 2. 空间索引索引算法 (核心亮点)

- **空间切分递归树**：系统在数据加载后，会基于经纬度通过递归切分（类似于 KD-Tree）构建空间搜索树。
- **性能优化**：相较于传统的 O(n) 全表扫描，该算法将周边点位的检索时间复杂度降低至 O(log n)，极大提升了移动端地图缩放和拖动时的响应速度。

### 3. 地理坐标转换
- **高精度转换**：内置 `LonlatConverUtil`，支持 WGS84（原始 GPS）与 GCJ-02（火星坐标系，如高德、腾讯地图）之间的精准转换，确保施工点位在民用地图插件上的无缝叠加。

## 项目结构
```text
tyut.selab.a2.pipes
├── configure      # 配置类（CORS、MyBatis Plus、全局异常处理、GZIP 压缩）
├── controller     # RESTful API 接口
├── entity         # 数据库映射实体（Pipe、Well）
├── exception      # 业务自定义异常体系
├── filters        # 安全与编码过滤器
├── mapper         # 数据库访问层
├── service        # 业务逻辑层（包含核心空间算法实现）
├── utils          # 工具类（Excel 工具、坐标转换、反射工具）
└── vo             # 视图对象（Node 树节点、DataVo 等）
```

## 快速开始

### 环境要求
- JDK 17
- Maven 3.6+
- MySQL 8.0+

### 部署配置
1. **数据库初始化**：执行 `sql/data.sql` 创建 `pipe` 和 `well` 表。
2. **配置文件**：
   - 修改 `src/main/resources/application-dev-shf.yml` 或 `application-prod.yml` 中的数据库连接信息。
   - 在 `application.yml` 中配置 `pipe.offset`（经纬度偏移修正）。
3. **运行**：执行 `App.java` 中的 `main` 方法启动。默认端口：`23317`。
