/*
 Navicat Premium Data Transfer

 Source Server         : 139.198.104.199
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : 139.198.104.199:3306
 Source Schema         : erupt-weixin

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 29/08/2022 10:27:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for e_upms_menu
-- ----------------------------
DROP TABLE IF EXISTS `e_upms_menu`;
CREATE TABLE `e_upms_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '编码',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自定义参数',
  `sort` int(11) NULL DEFAULT NULL COMMENT '顺序',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单类型',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型值',
  `parent_menu_id` bigint(20) NULL DEFAULT NULL COMMENT '上级菜单',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK95xpkppt33d2bka0g2d7rgwqt`(`code`) USING BTREE,
  INDEX `FK5mkgea183mm02v7ic1pdwxy5s`(`parent_menu_id`) USING BTREE,
  CONSTRAINT `FK5mkgea183mm02v7ic1pdwxy5s` FOREIGN KEY (`parent_menu_id`) REFERENCES `e_upms_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 86 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of e_upms_menu
-- ----------------------------
INSERT INTO `e_upms_menu` VALUES (1, NULL, '2022-08-18 20:04:45', NULL, NULL, '$manager', 'fa fa-cogs', '系统管理', NULL, 1, 1, NULL, NULL, NULL);
INSERT INTO `e_upms_menu` VALUES (2, NULL, '2022-08-18 20:04:45', NULL, NULL, 'EruptMenu', '', '菜单管理', NULL, 0, 1, 'tree', 'EruptMenu', 1);
INSERT INTO `e_upms_menu` VALUES (3, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptMenu@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptMenu@ADD', 2);
INSERT INTO `e_upms_menu` VALUES (4, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptMenu@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptMenu@EDIT', 2);
INSERT INTO `e_upms_menu` VALUES (5, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptMenu@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptMenu@DELETE', 2);
INSERT INTO `e_upms_menu` VALUES (6, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptMenu@VIEW_DETAIL', NULL, '详情', NULL, 40, 1, 'button', 'EruptMenu@VIEW_DETAIL', 2);
INSERT INTO `e_upms_menu` VALUES (7, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptRole', '', '角色管理', NULL, 10, 1, 'table', 'EruptRole', 1);
INSERT INTO `e_upms_menu` VALUES (8, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptRole@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptRole@ADD', 7);
INSERT INTO `e_upms_menu` VALUES (9, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptRole@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptRole@EDIT', 7);
INSERT INTO `e_upms_menu` VALUES (10, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptRole@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptRole@DELETE', 7);
INSERT INTO `e_upms_menu` VALUES (11, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptRole@VIEW_DETAIL', NULL, '详情', NULL, 40, 1, 'button', 'EruptRole@VIEW_DETAIL', 7);
INSERT INTO `e_upms_menu` VALUES (12, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptOrg', '', '组织维护', NULL, 20, 1, 'tree', 'EruptOrg', 1);
INSERT INTO `e_upms_menu` VALUES (13, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptOrg@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptOrg@ADD', 12);
INSERT INTO `e_upms_menu` VALUES (14, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptOrg@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptOrg@EDIT', 12);
INSERT INTO `e_upms_menu` VALUES (15, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptOrg@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptOrg@DELETE', 12);
INSERT INTO `e_upms_menu` VALUES (16, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptOrg@VIEW_DETAIL', NULL, '详情', NULL, 40, 1, 'button', 'EruptOrg@VIEW_DETAIL', 12);
INSERT INTO `e_upms_menu` VALUES (17, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptPost', '', '岗位维护', NULL, 30, 1, 'tree', 'EruptPost', 1);
INSERT INTO `e_upms_menu` VALUES (18, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptPost@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptPost@ADD', 17);
INSERT INTO `e_upms_menu` VALUES (19, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptPost@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptPost@EDIT', 17);
INSERT INTO `e_upms_menu` VALUES (20, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptPost@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptPost@DELETE', 17);
INSERT INTO `e_upms_menu` VALUES (21, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptPost@VIEW_DETAIL', NULL, '详情', NULL, 40, 1, 'button', 'EruptPost@VIEW_DETAIL', 17);
INSERT INTO `e_upms_menu` VALUES (22, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptUser', '', '用户配置', NULL, 40, 1, 'table', 'EruptUser', 1);
INSERT INTO `e_upms_menu` VALUES (23, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptUser@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptUser@ADD', 22);
INSERT INTO `e_upms_menu` VALUES (24, NULL, '2022-08-18 20:04:46', NULL, NULL, 'EruptUser@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptUser@EDIT', 22);
INSERT INTO `e_upms_menu` VALUES (25, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptUser@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptUser@DELETE', 22);
INSERT INTO `e_upms_menu` VALUES (26, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptUser@VIEW_DETAIL', NULL, '详情', NULL, 40, 1, 'button', 'EruptUser@VIEW_DETAIL', 22);
INSERT INTO `e_upms_menu` VALUES (27, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDict', '', '数据字典', NULL, 50, 1, 'table', 'EruptDict', 1);
INSERT INTO `e_upms_menu` VALUES (28, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDict@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptDict@ADD', 27);
INSERT INTO `e_upms_menu` VALUES (29, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDict@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptDict@EDIT', 27);
INSERT INTO `e_upms_menu` VALUES (30, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDict@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptDict@DELETE', 27);
INSERT INTO `e_upms_menu` VALUES (31, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDict@EXPORT', NULL, '导出', NULL, 40, 1, 'button', 'EruptDict@EXPORT', 27);
INSERT INTO `e_upms_menu` VALUES (32, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDict@VIEW_DETAIL', NULL, '详情', NULL, 50, 1, 'button', 'EruptDict@VIEW_DETAIL', 27);
INSERT INTO `e_upms_menu` VALUES (33, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDictItem', '', '字典项', NULL, 60, 2, 'table', 'EruptDictItem', 1);
INSERT INTO `e_upms_menu` VALUES (34, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDictItem@ADD', NULL, '新增', NULL, 10, 1, 'button', 'EruptDictItem@ADD', 33);
INSERT INTO `e_upms_menu` VALUES (35, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDictItem@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'EruptDictItem@EDIT', 33);
INSERT INTO `e_upms_menu` VALUES (36, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDictItem@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'EruptDictItem@DELETE', 33);
INSERT INTO `e_upms_menu` VALUES (37, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDictItem@EXPORT', NULL, '导出', NULL, 40, 1, 'button', 'EruptDictItem@EXPORT', 33);
INSERT INTO `e_upms_menu` VALUES (38, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptDictItem@VIEW_DETAIL', NULL, '详情', NULL, 50, 1, 'button', 'EruptDictItem@VIEW_DETAIL', 33);
INSERT INTO `e_upms_menu` VALUES (39, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptOnline', '', '在线用户', NULL, 65, 1, 'table', 'EruptOnline', 1);
INSERT INTO `e_upms_menu` VALUES (40, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptOnline@EXPORT', NULL, '导出', NULL, 10, 1, 'button', 'EruptOnline@EXPORT', 39);
INSERT INTO `e_upms_menu` VALUES (41, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptLoginLog', '', '登录日志', NULL, 70, 1, 'table', 'EruptLoginLog', 1);
INSERT INTO `e_upms_menu` VALUES (42, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptLoginLog@EXPORT', NULL, '导出', NULL, 10, 1, 'button', 'EruptLoginLog@EXPORT', 41);
INSERT INTO `e_upms_menu` VALUES (43, NULL, '2022-08-18 20:04:47', NULL, NULL, 'EruptOperateLog', '', '操作日志', NULL, 80, 1, 'table', 'EruptOperateLog', 1);
INSERT INTO `e_upms_menu` VALUES (44, 'erupt', '2022-08-18 20:12:30', 'erupt', '2022-08-18 20:12:30', 'EeRR9uCp', 'fa fa-code', '代码生成', NULL, 90, 1, 'table', 'GeneratorClass', NULL);
INSERT INTO `e_upms_menu` VALUES (45, NULL, '2022-08-18 20:12:30', NULL, NULL, 'A7Zz479M', NULL, '新增', NULL, 10, 1, 'button', 'GeneratorClass@ADD', 44);
INSERT INTO `e_upms_menu` VALUES (46, NULL, '2022-08-18 20:12:30', NULL, NULL, 'V40AA6Kg', NULL, '修改', NULL, 20, 1, 'button', 'GeneratorClass@EDIT', 44);
INSERT INTO `e_upms_menu` VALUES (47, NULL, '2022-08-18 20:12:30', NULL, NULL, 's5gRDHLf', NULL, '删除', NULL, 30, 1, 'button', 'GeneratorClass@DELETE', 44);
INSERT INTO `e_upms_menu` VALUES (48, NULL, '2022-08-18 20:12:30', NULL, NULL, 'wsZJOzcq', NULL, '导出', NULL, 40, 1, 'button', 'GeneratorClass@EXPORT', 44);
INSERT INTO `e_upms_menu` VALUES (49, NULL, '2022-08-18 20:12:30', NULL, NULL, 'jR0sZO3P', NULL, '导入', NULL, 50, 1, 'button', 'GeneratorClass@IMPORTABLE', 44);
INSERT INTO `e_upms_menu` VALUES (50, NULL, '2022-08-18 20:12:30', NULL, NULL, '1gHF13Gv', NULL, '详情', NULL, 60, 1, 'button', 'GeneratorClass@VIEW_DETAIL', 44);
INSERT INTO `e_upms_menu` VALUES (51, NULL, '2022-08-18 20:15:39', 'erupt', '2022-08-18 20:16:13', '$generator', 'fa fa-code', '代码生成', NULL, 40, 2, NULL, NULL, NULL);
INSERT INTO `e_upms_menu` VALUES (52, NULL, '2022-08-18 20:15:39', NULL, NULL, 'GeneratorClass', '', '生成Erupt代码', NULL, 0, 1, 'table', 'GeneratorClass', 51);
INSERT INTO `e_upms_menu` VALUES (53, NULL, '2022-08-18 20:15:39', NULL, NULL, 'GeneratorClass@ADD', NULL, '新增', NULL, 10, 1, 'button', 'GeneratorClass@ADD', 52);
INSERT INTO `e_upms_menu` VALUES (54, NULL, '2022-08-18 20:15:39', NULL, NULL, 'GeneratorClass@EDIT', NULL, '修改', NULL, 20, 1, 'button', 'GeneratorClass@EDIT', 52);
INSERT INTO `e_upms_menu` VALUES (55, NULL, '2022-08-18 20:15:39', NULL, NULL, 'GeneratorClass@DELETE', NULL, '删除', NULL, 30, 1, 'button', 'GeneratorClass@DELETE', 52);
INSERT INTO `e_upms_menu` VALUES (56, NULL, '2022-08-18 20:15:40', NULL, NULL, 'GeneratorClass@VIEW_DETAIL', NULL, '详情', NULL, 40, 1, 'button', 'GeneratorClass@VIEW_DETAIL', 52);
INSERT INTO `e_upms_menu` VALUES (57, 'erupt', '2022-08-18 20:25:07', 'erupt', '2022-08-18 20:25:07', 'uJ8nwL15', 'fa fa-weixin', '采集系统', NULL, 100, 1, NULL, NULL, NULL);
INSERT INTO `e_upms_menu` VALUES (58, 'erupt', '2022-08-18 20:25:45', 'erupt', '2022-08-18 20:25:45', 'qcXOUD2t', NULL, '公众号分类', NULL, 10, 1, 'table', 'MpCategory', 57);
INSERT INTO `e_upms_menu` VALUES (59, NULL, '2022-08-18 20:25:45', NULL, NULL, 'czJ6Iq4H', NULL, '新增', NULL, 10, 1, 'button', 'MpCategory@ADD', 58);
INSERT INTO `e_upms_menu` VALUES (60, NULL, '2022-08-18 20:25:45', NULL, NULL, 'nx0OwUq8', NULL, '修改', NULL, 20, 1, 'button', 'MpCategory@EDIT', 58);
INSERT INTO `e_upms_menu` VALUES (61, NULL, '2022-08-18 20:25:45', NULL, NULL, '4rap0FN6', NULL, '删除', NULL, 30, 1, 'button', 'MpCategory@DELETE', 58);
INSERT INTO `e_upms_menu` VALUES (62, NULL, '2022-08-18 20:25:45', NULL, NULL, '7Rss2GYS', NULL, '详情', NULL, 40, 1, 'button', 'MpCategory@VIEW_DETAIL', 58);
INSERT INTO `e_upms_menu` VALUES (63, 'erupt', '2022-08-18 20:32:50', 'erupt', '2022-08-18 20:32:50', 'RSaxBzzQ', NULL, '公众号管理', NULL, 20, 1, 'table', 'MpInfo', 57);
INSERT INTO `e_upms_menu` VALUES (64, NULL, '2022-08-18 20:32:50', NULL, NULL, 'quBt3XAh', NULL, '新增', NULL, 10, 1, 'button', 'MpInfo@ADD', 63);
INSERT INTO `e_upms_menu` VALUES (65, NULL, '2022-08-18 20:32:50', NULL, NULL, 'henSZuOl', NULL, '修改', NULL, 20, 1, 'button', 'MpInfo@EDIT', 63);
INSERT INTO `e_upms_menu` VALUES (66, NULL, '2022-08-18 20:32:50', NULL, NULL, 'JWIfTKEa', NULL, '删除', NULL, 30, 1, 'button', 'MpInfo@DELETE', 63);
INSERT INTO `e_upms_menu` VALUES (67, NULL, '2022-08-18 20:32:50', NULL, NULL, 'Uv2qVw4Q', NULL, '导出', NULL, 40, 1, 'button', 'MpInfo@EXPORT', 63);
INSERT INTO `e_upms_menu` VALUES (68, NULL, '2022-08-18 20:32:50', NULL, NULL, 'Yx55xD19', NULL, '导入', NULL, 50, 1, 'button', 'MpInfo@IMPORTABLE', 63);
INSERT INTO `e_upms_menu` VALUES (69, NULL, '2022-08-18 20:32:50', NULL, NULL, 'k2PcNy7d', NULL, '详情', NULL, 60, 1, 'button', 'MpInfo@VIEW_DETAIL', 63);
INSERT INTO `e_upms_menu` VALUES (70, 'erupt', '2022-08-18 20:38:37', 'erupt', '2022-08-18 20:38:37', '3V6sRjbt', NULL, '文章分类', NULL, 30, 1, 'table', 'MpArticleCategory', 57);
INSERT INTO `e_upms_menu` VALUES (71, NULL, '2022-08-18 20:38:37', NULL, NULL, '4QVtUkPv', NULL, '新增', NULL, 10, 1, 'button', 'MpArticleCategory@ADD', 70);
INSERT INTO `e_upms_menu` VALUES (72, NULL, '2022-08-18 20:38:37', NULL, NULL, '1sz10rqi', NULL, '修改', NULL, 20, 1, 'button', 'MpArticleCategory@EDIT', 70);
INSERT INTO `e_upms_menu` VALUES (73, NULL, '2022-08-18 20:38:37', NULL, NULL, 'FZDT6jRR', NULL, '删除', NULL, 30, 1, 'button', 'MpArticleCategory@DELETE', 70);
INSERT INTO `e_upms_menu` VALUES (74, NULL, '2022-08-18 20:38:37', NULL, NULL, 'b7x1u1Eb', NULL, '详情', NULL, 40, 1, 'button', 'MpArticleCategory@VIEW_DETAIL', 70);
INSERT INTO `e_upms_menu` VALUES (75, 'erupt', '2022-08-18 20:48:49', 'erupt', '2022-08-18 20:48:49', 'NIi5GLF0', NULL, '文章管理', NULL, 40, 1, 'table', 'MpArticle', 57);
INSERT INTO `e_upms_menu` VALUES (76, NULL, '2022-08-18 20:48:49', NULL, NULL, 'eczGuFdA', NULL, '新增', NULL, 10, 1, 'button', 'MpArticle@ADD', 75);
INSERT INTO `e_upms_menu` VALUES (77, NULL, '2022-08-18 20:48:49', NULL, NULL, 'ExHfsP3P', NULL, '修改', NULL, 20, 1, 'button', 'MpArticle@EDIT', 75);
INSERT INTO `e_upms_menu` VALUES (78, NULL, '2022-08-18 20:48:49', NULL, NULL, 'UvIp1QAo', NULL, '删除', NULL, 30, 1, 'button', 'MpArticle@DELETE', 75);
INSERT INTO `e_upms_menu` VALUES (79, NULL, '2022-08-18 20:48:49', NULL, NULL, 'fRgmbahW', NULL, '详情', NULL, 40, 1, 'button', 'MpArticle@VIEW_DETAIL', 75);
INSERT INTO `e_upms_menu` VALUES (80, 'erupt', '2022-08-18 22:00:32', 'erupt', '2022-08-18 22:00:32', '2oBtySEt', NULL, '登录TOKEN', NULL, 50, 1, 'table', 'MpToken', 57);
INSERT INTO `e_upms_menu` VALUES (81, NULL, '2022-08-18 22:00:32', NULL, NULL, 'IY2EgIuZ', NULL, '修改', NULL, 10, 1, 'button', 'MpToken@EDIT', 80);
INSERT INTO `e_upms_menu` VALUES (82, NULL, '2022-08-18 22:00:32', NULL, NULL, 'X2zOQOOC', NULL, '删除', NULL, 20, 1, 'button', 'MpToken@DELETE', 80);
INSERT INTO `e_upms_menu` VALUES (83, NULL, '2022-08-18 22:00:32', NULL, NULL, 'ydI9M3Os', NULL, '详情', NULL, 30, 1, 'button', 'MpToken@VIEW_DETAIL', 80);
INSERT INTO `e_upms_menu` VALUES (84, 'erupt', '2022-08-22 14:45:14', 'erupt', '2022-08-22 14:45:14', 'V2A8ysy3', NULL, '关于软件', NULL, 60, 1, 'tpl', 'about.html', 57);
INSERT INTO `e_upms_menu` VALUES (85, 'erupt', '2022-08-24 15:32:55', 'erupt', '2022-08-24 15:32:55', '5rdHaeZN', NULL, '公众号搜索结果', NULL, 50, 2, 'tpl', 'mpResult.html', 75);

SET FOREIGN_KEY_CHECKS = 1;
