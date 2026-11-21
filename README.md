# 管线定位系统

[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%202.7.5-brightgreen)](./pipe-springboot)
[![WeChat](https://img.shields.io/badge/Frontend-WeChat%20Mini%20Program-blue)](./pipes-wechat-minimum-programming)
[![Language](https://img.shields.io/badge/Language-Java%2017%20%7C%20TypeScript-orange)](./)

## 📌 项目简介

本项目是为 **中铁三局** 定制开发的数字化施工管理解决方案。旨在解决传统施工现场“地下管网复杂、纸质图纸难携带、定位交底效率低”的痛点。通过将复杂的地下管网数据数字化，并结合手机高精度定位，实现“**图纸入口袋，现场见真章**”的施工新模式。

## 🏗️ 系统架构

系统采用前后端分离架构，确保了施工现场高频交互的流畅性：
*   **后端 (`pipe-springboot`)**：负责空间点位的存储、高并发检索算法实现及 Excel 拓扑校验。
*   **前端 (`pipes-wechat-minimum-programming`)**：基于微信小程序原生开发，提供实时地图渲染、交互式参数查询及现场数据采集。

> 💡 **提示**：若需深入了解各端的硬核技术实现（如空间索引算法、坐标转换细节）及部署指南，请分别参阅 [后端子项目 README](./pipe-springboot/README.md) 与 [前端子项目 README](./pipes-wechat-minimum-programming/README.md)。

## 📁 项目结构

```text
.
├── pipe-springboot/                 # Spring Boot 后端工程
│   ├── sql/                         # 数据库初始化脚本
│   └── src/                         # 核心业务与空间算法
└── pipes-wechat-minimum-programming/ # 微信小程序前端工程
    ├── miniprogram/pages/index/     # 地图主交互页面
    └── miniprogram/utils/           # 坐标转换与前端计算工具
```

