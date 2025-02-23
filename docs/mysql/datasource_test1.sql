/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50724 (5.7.24-log)
 Source Host           : localhost:3306
 Source Schema         : datasource_test1

 Target Server Type    : MySQL
 Target Server Version : 50724 (5.7.24-log)
 File Encoding         : 65001

 Date: 23/02/2025 15:07:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gender` bit(1) NULL DEFAULT NULL,
  `age` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('1893556937522450434', '张三', b'1', 20);
INSERT INTO `student` VALUES ('1893557607566757890', '李四', b'0', 22);

SET FOREIGN_KEY_CHECKS = 1;
