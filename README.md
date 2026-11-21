# 管线定位系统

![Java 17](https://img.shields.io/badge/Java%2017-2563EB?style=flat-square)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-0F766E?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-334155?style=flat-square)
![微信小程序](https://img.shields.io/badge/%E5%BE%AE%E4%BF%A1%E5%B0%8F%E7%A8%8B%E5%BA%8F-B45309?style=flat-square)

## 📌 项目简介

为中铁三局定制的施工现场地下管网可视化工具。施工人员可以在地图上查看管线和井位、查询属性，并通过 Excel 导入更新数据。

## 🏗️ 系统架构

- 微信小程序负责定位、地图绘制和交互查询。
- Spring Boot 服务负责解析 Excel、检索空间点位，并将管线和井位存入 MySQL。
- 分端开发与联调步骤见 [后端 README](pipe-springboot/README.md) 和 [小程序 README](pipes-wechat-minimum-programming/README.md)。

## 📁 项目结构

| 路径 | 用途 |
| --- | --- |
| [`pipe-springboot`](pipe-springboot) | 后端、数据库脚本与空间检索 |
| [`pipes-wechat-minimum-programming`](pipes-wechat-minimum-programming) | 原生微信小程序 |
| [`docker`](docker) | 后端镜像和本地运行配置 |

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
