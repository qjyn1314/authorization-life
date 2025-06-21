-- MySQL dump 10.13  Distrib 9.3.0, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: life
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `liam_oauth_client`
--

DROP TABLE IF EXISTS `liam_oauth_client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liam_oauth_client` (
  `oauth_client_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'oauth客户端表主键',
  `domain_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '三级域名-默认是 www.authorization.life',
  `client_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端ID',
  `client_secret` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端密钥',
  `grant_types` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '授权类型',
  `scopes` varchar(480) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '授权域',
  `redirect_uri` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '重定向地址',
  `access_token_timeout` int NOT NULL COMMENT '访问授权超时时间',
  `refresh_token_timeout` int NOT NULL COMMENT '刷新授权超时时间',
  `additional_information` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '附加信息',
  `client_secret_bak` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备份密钥(源密码)',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`oauth_client_id`) USING BTREE,
  UNIQUE KEY `lifetime_oauth_client_u1` (`client_id`) USING BTREE,
  UNIQUE KEY `lifetime_oauth_client_u2` (`domain_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='oauth2客户端表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liam_oauth_client`
--

LOCK TABLES `liam_oauth_client` WRITE;
/*!40000 ALTER TABLE `liam_oauth_client` DISABLE KEYS */;
INSERT INTO `liam_oauth_client` (`oauth_client_id`, `domain_name`, `client_id`, `client_secret`, `grant_types`, `scopes`, `redirect_uri`, `access_token_timeout`, `refresh_token_timeout`, `additional_information`, `client_secret_bak`, `tenant_id`, `version_num`, `created_by_user`, `created_by_emp`, `created_time`, `updated_by_user`, `updated_by_emp`, `updated_time`) VALUES ('2','www.authorization.life','passport','$2a$10$MMzpgdNcjGpKyKcYefsS.eYep472gJh4ABvkp6Uo18BUETxHVqxzW','authorization_code,refresh_token,client_credentials','TENANT','http://www.authorization.life/auth-redirect',10800,108000,'','3MMoCFo4nTNjRtGZ','0',1,'0','0','2024-09-17 22:43:57','0','0','2024-09-17 22:43:57'),('4','dev.authorization.life','passport_dev','$2a$10$MMzpgdNcjGpKyKcYefsS.eYep472gJh4ABvkp6Uo18BUETxHVqxzW','authorization_code,refresh_token,client_credentials','TENANT','http://dev.authorization.life/auth-redirect',10800,108000,'','3MMoCFo4nTNjRtGZ','0',1,'0','0','2024-09-17 22:43:57','0','0','2024-09-17 22:43:57');
/*!40000 ALTER TABLE `liam_oauth_client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liam_user`
--

DROP TABLE IF EXISTS `liam_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liam_user` (
  `user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户表主键',
  `username` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户编码',
  `real_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户姓名',
  `lang` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'zh_CN' COMMENT '语言,默认是中文',
  `locale` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT 'CN' COMMENT '地区,默认是中国',
  `gender` tinyint DEFAULT '1' COMMENT '用户性别:1:男 2:女 3：未知',
  `hash_password` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '密码',
  `tel_area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '+86' COMMENT '电话区号。',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '手机号',
  `phone_checked_flag` tinyint DEFAULT '0' COMMENT '手机号通过验证标识',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `email_checked_flag` tinyint DEFAULT '0' COMMENT '邮箱通过验证标识',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `effective_start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生效开始日期',
  `effective_end_date` datetime DEFAULT NULL COMMENT '生效截至日期',
  `actived_flag` tinyint DEFAULT '0' COMMENT '已激活标识',
  `locked_flag` tinyint DEFAULT '0' COMMENT '已锁定标识',
  `locked_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `enabled_flag` tinyint DEFAULT '0' COMMENT '状态：1-启用；0-未启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `lifetime_user_u1` (`username`) USING BTREE,
  UNIQUE KEY `lifetime_user_u2` (`phone`) USING BTREE,
  UNIQUE KEY `lifetime_user_u3` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liam_user`
--

LOCK TABLES `liam_user` WRITE;
/*!40000 ALTER TABLE `liam_user` DISABLE KEYS */;
INSERT INTO `liam_user` (`user_id`, `username`, `real_name`, `lang`, `locale`, `gender`, `hash_password`, `tel_area_code`, `phone`, `phone_checked_flag`, `email`, `email_checked_flag`, `birthday`, `effective_start_date`, `effective_end_date`, `actived_flag`, `locked_flag`, `locked_time`, `enabled_flag`, `tenant_id`, `version_num`, `created_by_user`, `created_by_emp`, `created_time`, `updated_by_user`, `updated_by_emp`, `updated_time`) VALUES ('1','auth-server','AuthServer','zh_CN','CN',1,'$2a$10$HwlX.mszrL626fqplubAOO1t7E1XYs30fkcrNI2CwceWyXSDrdqOy','+86','15321355715',0,'qjyn1314@163.com',0,NULL,'2022-03-06 18:52:20','3000-12-31 23:59:59',1,0,'2024-10-30 23:48:13',1,'0',1,'0','0','2022-03-06 18:52:20','0','0','2024-10-27 20:48:13'),('1bad911753332b234be12411de61c5f0','X3A9X5VG0PJEVKHT',NULL,'zh_CN','CN',1,'$2a$10$kGupRXFnSGKUxSZjGXWoPea5Kgx8ejcvi5uNpAXOH6NoiOT9ptUiq','+86',NULL,0,'calmer19961126@163.com',1,NULL,'2024-10-27 22:41:46','2034-10-27 22:41:46',1,0,NULL,1,'0',1,'0','0','2024-10-27 22:41:46','0','0','2024-10-27 23:46:20');
/*!40000 ALTER TABLE `liam_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `liam_user_group`
--

DROP TABLE IF EXISTS `liam_user_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `liam_user_group` (
  `user_group_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户组表主键',
  `user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '用户ID',
  `user_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户编码(username)',
  `user_group_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户组编码, 与SCOPE编码保持一致',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_group_id`) USING BTREE,
  UNIQUE KEY `lifetime_user_group_u1` (`user_id`,`user_group_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户组表(scope)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `liam_user_group`
--

LOCK TABLES `liam_user_group` WRITE;
/*!40000 ALTER TABLE `liam_user_group` DISABLE KEYS */;
INSERT INTO `liam_user_group` (`user_group_id`, `user_id`, `user_code`, `user_group_code`, `tenant_id`, `version_num`, `created_by_user`, `created_by_emp`, `created_time`, `updated_by_user`, `updated_by_emp`, `updated_time`) VALUES ('1','1','','TENANT','0',1,'0','0','2024-09-17 22:43:30','0','0','2024-09-17 22:43:30'),('2','1bad911753332b234be12411de61c5f0','','TENANT','0',1,'0','0','2024-09-17 22:43:30','0','0','2024-09-17 22:43:30');
/*!40000 ALTER TABLE `liam_user_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lsys_lov`
--

DROP TABLE IF EXISTS `lsys_lov`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lsys_lov` (
  `lov_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集主键',
  `lov_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集代码',
  `lov_type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'LOV类型：FIXED/URL',
  `lov_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '值集名称',
  `description` varchar(360) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `enabled_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`lov_id`) USING BTREE,
  UNIQUE KEY `ztnt_lov_u1` (`lov_code`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='字典主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lsys_lov`
--

LOCK TABLES `lsys_lov` WRITE;
/*!40000 ALTER TABLE `lsys_lov` DISABLE KEYS */;
INSERT INTO `lsys_lov` (`lov_id`, `lov_code`, `lov_type_code`, `lov_name`, `description`, `enabled_flag`, `tenant_id`, `version_num`, `created_by_user`, `created_by_emp`, `created_time`, `updated_by_user`, `updated_by_emp`, `updated_time`) VALUES ('1d3014b271089a267c77f8fb123a71fd','SYS_TEMP_TYPE','FIXED','模板类型','邮件, 短信, 公告, 站内信模板的类型值',1,'0',1,'0','0','2024-10-26 20:43:47','0','0','2024-10-26 20:43:47'),('47cab3a77b0329db09542a6fb28abc6c','FLAG_STATUS','FIXED','是否/启用停用','是否/启用停用-true/false',0,'0',1,'1',NULL,'2024-12-08 16:10:39','1',NULL,'2024-12-08 17:34:08'),('644fcf7e2063f79f4852cfdb73edd859','SYS_LOV_TYPE','FIXED','值集类型','值集的类型',1,'0',1,'0','0','2024-10-26 22:40:12','1',NULL,'2024-12-07 20:48:17'),('b182ed5bc05be17887f49e27c6584fbf','sys_menu_type','FIXED','菜单类型','菜单类型',1,'0',1,'1',NULL,'2024-12-07 21:06:09','1',NULL,'2024-12-08 20:19:50');
/*!40000 ALTER TABLE `lsys_lov` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lsys_lov_value`
--

DROP TABLE IF EXISTS `lsys_lov_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lsys_lov_value` (
  `lov_value_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '固定值集主键',
  `lov_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集表主键, ztnt_lov.lovid',
  `lov_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集代码',
  `value_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '值代码',
  `value_content` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '值内容',
  `description` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `value_order` int NOT NULL DEFAULT '0' COMMENT '排序号',
  `enabled_flag` tinyint NOT NULL DEFAULT '1' COMMENT '生效标识：1:生效，0:失效',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`lov_value_id`) USING BTREE,
  KEY `ztnt_lov_value_n1` (`lov_id`,`value_order`) USING BTREE,
  KEY `ztnt_lov_value_n2` (`lov_code`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='字典明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lsys_lov_value`
--

LOCK TABLES `lsys_lov_value` WRITE;
/*!40000 ALTER TABLE `lsys_lov_value` DISABLE KEYS */;
INSERT INTO `lsys_lov_value` (`lov_value_id`, `lov_id`, `lov_code`, `value_code`, `value_content`, `description`, `tenant_id`, `value_order`, `enabled_flag`, `version_num`, `created_by_user`, `created_by_emp`, `created_time`, `updated_by_user`, `updated_by_emp`, `updated_time`) VALUES ('20676110b67b3a4e32f9a6f2fd980c0d','1d3014b271089a267c77f8fb123a71fd','SYS_TEMP_TYPE','MESSAGES','站内信',NULL,'0',12,1,1,'0','0','2024-10-26 22:31:41','0','0','2024-10-26 22:31:41'),('29ac4961e56ce1f51b1c21b0e1f1415c','b182ed5bc05be17887f49e27c6584fbf','asdfges','MENU','菜单','菜单','0',1,1,1,'1',NULL,'2024-12-07 21:26:12','1',NULL,'2024-12-07 21:34:34'),('2b732937b255d231e9f831580baeb5c3','644fcf7e2063f79f4852cfdb73edd859','SYS_LOV_TYPE','FIXED','常量',NULL,'0',9,1,1,'0','0','2024-10-27 00:11:54','0','0','2024-10-27 00:12:23'),('4233455f421a2c53edc500f8c0e763d5','1d3014b271089a267c77f8fb123a71fd','SYS_TEMP_TYPE','SMS','短信',NULL,'0',2,1,1,'0','0','2024-10-26 22:29:40','0','0','2024-10-26 22:29:40'),('6617a5b2806648e66abc765934c839d5','47cab3a77b0329db09542a6fb28abc6c','FLAG_STATUS','true','启用','启用','0',2,1,1,'1',NULL,'2024-12-08 16:12:04','1',NULL,'2024-12-08 17:33:48'),('796e6516618fedb49296bdb367b5ef9e','47cab3a77b0329db09542a6fb28abc6c','FLAG_STATUS','false','停用','停用','0',1,1,1,'1',NULL,'2024-12-08 16:12:26','1',NULL,'2024-12-08 17:33:53'),('9c83195783781a5dc5e4fecec24fff92','1d3014b271089a267c77f8fb123a71fd','SYS_TEMP_TYPE','ANNOUNCEMENT','公告',NULL,'0',6,1,1,'0','0','2024-10-26 22:31:20','0','0','2024-10-26 22:31:20'),('b7d8fb71583191b82b527a3c97e30a53','b182ed5bc05be17887f49e27c6584fbf','asdfges','BUTTON','按钮','按钮','0',3,1,1,'1',NULL,'2024-12-07 21:26:21','1',NULL,'2024-12-07 21:35:06'),('b83bd6ec28f6c1136edb17b30230270c','644fcf7e2063f79f4852cfdb73edd859','SYS_LOV_TYPE','URL','URL',NULL,'0',6,1,1,'0','0','2024-10-27 00:12:06','0','0','2024-10-27 00:14:23'),('c61d14b6a2d8e106d115cdef5d79bb76','1d3014b271089a267c77f8fb123a71fd','SYS_TEMP_TYPE','EMAIL','邮件','','0',7,1,1,'0','0','2024-10-26 22:27:40','1',NULL,'2024-12-07 21:36:55');
/*!40000 ALTER TABLE `lsys_lov_value` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lsys_temp`
--

DROP TABLE IF EXISTS `lsys_temp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lsys_temp` (
  `temp_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模版ID',
  `temp_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编码',
  `temp_desc` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模版描述',
  `temp_type` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模版内容',
  `enabled_flag` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`temp_id`) USING BTREE,
  UNIQUE KEY `temp_code_unique` (`temp_code`) USING BTREE COMMENT '模板编码唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='模版';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lsys_temp`
--

LOCK TABLES `lsys_temp` WRITE;
/*!40000 ALTER TABLE `lsys_temp` DISABLE KEYS */;
INSERT INTO `lsys_temp` (`temp_id`, `temp_code`, `temp_desc`, `temp_type`, `content`, `enabled_flag`, `tenant_id`, `version_num`, `created_by_user`, `created_by_emp`, `created_time`, `updated_by_user`, `updated_by_emp`, `updated_time`) VALUES ('486426b384cb6b7197b9599df3f2f5ca','REGISTER_CAPTCHA_CODE_TEMPLATE','《命运迷雾》官方后台用户注册邮件','EMAIL','尊敬的用户您好! 您的验证码是: {captchaCode}, 请在10分钟内进行验证. 如果该验证码不是您本人申请,请无视.',1,'0',1,'0','0','2024-10-02 14:25:06','1',NULL,'2024-10-27 23:12:38'),('73083ed156dfe989282a2e01c2df7177','RESET_PASSWORD_CAPTCHA_CODE_TEMPLATE','《命运迷雾》官方后台用户注册邮件','EMAIL','尊敬的用户您好! 您目前正在重置密码, 验证码是: {captchaCode}, 请在10分钟内进行验证. 如果该验证码不是您本人申请,请无视.',1,'0',1,'1',NULL,'2024-10-27 23:12:11','1',NULL,'2024-10-27 23:12:32');
/*!40000 ALTER TABLE `lsys_temp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lsys_temp_user`
--

DROP TABLE IF EXISTS `lsys_temp_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lsys_temp_user` (
  `temp_user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `temp_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板编码',
  `user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户ID',
  `username` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户编码',
  `enabled_flag` tinyint DEFAULT '0' COMMENT '状态：1-启用；0-未启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint DEFAULT '1' COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`temp_user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='模板接收人员';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lsys_temp_user`
--

LOCK TABLES `lsys_temp_user` WRITE;
/*!40000 ALTER TABLE `lsys_temp_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `lsys_temp_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'life'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-21 16:45:02
