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

 Date: 10/09/2024 13:35:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dic
-- ----------------------------
DROP TABLE IF EXISTS `sys_dic`;
CREATE TABLE `sys_dic` (
  `dic_id` varchar(32) NOT NULL COMMENT '字典id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `dictype_id` varchar(32) DEFAULT NULL COMMENT '字典类别id',
  `dictype_code` varchar(64) DEFAULT NULL COMMENT '字典类别编码',
  `dic_name` varchar(128) DEFAULT NULL COMMENT '名称',
  `dic_code` varchar(128) DEFAULT NULL COMMENT '编码',
  `extend_field` varchar(512) DEFAULT NULL COMMENT '扩展字段',
  `dic_sort` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`dic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统字典表';

-- ----------------------------
-- Records of sys_dic
-- ----------------------------
BEGIN;
INSERT INTO `sys_dic` (`dic_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_id`, `dictype_code`, `dic_name`, `dic_code`, `extend_field`, `dic_sort`) VALUES ('1661983623210909697', 0, '1', '2023-05-26 14:32:29', '1', '2023-09-16 17:26:03', '1661983574905110530', 'WhetherOrNot', '是', '1', NULL, 1);
INSERT INTO `sys_dic` (`dic_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_id`, `dictype_code`, `dic_name`, `dic_code`, `extend_field`, `dic_sort`) VALUES ('1661983646401216514', 0, '1', '2023-05-26 14:32:34', NULL, NULL, '1661983574905110530', 'WhetherOrNot', '否', '0', NULL, 2);
INSERT INTO `sys_dic` (`dic_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_id`, `dictype_code`, `dic_name`, `dic_code`, `extend_field`, `dic_sort`) VALUES ('1663075357412151297', 0, '1', '2023-05-29 14:50:38', '1', '2023-09-01 16:03:13', '1663075199576297474', 'Gender', '男', '1', NULL, 1);
INSERT INTO `sys_dic` (`dic_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_id`, `dictype_code`, `dic_name`, `dic_code`, `extend_field`, `dic_sort`) VALUES ('1663075382259208193', 0, '1', '2023-05-29 14:50:44', NULL, NULL, '1663075199576297474', 'Gender', '女', '0', NULL, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_dictype
-- ----------------------------
DROP TABLE IF EXISTS `sys_dictype`;
CREATE TABLE `sys_dictype` (
  `dictype_id` varchar(32) NOT NULL COMMENT '字典类别id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `dictype_name` varchar(64) DEFAULT NULL COMMENT '名称',
  `dictype_code` varchar(64) DEFAULT NULL COMMENT '编码',
  `dictype_remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父亲id',
  `tree_level` int DEFAULT NULL COMMENT '树等级',
  `tree_path` varchar(4000) DEFAULT NULL COMMENT '树路径',
  `tree_sort` int DEFAULT NULL COMMENT '树排序',
  PRIMARY KEY (`dictype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统字典类别表';

-- ----------------------------
-- Records of sys_dictype
-- ----------------------------
BEGIN;
INSERT INTO `sys_dictype` (`dictype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_name`, `dictype_code`, `dictype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1661983334256918529', 0, '1', '2023-05-26 14:31:20', '1', '2023-05-26 14:31:20', '常用字典', 'CommonDicType', NULL, '0', 1, '0.1661983334256918529.', 1);
INSERT INTO `sys_dictype` (`dictype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_name`, `dictype_code`, `dictype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1661983477735669761', 0, '1', '2023-05-26 14:31:54', '1', '2023-05-26 14:31:54', '系统管理', 'System', NULL, '0', 1, '0.1661983477735669761.', 2);
INSERT INTO `sys_dictype` (`dictype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_name`, `dictype_code`, `dictype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1661983574905110530', 0, '1', '2023-05-26 14:32:17', '1', '2024-09-04 16:16:08', '是与否', 'WhetherOrNot', NULL, '1661983334256918529', 2, '0.1661983334256918529.1661983574905110530.', 1);
INSERT INTO `sys_dictype` (`dictype_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `dictype_name`, `dictype_code`, `dictype_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1663075199576297474', 0, '1', '2023-05-29 14:50:01', '1', '2023-08-24 16:31:23', '性别', 'Gender', NULL, '1661983334256918529', 2, '0.1661983334256918529.1663075199576297474.', 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `log_id` varchar(32) NOT NULL COMMENT '日志id',
  `log_type` int DEFAULT NULL COMMENT '日志类别',
  `operate_ip` varchar(256) DEFAULT NULL COMMENT '操作端ip',
  `operate_browser` varchar(256) DEFAULT NULL COMMENT '操作端浏览器',
  `log_name` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '日志名称',
  `operate_os` varchar(256) DEFAULT NULL COMMENT '操作端系统',
  `request_uri` varchar(256) DEFAULT NULL COMMENT '请求uri',
  `param_json` longtext COMMENT '请求参数',
  `result_json` longtext COMMENT '返回结果',
  `error_msg` longtext COMMENT '异常信息',
  `operate_userid` varchar(32) DEFAULT NULL COMMENT '操作者id',
  `operate_user` varchar(64) DEFAULT NULL COMMENT '操作者姓名',
  `operate_orgname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组织名称',
  `operate_time` varchar(32) DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT NULL COMMENT '消耗时间，单位：毫秒',
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_org
-- ----------------------------
DROP TABLE IF EXISTS `sys_org`;
CREATE TABLE `sys_org` (
  `org_id` varchar(32) NOT NULL COMMENT '组织机构id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `org_name` varchar(128) DEFAULT NULL COMMENT '名称',
  `org_shortname` varchar(64) DEFAULT NULL COMMENT '简称',
  `org_code` varchar(64) DEFAULT NULL COMMENT '编码',
  `org_level` int DEFAULT NULL COMMENT '组织级别',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父亲id',
  `tree_level` int DEFAULT NULL COMMENT '树等级',
  `tree_path` varchar(4000) DEFAULT NULL COMMENT '树路径',
  `tree_sort` int DEFAULT NULL COMMENT '树排序',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='组织机构表';

-- ----------------------------
-- Records of sys_org
-- ----------------------------
BEGIN;
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646530142383677442', 0, '1', '2023-04-13 23:05:52', '1', '2023-08-30 15:06:53', '机构1', '', '', 1, '0', 1, '0.1646530142383677442.', 1);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646530342812688386', 0, '1', '2023-04-13 23:06:39', '1660938064291287042', '2023-05-31 15:37:17', '机构2', NULL, NULL, 1, '0', 1, '0.1646530342812688386.', 2);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646682646543175682', 0, '1', '2023-04-14 09:11:51', '1', '2023-08-24 16:29:36', '机构1-1', NULL, '', 2, '1646530142383677442', 2, '0.1646530142383677442.1646682646543175682.', 1);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646722354333696001', 0, '1', '2023-04-14 11:49:39', '1', '2023-05-22 10:57:54', '机构1-2', NULL, NULL, 2, '1646530142383677442', 2, '0.1646530142383677442.1646722354333696001.', 2);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646774390039371777', 0, '1', '2023-04-14 15:16:25', '1', '2023-05-25 10:54:37', '机构2-1', NULL, NULL, 2, '1646530342812688386', 2, '0.1646530342812688386.1646774390039371777.', 1);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646784416044847106', 0, '2', '2023-04-14 15:56:15', '1', '2023-05-22 10:58:11', '机构2-2', NULL, NULL, 2, '1646530342812688386', 2, '0.1646530342812688386.1646784416044847106.', 2);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1646787328087166977', 0, '2', '2023-04-14 16:07:50', '1', '2023-05-22 10:58:16', '机构2-3', NULL, NULL, 2, '1646530342812688386', 2, '0.1646530342812688386.1646787328087166977.', 3);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1648243066538049538', 0, '1', '2023-04-18 16:32:25', '1', '2024-09-09 11:14:10', '机构1-1-1', NULL, NULL, 3, '1646682646543175682', 3, '0.1646530142383677442.1646682646543175682.1648243066538049538.', 1);
INSERT INTO `sys_org` (`org_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_name`, `org_shortname`, `org_code`, `org_level`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1661238017316126722', 0, '1648257635159425025', '2023-05-24 13:09:42', '1', '2023-05-24 18:24:55', '机构2-1-1', NULL, NULL, 3, '1646774390039371777', 3, '0.1646530342812688386.1646774390039371777.1661238017316126722.', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `position_id` varchar(32) NOT NULL COMMENT '岗位id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `position_name` varchar(64) DEFAULT NULL COMMENT '岗位名称',
  `position_code` varchar(64) DEFAULT NULL COMMENT '岗位编码',
  `position_level` int DEFAULT NULL COMMENT '岗位级别',
  `position_remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父亲id',
  `tree_level` int DEFAULT NULL COMMENT '树等级',
  `tree_path` varchar(4000) DEFAULT NULL COMMENT '树路径',
  `tree_sort` int DEFAULT NULL COMMENT '树排序',
  PRIMARY KEY (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='岗位表';

-- ----------------------------
-- Records of sys_position
-- ----------------------------
BEGIN;
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679036499473039361', 0, '1', '2023-07-12 15:54:31', '1660933620703002625', '2024-09-09 11:23:41', '总经理', NULL, 1, NULL, '0', 1, '0.1679036499473039361.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679041326487797761', 0, '1', '2023-07-12 16:13:42', '1660933620703002625', '2024-09-09 11:24:55', '副总', NULL, 2, NULL, '1679036499473039361', 2, '0.1679036499473039361.1679041326487797761.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679042689527504898', 0, '1', '2023-07-12 16:19:07', '1', '2024-09-09 11:20:07', '总经理助理', NULL, 5, NULL, '1679036499473039361', 2, '0.1679036499473039361.1679042689527504898.', 2);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679042807546830849', 0, '1', '2023-07-12 16:19:35', '1', '2023-08-24 16:31:57', '总监', NULL, 3, NULL, '1679036499473039361', 2, '0.1679036499473039361.1679042807546830849.', 3);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043114741850113', 0, '1', '2023-07-12 16:20:48', '1', '2023-07-12 16:20:48', '销售总监', NULL, 3, NULL, '1679036499473039361', 2, '0.1679036499473039361.1679043114741850113.', 4);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043170920357890', 0, '1', '2023-07-12 16:21:02', '1', '2023-08-24 21:35:49', '财务总监', NULL, 3, NULL, '1679036499473039361', 2, '0.1679036499473039361. 1679043170920357890.', 5);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043346946908162', 0, '1', '2023-07-12 16:21:44', '1', '2023-07-12 16:22:41', '经理', NULL, 4, NULL, '1679042807546830849', 3, '0.1679036499473039361.1679042807546830849.1679043346946908162.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043568146112514', 0, '1', '2023-07-12 16:22:36', '1', '2023-07-12 16:22:36', '副经理', NULL, 4, NULL, '1679043346946908162', 4, '0.1679036499473039361.1679042807546830849.1679043346946908162.1679043568146112514.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043663247761410', 0, '1', '2023-07-12 16:22:59', '1', '2023-07-12 16:22:59', '主管', NULL, 5, NULL, '1679043346946908162', 4, '0.1679036499473039361.1679042807546830849.1679043346946908162.1679043663247761410.', 2);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043798811860994', 0, '1', '2023-07-12 16:23:31', '1', '2023-07-12 16:23:31', '普通员工', NULL, 6, NULL, '1679043346946908162', 4, '0.1679036499473039361.1679042807546830849.1679043346946908162.1679043798811860994.', 3);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679043915962966018', 0, '1', '2023-07-12 16:23:59', '1', '2023-07-12 16:23:59', '渠道专员', '', 5, NULL, '1679043114741850113', 3, '0.1679036499473039361.1679043114741850113.1679043915962966018.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679044947216490498', 0, '1', '2023-07-12 16:28:05', '1', '2023-07-12 16:28:05', '实习生', NULL, 7, NULL, '1679043346946908162', 4, '0.1679036499473039361.1679042807546830849.1679043346946908162.1679044947216490498.', 4);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679046124855418881', 0, '1', '2023-07-12 16:32:46', '1', '2023-07-12 16:32:46', '大客户经理', NULL, 4, NULL, '1679043114741850113', 3, '0.1679036499473039361.1679043114741850113.1679046124855418881.', 2);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679046202051584001', 0, '1', '2023-07-12 16:33:04', '1', '2023-07-12 16:33:04', '销售代表', NULL, 5, NULL, '1679046124855418881', 4, '0.1679036499473039361.1679043114741850113.1679046124855418881.1679046202051584001.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679046289360216065', 0, '1', '2023-07-12 16:33:25', '1', '2023-07-12 16:33:25', '会计', NULL, 5, NULL, '1679043170920357890', 5, '0.1679036499473039361.1679043170920357890.1679046289360216065.1679043170920357890.1679046289360216065.', 1);
INSERT INTO `sys_position` (`position_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `position_name`, `position_code`, `position_level`, `position_remark`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`) VALUES ('1679046360394948609', 0, '1', '2023-07-12 16:33:42', '1', '2023-07-12 16:33:42', '出纳', NULL, 5, NULL, '1679043170920357890', 5, '0.1679036499473039361.1679043170920357890.1679046289360216065.1679043170920357890.1679046360394948609.', 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_res`;
CREATE TABLE `sys_res` (
  `res_id` varchar(32) NOT NULL COMMENT '资源id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `res_name` varchar(64) DEFAULT NULL COMMENT '资源名称',
  `res_code` varchar(64) DEFAULT NULL COMMENT '资源编码',
  `res_type` int DEFAULT NULL COMMENT '资源类别0=菜单1=按钮',
  `res_icon` varchar(64) DEFAULT NULL COMMENT '资源图标',
  `menu_type` int DEFAULT NULL COMMENT '资源来源0=目录1=路由2=外链',
  `res_path` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源路径/外链url',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父亲id',
  `tree_level` int DEFAULT NULL COMMENT '树等级',
  `tree_path` varchar(4000) DEFAULT NULL COMMENT '树路径',
  `tree_sort` int DEFAULT NULL COMMENT '树排序',
  `hide_flag` int DEFAULT NULL COMMENT '是否隐藏0=否1=是',
  `fullscreen_flag` int DEFAULT NULL COMMENT '是否全屏0=否1=是',
  PRIMARY KEY (`res_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统资源表';

-- ----------------------------
-- Records of sys_res
-- ----------------------------
BEGIN;
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1645653371882856450', 0, '-1', '2023-04-08 11:50:00', '1', '2024-09-09 11:13:47', '首页', 'home', 0, 'HomeFilled', 1, '/home/index', '0', 1, '0.1645653371882856450.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1646048621998649346', 0, '-1', '2023-04-10 10:50:00', '1', '2023-08-30 16:38:29', '系统资源管理', 'sysResManage', 0, 'Menu', 1, '/sys/res/index', '1646111300264181761', 2, '0.1646111300264181761.1646048621998649346.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1646111300264181761', 0, '1', '2023-04-12 19:21:32', '1', '2023-09-14 10:57:10', '系统管理', 'sysManage', 0, 'Setting', 0, NULL, '0', 1, '0.1646111300264181761.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1646442239360020482', 0, '1', '2023-04-13 17:16:34', '1', '2023-08-22 16:21:08', '组织机构管理', 'sysOrgManage', 0, 'OfficeBuilding', 1, '/sys/org/index', '1646111300264181761', 2, '0.1646111300264181761.1646442239360020482.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1647113313047400449', 0, '1', '2023-04-15 13:43:10', '1', '2023-08-31 11:00:46', '系统角色管理', 'sysRoleManage', 0, 'Lock', 1, '/sys/role/index', '1646111300264181761', 2, '0.1646111300264181761.1647113313047400449.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1647516536761516034', 0, '1', '2023-04-16 16:25:26', '1', '2023-08-26 21:09:23', '系统用户管理', 'sysUserManage', 0, 'User', 1, '/sys/user/index', '1646111300264181761', 2, '0.1646111300264181761.1647516536761516034.', 4, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1661632682372640769', 0, '1', '2023-05-25 15:17:58', '1', '2023-08-26 21:13:35', '系统字典管理', 'sysDicManage', 0, 'Notebook', 1, '/sys/dic/index', '1646111300264181761', 2, '0.1646111300264181761.1661632682372640769.', 5, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1679023108364795906', 0, '1', '2023-07-12 15:01:18', '1', '2023-08-26 21:15:25', '岗位管理', 'sysPositionManage', 0, 'Briefcase', 1, '/sys/position/index', '1646111300264181761', 2, '0.1646111300264181761.1679023108364795906.', 6, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1688473383233826817', 0, '1', '2023-08-07 16:53:20', '1', '2023-08-26 21:18:25', '系统日志管理', 'sysLogManage', 0, 'Memo', 1, '/sys/log/index', '1646111300264181761', 2, '0.1646111300264181761.1688473383233826817.', 7, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693895941772980225', 0, '1', '2023-08-22 16:00:38', '1', '2023-08-23 11:59:23', '新增', 'add', 1, NULL, NULL, NULL, '1646048621998649346', 3, '0.1646111300264181761.1646048621998649346.1693895941772980225.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693896310099980290', 0, '1', '2023-08-22 16:02:06', '1', '2023-08-23 12:00:00', '编辑', 'edit', 1, NULL, NULL, NULL, '1646048621998649346', 3, '0.1646111300264181761.1646048621998649346.1693896310099980290.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693896442614820865', 0, '1', '2023-08-22 16:02:38', '1', '2023-08-23 12:00:05', '删除', 'delete', 1, NULL, NULL, NULL, '1646048621998649346', 3, '0.1646111300264181761.1646048621998649346.1693896442614820865.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693900953471332353', 0, '1', '2023-08-22 16:20:33', '1', '2023-08-23 11:59:53', '新增', 'add', 1, NULL, NULL, NULL, '1646442239360020482', 3, '0.1646111300264181761.1646442239360020482.1693900953471332353.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693901340274241537', 0, '1', '2023-08-22 16:22:05', '1', '2023-08-23 12:00:10', '编辑', 'edit', 1, NULL, NULL, NULL, '1646442239360020482', 3, '0.1646111300264181761.1646442239360020482.1693901340274241537.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693901473250455553', 0, '1', '2023-08-22 16:22:37', '1', '2023-08-23 12:00:13', '删除', 'delete', 1, NULL, NULL, NULL, '1646442239360020482', 3, '0.1646111300264181761.1646442239360020482.1693901473250455553.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693912686785114113', 0, '1', '2023-08-22 17:07:11', '1', '2023-08-23 12:43:18', '新增', 'add', 1, NULL, NULL, NULL, '1647113313047400449', 3, '0.1646111300264181761.1647113313047400449.1693912686785114113.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693915351829098498', 0, '1', '2023-08-22 17:17:46', '1', '2023-08-23 12:43:22', '编辑', 'edit', 1, NULL, NULL, NULL, '1647113313047400449', 3, '0.1646111300264181761.1647113313047400449.1693915351829098498.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693915581119115265', 0, '1', '2023-08-22 17:18:41', '1', '2023-08-23 12:43:26', '删除', 'delete', 1, NULL, NULL, NULL, '1647113313047400449', 3, '0.1646111300264181761.1647113313047400449.1693915581119115265.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693915909935771649', 0, '1', '2023-08-22 17:19:59', '1', '2023-08-23 12:43:37', '授权资源', 'grantRes', 1, NULL, NULL, NULL, '1647113313047400449', 3, '0.1646111300264181761.1647113313047400449.1693915909935771649.', 4, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693917369067024385', 0, '1', '2023-08-22 17:25:47', '1', '2023-08-23 12:43:42', '授权数据', 'grantData', 1, NULL, NULL, NULL, '1647113313047400449', 3, '0.1646111300264181761.1647113313047400449.1693917369067024385.', 5, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1693918135202787330', 0, '1', '2023-08-22 17:28:50', '1', '2023-08-26 21:29:50', '授权用户', 'grantUser', 1, NULL, NULL, NULL, '1647113313047400449', 3, '0.1646111300264181761.1647113313047400449.1693918135202787330.', 6, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209051020935170', 0, '1', '2023-08-23 12:44:49', '1', '2023-08-26 21:09:41', '新增', 'add', 1, NULL, NULL, NULL, '1647516536761516034', 3, '0.1646111300264181761.1647516536761516034.1694209051020935170.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209115147649026', 0, '1', '2023-08-23 12:45:05', '1', '2023-08-26 21:10:50', '编辑', 'edit', 1, NULL, NULL, NULL, '1647516536761516034', 3, '0.1646111300264181761.1647516536761516034.1694209115147649026.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209156738367490', 0, '1', '2023-08-23 12:45:15', '1', '2023-08-26 21:10:00', '删除', 'delete', 1, NULL, NULL, NULL, '1647516536761516034', 3, '0.1646111300264181761.1647516536761516034.1694209156738367490.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209331972194305', 0, '1', '2023-08-23 12:45:56', '1', '2023-08-26 21:11:17', '重置密码', 'resetPassword', 1, NULL, NULL, NULL, '1647516536761516034', 3, '0.1646111300264181761.1647516536761516034.1694209331972194305.', 4, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209404063891457', 0, '1', '2023-08-23 12:46:13', '1', '2023-08-26 21:12:23', '授权角色', 'grantRole', 1, NULL, NULL, NULL, '1647516536761516034', 3, '0.1646111300264181761.1647516536761516034.1694209404063891457.', 5, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209473806778369', 0, '1', '2023-08-23 12:46:30', '1', '2023-08-26 21:13:03', '授权数据', 'grantData', 1, NULL, NULL, NULL, '1647516536761516034', 3, '0.1646111300264181761.1647516536761516034.1694209473806778369.', 6, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209888828964866', 0, '1', '2023-08-23 12:48:09', '1', '2023-08-26 21:13:52', '新增类别', 'addType', 1, NULL, NULL, NULL, '1661632682372640769', 3, '0.1646111300264181761.1661632682372640769.1694209888828964866.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694209968839507969', 0, '1', '2023-08-23 12:48:28', '1', '2023-08-26 21:14:02', '编辑类别', 'editType', 1, NULL, NULL, NULL, '1661632682372640769', 3, '0.1646111300264181761.1661632682372640769.1694209968839507969.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210019615752194', 0, '1', '2023-08-23 12:48:40', '1', '2023-08-26 21:14:13', '删除类别', 'deleteType', 1, NULL, NULL, NULL, '1661632682372640769', 3, '0.1646111300264181761.1661632682372640769.1694210019615752194.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210079372001282', 0, '1', '2023-08-23 12:48:54', '1', '2023-08-26 21:14:25', '新增字典', 'add', 1, NULL, NULL, NULL, '1661632682372640769', 3, '0.1646111300264181761.1661632682372640769.1694210079372001282.', 4, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210131121324034', 0, '1', '2023-08-23 12:49:07', '1', '2023-08-26 21:14:34', '编辑字典', 'edit', 1, NULL, NULL, NULL, '1661632682372640769', 3, '0.1646111300264181761.1661632682372640769.1694210131121324034.', 5, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210175153127425', 0, '1', '2023-08-23 12:49:17', '1', '2023-08-26 21:14:44', '删除字典', 'delete', 1, NULL, NULL, NULL, '1661632682372640769', 3, '0.1646111300264181761.1661632682372640769.1694210175153127425.', 6, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210351372615682', 0, '1', '2023-08-23 12:49:59', '1', '2023-08-26 21:16:02', '新增', 'add', 1, NULL, NULL, NULL, '1679023108364795906', 3, '0.1646111300264181761.1679023108364795906.1694210351372615682.', 1, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210393676365826', 0, '1', '2023-08-23 12:50:09', '1', '2023-08-26 21:16:09', '编辑', 'edit', 1, NULL, NULL, NULL, '1679023108364795906', 3, '0.1646111300264181761.1679023108364795906.1694210393676365826.', 2, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210433690025985', 0, '1', '2023-08-23 12:50:19', '1', '2023-08-26 21:16:16', '删除', 'delete', 1, NULL, NULL, NULL, '1679023108364795906', 3, '0.1646111300264181761.1679023108364795906.1694210433690025985.', 3, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1694210551881318401', 0, '1', '2023-08-23 12:50:47', '1', '2023-08-26 21:17:55', '授权用户', 'grantUser', 1, NULL, NULL, NULL, '1679023108364795906', 3, '0.1646111300264181761.1679023108364795906.1694210551881318401.', 4, 0, 0);
INSERT INTO `sys_res` (`res_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `res_name`, `res_code`, `res_type`, `res_icon`, `menu_type`, `res_path`, `parent_id`, `tree_level`, `tree_path`, `tree_sort`, `hide_flag`, `fullscreen_flag`) VALUES ('1702155580532760578', 0, '1', '2023-09-14 11:01:30', '1', '2023-09-14 11:01:38', '个人中心', 'userCenter', 0, 'Avatar', 1, '/userCenter/index', '0', 1, '0.1702155580532760578.', 2, 1, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_resurl
-- ----------------------------
DROP TABLE IF EXISTS `sys_resurl`;
CREATE TABLE `sys_resurl` (
  `resurl_id` varchar(32) NOT NULL COMMENT '主键id',
  `res_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `res_url` varchar(256) DEFAULT NULL COMMENT '资源url',
  PRIMARY KEY (`resurl_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统资源url表';

-- ----------------------------
-- Records of sys_resurl
-- ----------------------------
BEGIN;
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1693901101735784450', '1646442239360020482', '/sysOrg/authTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1693901101735784451', '1646442239360020482', '/sysOrg/authPage');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694197616853303298', '1693895941772980225', '/sysRes/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694197743319957505', '1693900953471332353', '/sysOrg/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694197773191790594', '1693896310099980290', '/sysRes/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694197791361515521', '1693896442614820865', '/sysRes/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694197811221544962', '1693901340274241537', '/sysOrg/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694197826472034306', '1693901473250455553', '/sysOrg/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694208667472805890', '1693912686785114113', '/sysRole/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694208684631703554', '1693915351829098498', '/sysRole/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694208700708470785', '1693915581119115265', '/sysRole/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694208746279583746', '1693915909935771649', '/sysRole/grantRes');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694208746279583747', '1693915909935771649', '/sysRole/ownSysRes');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1694208766689067009', '1693917369067024385', '/sysRole/grantDataScope');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423191469531137', '1647516536761516034', '/sysOrg/authTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423191473725441', '1647516536761516034', '/sysUser/authPage');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423266262360066', '1694209051020935170', '/sysUser/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423346415509505', '1694209156738367490', '/sysUser/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423555790970882', '1694209115147649026', '/sysUser/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423555795165186', '1694209115147649026', '/sysUser/enable');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423555795165187', '1694209115147649026', '/sysUser/disable');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423668529668097', '1694209331972194305', '/sysUser/resetPassword');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423948063252481', '1694209404063891457', '/sysUser/ownSysRole');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423948067446785', '1694209404063891457', '/sysOrg/authTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423948067446786', '1694209404063891457', '/sysRole/authPage');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695423948071641090', '1694209404063891457', '/sysUser/grantRole');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424114942025729', '1694209473806778369', '/sysUser/ownDataScope');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424114946220034', '1694209473806778369', '/sysUser/grantDataScope');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424250187358210', '1661632682372640769', '/sysDicType/tree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424250187358211', '1661632682372640769', '/sysDic/page');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424321293393921', '1694209888828964866', '/sysDicType/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424363811053570', '1694209968839507969', '/sysDicType/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424408149041154', '1694210019615752194', '/sysDicType/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424458581352449', '1694210079372001282', '/sysDic/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424496468500482', '1694210131121324034', '/sysDic/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424539468505090', '1694210175153127425', '/sysDic/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424708368932865', '1679023108364795906', '/sysPosition/tree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424708368932866', '1679023108364795906', '/sysPosition/pageUser');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424863558180866', '1694210351372615682', '/sysPosition/add');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424893752975361', '1694210393676365826', '/sysPosition/edit');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695424925612908546', '1694210433690025985', '/sysPosition/delete');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695425340240830466', '1694210551881318401', '/sysUser/authOrgUserTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695425340240830467', '1694210551881318401', '/sysPosition/listUser');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695425340240830468', '1694210551881318401', '/sysPosition/grantUser');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695425465096871938', '1688473383233826817', '/sysLog/page');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695428337461108737', '1693918135202787330', '/sysUser/listAuthSysUsersByRoleId');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695428337461108738', '1693918135202787330', '/sysUser/grantRoleToUser');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1695428337465303041', '1693918135202787330', '/sysUser/authOrgUserTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1696804568821301250', '1646048621998649346', '/sysRes/tree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1696804568821301251', '1646048621998649346', '/sysResUrl/listUrlByResId');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1696804568821301252', '1646048621998649346', '/sysResUrl/listSaPcPermissionUrl');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1697081967647608834', '1647113313047400449', '/sysOrg/authTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1697081967647608835', '1647113313047400449', '/sysRes/authTree');
INSERT INTO `sys_resurl` (`resurl_id`, `res_id`, `res_url`) VALUES ('1697081967647608836', '1647113313047400449', '/sysRole/authPage');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `org_id` varchar(32) DEFAULT NULL COMMENT '组织机构id',
  `role_name` varchar(64) DEFAULT NULL COMMENT '角色名称',
  `publicrole_flag` int DEFAULT NULL COMMENT '是否公共角色0=否1=是',
  `datascope_type` int DEFAULT NULL COMMENT '数据范围类别0=仅本人1=全部机构2=角色机构3=角色机构及下属4=自定义机构',
  `role_remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `role_sort` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_id`, `role_name`, `publicrole_flag`, `datascope_type`, `role_remark`, `role_sort`) VALUES ('1', 0, '1', '2023-04-15 18:37:25', '1', '2023-08-31 11:00:59', '1646530142383677442', '角色1', NULL, 4, NULL, 1);
INSERT INTO `sys_role` (`role_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_id`, `role_name`, `publicrole_flag`, `datascope_type`, `role_remark`, `role_sort`) VALUES ('1647187361001377793', 0, '1', '2023-04-15 18:37:25', '1', '2024-08-08 15:31:47', '0', '全局角色', 1, 0, NULL, 1);
INSERT INTO `sys_role` (`role_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_id`, `role_name`, `publicrole_flag`, `datascope_type`, `role_remark`, `role_sort`) VALUES ('1647471767440113666', 1, '2', '2023-04-16 13:27:33', '1', '2023-05-23 11:50:21', '1646784416044847106', '角色2-2', NULL, 4, NULL, 2);
INSERT INTO `sys_role` (`role_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_id`, `role_name`, `publicrole_flag`, `datascope_type`, `role_remark`, `role_sort`) VALUES ('1660484820263837697', 0, '1', '2023-05-22 11:16:46', '1', '2024-08-29 17:09:24', '1646682646543175682', '角色1-1', NULL, 4, NULL, 1);
INSERT INTO `sys_role` (`role_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `org_id`, `role_name`, `publicrole_flag`, `datascope_type`, `role_remark`, `role_sort`) VALUES ('2', 0, '1', '2023-05-22 11:16:46', '1660938064291287042', '2023-05-31 17:34:51', '1646774390039371777', '角色2-1', NULL, 4, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_roleprop
-- ----------------------------
DROP TABLE IF EXISTS `sys_roleprop`;
CREATE TABLE `sys_roleprop` (
  `prop_id` varchar(32) NOT NULL COMMENT '属性id',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `prop_tablename` varchar(64) DEFAULT NULL COMMENT '关联表名',
  `prop_recordid` varchar(32) DEFAULT NULL COMMENT '关联记录id',
  PRIMARY KEY (`prop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统角色属性表';

-- ----------------------------
-- Records of sys_roleprop
-- ----------------------------
BEGIN;
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1660935723014017025', '1660484820263837697', 'SYS_RES', '1647113313047400449');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1660935723014017026', '1660484820263837697', 'SYS_RES', '1647516536761516034');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1660935723014017027', '1660484820263837697', 'SYS_RES', '1646111300264181761');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1660935723014017028', '1660484820263837697', 'SYS_RES', '1645653371882856450');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1660935723014017029', '1660484820263837697', 'SYS_RES', '1646442239360020482');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1660935723014017030', '1660484820263837697', 'SYS_RES', '1646048621998649346');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1661560796892737538', '1', 'CUSTOM_DATASCOPE', '1646722354333696001');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1661560796892737539', '1', 'CUSTOM_DATASCOPE', '1648243066538049538');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1661560796892737540', '1', 'CUSTOM_DATASCOPE', '1646682646543175682');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1661560796892737541', '1', 'CUSTOM_DATASCOPE', '1646530142383677442');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1663841485415636994', '2', 'CUSTOM_DATASCOPE', '1646530342812688386');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1663841485482745858', '2', 'CUSTOM_DATASCOPE', '1646784416044847106');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1663841485482745859', '2', 'CUSTOM_DATASCOPE', '1646774390039371777');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1663841485482745860', '2', 'CUSTOM_DATASCOPE', '1646787328087166977');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1679688634749501442', '2', 'SYS_USER', '1660933321498132481');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1679688634749501443', '2', 'SYS_USER', '1660938064291287042');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1695423745285431297', '2', 'SYS_USER', '1648257635159425025');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1697528953920237569', '1', 'SYS_USER', '1660933620703002625');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1701445238320263170', '2', 'SYS_RES', '1646048621998649346');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1701445238320263171', '2', 'SYS_RES', '1693895941772980225');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832982933468655618', '1660484820263837697', 'CUSTOM_DATASCOPE', '1648243066538049538');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832982933472849922', '1660484820263837697', 'CUSTOM_DATASCOPE', '1646682646543175682');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983750984642562', '1', 'SYS_RES', '1693900953471332353');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983750997225473', '1', 'SYS_RES', '1694210393676365826');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983750997225474', '1', 'SYS_RES', '1646442239360020482');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751001419777', '1', 'SYS_RES', '1661632682372640769');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751005614082', '1', 'SYS_RES', '1693915351829098498');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751009808386', '1', 'SYS_RES', '1694209331972194305');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751018196993', '1', 'SYS_RES', '1693915581119115265');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751018196994', '1', 'SYS_RES', '1693918135202787330');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751018196995', '1', 'SYS_RES', '1694209115147649026');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751022391298', '1', 'SYS_RES', '1694209968839507969');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751043362818', '1', 'SYS_RES', '1679023108364795906');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751051751426', '1', 'SYS_RES', '1693912686785114113');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751055945730', '1', 'SYS_RES', '1693915909935771649');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751064334338', '1', 'SYS_RES', '1645653371882856450');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751068528642', '1', 'SYS_RES', '1647516536761516034');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751068528643', '1', 'SYS_RES', '1693917369067024385');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751072722945', '1', 'SYS_RES', '1647113313047400449');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751072722946', '1', 'SYS_RES', '1694209051020935170');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1832983751081111554', '1', 'SYS_RES', '1694210019615752194');
INSERT INTO `sys_roleprop` (`prop_id`, `role_id`, `prop_tablename`, `prop_recordid`) VALUES ('1833138395757494274', '1647187361001377793', 'SYS_RES', '1645653371882856450');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_name` varchar(64) DEFAULT NULL COMMENT '姓名',
  `user_account` varchar(64) DEFAULT NULL COMMENT '账号',
  `user_password` varchar(256) DEFAULT NULL COMMENT '密码',
  `user_mobile` varchar(32) DEFAULT NULL COMMENT '手机号码',
  `user_gender` int DEFAULT NULL COMMENT '性别0=女1=男',
  `user_email` varchar(256) DEFAULT NULL COMMENT '电子邮箱',
  `user_status` int DEFAULT NULL COMMENT '用户状态1=启用0=禁用',
  `last_loginip` varchar(64) DEFAULT NULL COMMENT '上次登录ip',
  `last_logintime` datetime DEFAULT NULL COMMENT '上次登录时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `user_name`, `user_account`, `user_password`, `user_mobile`, `user_gender`, `user_email`, `user_status`, `last_loginip`, `last_logintime`) VALUES ('1', 0, '1', '2023-04-06 09:58:46', '1', '2023-05-22 15:11:39', '嘻嘻嘻', 'coder', '207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb', NULL, 1, NULL, 1, '127.0.0.1', '2024-09-10 13:32:19');
INSERT INTO `sys_user` (`user_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `user_name`, `user_account`, `user_password`, `user_mobile`, `user_gender`, `user_email`, `user_status`, `last_loginip`, `last_logintime`) VALUES ('1648257635159425025', 0, '1', '2023-04-18 17:30:18', '1', '2023-08-30 16:11:32', '普通用户2-1', 'user2-1', 'bd27360ca984ea323bbeeb761b3ad53876e04dd7a64788577b6487f0e444db50', NULL, 0, NULL, 1, '127.0.0.1', '2024-08-30 09:05:12');
INSERT INTO `sys_user` (`user_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `user_name`, `user_account`, `user_password`, `user_mobile`, `user_gender`, `user_email`, `user_status`, `last_loginip`, `last_logintime`) VALUES ('1660933321498132481', 0, '1', '2023-05-23 16:58:57', '1660933620703002625', '2024-09-09 11:04:52', '普通用户1-1', 'user1-1', 'bd27360ca984ea323bbeeb761b3ad53876e04dd7a64788577b6487f0e444db50', NULL, 1, NULL, 1, '127.0.0.1', '2024-09-04 16:51:26');
INSERT INTO `sys_user` (`user_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `user_name`, `user_account`, `user_password`, `user_mobile`, `user_gender`, `user_email`, `user_status`, `last_loginip`, `last_logintime`) VALUES ('1660933620703002625', 0, '1', '2023-05-23 17:00:08', '1', '2024-09-09 11:19:05', '普通用户1', 'user1', 'bd27360ca984ea323bbeeb761b3ad53876e04dd7a64788577b6487f0e444db50', NULL, 1, NULL, 1, '127.0.0.1', '2024-09-09 14:54:56');
INSERT INTO `sys_user` (`user_id`, `delete_flag`, `create_userid`, `create_time`, `update_userid`, `update_time`, `user_name`, `user_account`, `user_password`, `user_mobile`, `user_gender`, `user_email`, `user_status`, `last_loginip`, `last_logintime`) VALUES ('1660938064291287042', 0, '1', '2023-05-23 17:17:48', '1', '2023-07-14 11:27:37', '普通用户2', 'user2', 'bd27360ca984ea323bbeeb761b3ad53876e04dd7a64788577b6487f0e444db50', NULL, 0, NULL, 1, '127.0.0.1', '2024-09-04 16:35:45');
COMMIT;

-- ----------------------------
-- Table structure for sys_usergroup
-- ----------------------------
DROP TABLE IF EXISTS `sys_usergroup`;
CREATE TABLE `sys_usergroup` (
  `group_id` varchar(32) NOT NULL COMMENT '群组id',
  `delete_flag` int DEFAULT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) DEFAULT NULL COMMENT '创建者id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `group_name` varchar(64) DEFAULT NULL COMMENT '群组名称',
  `group_sort` int DEFAULT NULL COMMENT '排序',
  `group_remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户群组表';

-- ----------------------------
-- Records of sys_usergroup
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_userprop
-- ----------------------------
DROP TABLE IF EXISTS `sys_userprop`;
CREATE TABLE `sys_userprop` (
  `prop_id` varchar(32) NOT NULL COMMENT '属性id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `prop_tablename` varchar(64) DEFAULT NULL COMMENT '关联表名',
  `prop_recordid` varchar(32) DEFAULT NULL COMMENT '关联记录id',
  `prop_sort` int DEFAULT NULL COMMENT '属性排序',
  `prop_extendid` varchar(32) DEFAULT NULL COMMENT '关联扩展id',
  `default_flag` int DEFAULT NULL COMMENT '是否默认属性0=否1=是',
  PRIMARY KEY (`prop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='系统用户属性表';

-- ----------------------------
-- Records of sys_userprop
-- ----------------------------
BEGIN;
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1679694106944684033', '1660938064291287042', 'SYS_ORG', '1646682646543175682', 2, '1679042689527504898', 0);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1679694106944684034', '1660938064291287042', 'SYS_ORG', '1646787328087166977', 1, NULL, 1);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1695423997312770050', '1648257635159425025', 'DATASCOPE', '1648243066538049538', NULL, NULL, NULL);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1695423997312770051', '1648257635159425025', 'DATASCOPE', '1661238017316126722', NULL, NULL, NULL);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1695423997316964353', '1648257635159425025', 'DATASCOPE', '1646682646543175682', NULL, NULL, NULL);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1695423997316964354', '1648257635159425025', 'DATASCOPE', '1646774390039371777', NULL, NULL, NULL);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1696797786682073090', '1648257635159425025', 'SYS_ORG', '1646774390039371777', 1, NULL, 1);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1832978447920574466', '1660933321498132481', 'SYS_ORG', '1646682646543175682', 1, NULL, 1);
INSERT INTO `sys_userprop` (`prop_id`, `user_id`, `prop_tablename`, `prop_recordid`, `prop_sort`, `prop_extendid`, `default_flag`) VALUES ('1832982028237189121', '1660933620703002625', 'SYS_ORG', '1646530142383677442', 1, '1679036499473039361', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
