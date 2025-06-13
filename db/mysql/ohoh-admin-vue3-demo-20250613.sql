-- MySQL dump 10.13  Distrib 8.0.36, for macos14 (x86_64)
--
-- Host: localhost    Database: ohohplat
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `demo_leave`
--

DROP TABLE IF EXISTS `demo_leave`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `demo_leave` (
  `leave_id` varchar(32) NOT NULL COMMENT '主键id',
  `delete_flag` tinyint NOT NULL COMMENT '是否删除0=否1=是',
  `create_userid` varchar(32) NOT NULL COMMENT '创建者id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_userid` varchar(32) DEFAULT NULL COMMENT '更新者id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `apply_user` varchar(64) NOT NULL COMMENT '申请人',
  `applyuser_orgid` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人部门id',
  `applyuser_orgname` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '申请人部门',
  `leave_type` int NOT NULL COMMENT '请假类别',
  `leave_startdate` date NOT NULL COMMENT '开始日期',
  `leave_enddate` date NOT NULL COMMENT '结束日期',
  `leave_days` int NOT NULL COMMENT '请假天数',
  `apply_reason` varchar(256) NOT NULL COMMENT '申请理由',
  `agent_userid` varchar(32) DEFAULT NULL COMMENT '职务代理人id',
  `agent_user` varchar(64) DEFAULT NULL COMMENT '职务代理人',
  PRIMARY KEY (`leave_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='请假信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demo_leave`
--

LOCK TABLES `demo_leave` WRITE;
/*!40000 ALTER TABLE `demo_leave` DISABLE KEYS */;
/*!40000 ALTER TABLE `demo_leave` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-13 22:39:34
