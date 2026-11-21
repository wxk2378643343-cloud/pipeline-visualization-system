/*
 Navicat Premium Data Transfer

 Source Server         : selab
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : 1.13.2.68:3306
 Source Schema         : pip

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 28/03/2023 21:19:40
*/


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pipe
-- ----------------------------
DROP TABLE IF EXISTS `pipe`;
CREATE TABLE `pipe`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管道类型',
  `wells_id` json NULL COMMENT '井口id',
  `material` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '材质',
  `diameter` decimal(65, 30) NULL DEFAULT NULL COMMENT '直径',
  `deep` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '深度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for well
-- ----------------------------
DROP TABLE IF EXISTS `well`;
CREATE TABLE `well`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `longitude` decimal(20, 14) NOT NULL COMMENT '经度',
  `latitude` decimal(20, 14) NULL DEFAULT NULL COMMENT '纬度',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '井类型',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '井型号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1092 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
