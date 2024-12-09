/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80028 (8.0.28)
 Source Host           : localhost:3306
 Source Schema         : ohohplat

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 09/12/2024 15:46:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for workflow_def
-- ----------------------------
DROP TABLE IF EXISTS `workflow_def`;
CREATE TABLE `workflow_def` (
  `def_id` varchar(32) NOT NULL COMMENT '流程定义id',
  `delete_flag` tinyint DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deftype_id` varchar(32) DEFAULT NULL COMMENT '流程定义类别id',
  `def_name` varchar(64) DEFAULT NULL COMMENT '流程名称',
  `def_code` varchar(64) DEFAULT NULL COMMENT '流程编码',
  `def_version` int DEFAULT NULL COMMENT '版本号',
  `def_sort` int DEFAULT NULL COMMENT '排序',
  `def_remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `def_xml` longtext COMMENT '流程定义xml',
  `def_json` longtext COMMENT '流程定义json',
  `def_svg` longtext COMMENT '流程定义svg',
  PRIMARY KEY (`def_id`),
  UNIQUE KEY `idx_flowdef_code` (`def_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='流程定义表';

-- ----------------------------
-- Records of workflow_def
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for workflow_deftype
-- ----------------------------
DROP TABLE IF EXISTS `workflow_deftype`;
CREATE TABLE `workflow_deftype` (
  `deftype_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '流程定义类别id',
  `delete_flag` tinyint DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `deftype_name` varchar(64) DEFAULT NULL COMMENT '名称',
  `deftype_code` varchar(64) DEFAULT NULL COMMENT '编码',
  `deftype_remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父亲id',
  `tree_level` int DEFAULT NULL COMMENT '树等级',
  `tree_path` varchar(4000) DEFAULT NULL COMMENT '树路径',
  `tree_sort` int DEFAULT NULL COMMENT '树排序',
  PRIMARY KEY (`deftype_id`),
  UNIQUE KEY `idx_flowdeftype_code` (`deftype_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='流程类别表';

-- ----------------------------
-- Records of workflow_deftype
-- ----------------------------
BEGIN;
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863472444065521666', 0, '1', '2024-12-02 14:37:07', '1', '2024-12-08 11:32:37', '审批流转', 'approveCategory', NULL, '0', 1, '0.1863472444065521666.', 1);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863472815190122497', 0, '1', '2024-12-02 14:38:35', '1', '2024-12-02 14:38:35', '公文发文', 'gwfwCategory', NULL, '0', 1, '0.1863472815190122497.', 2);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863472888477196289', 0, '1', '2024-12-02 14:38:53', '1', '2024-12-02 16:15:39', '公文收文', 'gwswCategory', NULL, '0', 1, '0.1863472888477196289.', 3);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863491010911313921', 0, '1', '2024-12-02 15:50:54', '1', '2024-12-08 11:32:42', '考勤申请', 'kqsq', NULL, '1863472444065521666', 2, '0.1863472444065521666.1863491010911313921.', 1);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863491898606395394', 0, '1', '2024-12-02 15:54:25', '1', '2024-12-02 15:54:25', '人事申请', 'rssq', NULL, '1863472444065521666', 2, '0.1863472444065521666.1863491898606395394.', 2);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863491960380104706', 0, '1', '2024-12-02 15:54:40', '1', '2024-12-02 15:54:40', '行政办公', 'xzbg', NULL, '1863472444065521666', 2, '0.1863472444065521666.1863491960380104706.', 3);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863492180224548865', 0, '1', '2024-12-02 15:55:32', '1', '2024-12-02 15:55:32', '差旅报销', 'clbx', NULL, '1863472444065521666', 2, '0.1863472444065521666.1863492180224548865.', 4);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863494922825408513', 0, '1', '2024-12-02 16:06:26', '1', '2024-12-02 16:06:26', '通用发文', 'tyfw', NULL, '1863472815190122497', 2, '0.1863472815190122497.1863494922825408513.', 1);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863495013393014786', 0, '1', '2024-12-02 16:06:48', '1', '2024-12-02 16:06:48', '通知发文', 'tzfw', NULL, '1863472815190122497', 2, '0.1863472815190122497.1863495013393014786.', 2);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863495078232760321', 0, '1', '2024-12-02 16:07:03', '1', '2024-12-02 16:07:03', '会议纪要', 'hyjy', NULL, '1863472815190122497', 2, '0.1863472815190122497.1863495078232760321.', 3);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863495231509405698', 0, '1', '2024-12-02 16:07:40', '1', '2024-12-02 16:07:40', '通用收文', 'tysw', NULL, '1863472888477196289', 2, '0.1863472888477196289.1863495231509405698.', 1);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863495304322523138', 0, '1', '2024-12-02 16:07:57', '1', '2024-12-02 16:07:57', '无类别收文', 'wlbsw', NULL, '1863472888477196289', 2, '0.1863472888477196289.1863495304322523138.', 2);
INSERT INTO `workflow_deftype` (`deftype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `deftype_name`, `deftype_code`, `deftype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1863501244815925249', 0, '1', '2024-12-02 16:31:34', '1', '2024-12-02 16:31:39', '无类别发文', 'wlbfw', NULL, '1863472815190122497', 2, '0.1863472815190122497.1863501244815925249.', 4);
COMMIT;

-- ----------------------------
-- Table structure for workflow_hisdeploy
-- ----------------------------
DROP TABLE IF EXISTS `workflow_hisdeploy`;
CREATE TABLE `workflow_hisdeploy` (
  `hisdeploy_id` varchar(32) NOT NULL COMMENT '历史部署id',
  `delete_flag` tinyint DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `def_code` varchar(64) DEFAULT NULL COMMENT '流程编码',
  `def_version` int DEFAULT NULL COMMENT '版本号',
  `def_xml` longtext COMMENT '流程定义xml',
  `def_json` longtext COMMENT '流程定义json',
  `def_svg` longtext COMMENT '流程定义svg',
  PRIMARY KEY (`hisdeploy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='流程历史部署表';

-- ----------------------------
-- Records of workflow_hisdeploy
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
