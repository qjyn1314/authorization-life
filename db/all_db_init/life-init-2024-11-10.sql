
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for liam_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `liam_oauth_client`;
CREATE TABLE `liam_oauth_client`  (
  `oauth_client_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'oauth客户端表主键',
  `domain_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '三级域名-默认是 www.authorization.life',
  `client_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `client_secret` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端密钥',
  `grant_types` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权类型',
  `scopes` varchar(480) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权域',
  `redirect_uri` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '重定向地址',
  `access_token_timeout` int NOT NULL COMMENT '访问授权超时时间',
  `refresh_token_timeout` int NOT NULL COMMENT '刷新授权超时时间',
  `additional_information` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '附加信息',
  `client_secret_bak` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备份密钥(源密码)',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`oauth_client_id`) USING BTREE,
  UNIQUE INDEX `lifetime_oauth_client_u1`(`client_id` ASC) USING BTREE,
  UNIQUE INDEX `lifetime_oauth_client_u2`(`domain_name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'oauth2客户端表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of liam_oauth_client
-- ----------------------------
INSERT INTO `liam_oauth_client` VALUES ('1', '127.0.0.1', 'passport_local', '$2a$10$MMzpgdNcjGpKyKcYefsS.eYep472gJh4ABvkp6Uo18BUETxHVqxzW', 'authorization_code,refresh_token,client_credentials', 'TENANT', 'http://127.0.0.1:8888/auth-redirect', 10800, 108000, '', '3MMoCFo4nTNjRtGZ', '0', 1, '0', '0', '2024-09-17 22:43:57', '0', '0', '2024-09-17 22:43:57');
INSERT INTO `liam_oauth_client` VALUES ('2', 'www.authorization.life', 'passport', '$2a$10$MMzpgdNcjGpKyKcYefsS.eYep472gJh4ABvkp6Uo18BUETxHVqxzW', 'authorization_code,refresh_token,client_credentials', 'TENANT', 'http://www.authorization.life/auth-redirect', 10800, 108000, '', '3MMoCFo4nTNjRtGZ', '0', 1, '0', '0', '2024-09-17 22:43:57', '0', '0', '2024-09-17 22:43:57');
INSERT INTO `liam_oauth_client` VALUES ('3', 'passport.authorization.life', 'passport_passport', '$2a$10$MMzpgdNcjGpKyKcYefsS.eYep472gJh4ABvkp6Uo18BUETxHVqxzW', 'authorization_code,refresh_token,client_credentials', 'TENANT', 'http://127.0.0.1:8888/', 10800, 108000, '', '3MMoCFo4nTNjRtGZ', '0', 1, '0', '0', '2024-09-17 22:43:57', '0', '0', '2024-09-17 22:43:57');
INSERT INTO `liam_oauth_client` VALUES ('4', 'dev.authorization.life', 'passport_dev', '$2a$10$MMzpgdNcjGpKyKcYefsS.eYep472gJh4ABvkp6Uo18BUETxHVqxzW', 'authorization_code,refresh_token,client_credentials', 'TENANT', 'http://dev.authorization.life/auth-redirect', 10800, 108000, '', '3MMoCFo4nTNjRtGZ', '0', 1, '0', '0', '2024-09-17 22:43:57', '0', '0', '2024-09-17 22:43:57');

-- ----------------------------
-- Table structure for liam_user
-- ----------------------------
DROP TABLE IF EXISTS `liam_user`;
CREATE TABLE `liam_user`  (
  `user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户表主键',
  `username` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `real_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `lang` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'zh_CN' COMMENT '语言,默认是中文',
  `locale` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'CN' COMMENT '地区,默认是中国',
  `gender` tinyint NULL DEFAULT 1 COMMENT '用户性别:1:男 2:女 3：未知',
  `hash_password` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `tel_area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '+86' COMMENT '电话区号。',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `phone_checked_flag` tinyint NULL DEFAULT 0 COMMENT '手机号通过验证标识',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `email_checked_flag` tinyint NULL DEFAULT 0 COMMENT '邮箱通过验证标识',
  `birthday` datetime NULL DEFAULT NULL COMMENT '出生日期',
  `effective_start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生效开始日期',
  `effective_end_date` datetime NULL DEFAULT NULL COMMENT '生效截至日期',
  `actived_flag` tinyint NULL DEFAULT 0 COMMENT '已激活标识',
  `locked_flag` tinyint NULL DEFAULT 0 COMMENT '已锁定标识',
  `locked_time` datetime NULL DEFAULT NULL COMMENT '锁定时间',
  `enabled_flag` tinyint NULL DEFAULT 0 COMMENT '状态：1-启用；0-未启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `lifetime_user_u1`(`username` ASC) USING BTREE,
  UNIQUE INDEX `lifetime_user_u2`(`phone` ASC) USING BTREE,
  UNIQUE INDEX `lifetime_user_u3`(`email` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of liam_user
-- ----------------------------
INSERT INTO `liam_user` VALUES ('1', 'auth-server', 'AuthServer', 'zh_CN', 'CN', 1, '$2a$10$HwlX.mszrL626fqplubAOO1t7E1XYs30fkcrNI2CwceWyXSDrdqOy', '+86', '15321355715', 0, 'qjyn1314@163.com', 0, NULL, '2022-03-06 18:52:20', '2025-06-06 20:50:50', 1, 0, '2024-10-30 23:48:13', 1, '0', 1, '0', '0', '2022-03-06 18:52:20', '0', '0', '2024-10-27 20:48:13');
INSERT INTO `liam_user` VALUES ('1bad911753332b234be12411de61c5f0', 'X3A9X5VG0PJEVKHT', NULL, 'zh_CN', 'CN', 1, '$2a$10$kGupRXFnSGKUxSZjGXWoPea5Kgx8ejcvi5uNpAXOH6NoiOT9ptUiq', '+86', NULL, 0, 'calmer19961126@163.com', 1, NULL, '2024-10-27 22:41:46', '2034-10-27 22:41:46', 1, 0, NULL, 1, '0', 1, '0', '0', '2024-10-27 22:41:46', '0', '0', '2024-10-27 23:46:20');

-- ----------------------------
-- Table structure for liam_user_group
-- ----------------------------
DROP TABLE IF EXISTS `liam_user_group`;
CREATE TABLE `liam_user_group`  (
  `user_group_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户组表主键',
  `user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '用户ID',
  `user_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户编码(username)',
  `user_group_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户组编码, 与SCOPE编码保持一致',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_group_id`) USING BTREE,
  UNIQUE INDEX `lifetime_user_group_u1`(`user_id` ASC, `user_group_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户组表(scope)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of liam_user_group
-- ----------------------------
INSERT INTO `liam_user_group` VALUES ('1', '1', '', 'TENANT', '0', 1, '0', '0', '2024-09-17 22:43:30', '0', '0', '2024-09-17 22:43:30');
INSERT INTO `liam_user_group` VALUES ('2', '1bad911753332b234be12411de61c5f0', '', 'TENANT', '0', 1, '0', '0', '2024-09-17 22:43:30', '0', '0', '2024-09-17 22:43:30');

-- ----------------------------
-- Table structure for lsys_lov
-- ----------------------------
DROP TABLE IF EXISTS `lsys_lov`;
CREATE TABLE `lsys_lov`  (
  `lov_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集主键',
  `lov_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集代码',
  `lov_type_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'LOV类型：FIXED/URL',
  `lov_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '值集名称',
  `description` varchar(360) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `enabled_flag` tinyint NOT NULL DEFAULT 0 COMMENT '是否启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`lov_id`) USING BTREE,
  UNIQUE INDEX `ztnt_lov_u1`(`lov_code` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lsys_lov
-- ----------------------------
INSERT INTO `lsys_lov` VALUES ('1d3014b271089a267c77f8fb123a71fd', 'SYS_TEMP_TYPE', 'FIXED', '模板类型', '邮件, 短信, 公告, 站内信模板的类型值', 1, '0', 1, '0', '0', '2024-10-26 20:43:47', '0', '0', '2024-10-26 20:43:47');
INSERT INTO `lsys_lov` VALUES ('644fcf7e2063f79f4852cfdb73edd859', 'SYS_LOV_TYPE', 'FIXED', '值集类型', '值集的类型', 1, '0', 1, '0', '0', '2024-10-26 22:40:12', '0', '0', '2024-10-26 22:47:03');

-- ----------------------------
-- Table structure for lsys_lov_value
-- ----------------------------
DROP TABLE IF EXISTS `lsys_lov_value`;
CREATE TABLE `lsys_lov_value`  (
  `lov_value_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '固定值集主键',
  `lov_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集表主键, ztnt_lov.lovid',
  `lov_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值集代码',
  `value_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值代码',
  `value_content` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '值内容',
  `description` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `value_order` int NOT NULL DEFAULT 0 COMMENT '排序号',
  `enabled_flag` tinyint NOT NULL DEFAULT 1 COMMENT '生效标识：1:生效，0:失效',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`lov_value_id`) USING BTREE,
  INDEX `ztnt_lov_value_n1`(`lov_id` ASC, `value_order` ASC) USING BTREE,
  INDEX `ztnt_lov_value_n2`(`lov_code` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lsys_lov_value
-- ----------------------------
INSERT INTO `lsys_lov_value` VALUES ('20676110b67b3a4e32f9a6f2fd980c0d', '1d3014b271089a267c77f8fb123a71fd', 'SYS_TEMP_TYPE', 'MESSAGES', '站内信', NULL, '0', 12, 1, 1, '0', '0', '2024-10-26 22:31:41', '0', '0', '2024-10-26 22:31:41');
INSERT INTO `lsys_lov_value` VALUES ('2b732937b255d231e9f831580baeb5c3', '644fcf7e2063f79f4852cfdb73edd859', 'SYS_LOV_TYPE', 'FIXED', '常量', NULL, '0', 9, 1, 1, '0', '0', '2024-10-27 00:11:54', '0', '0', '2024-10-27 00:12:23');
INSERT INTO `lsys_lov_value` VALUES ('4233455f421a2c53edc500f8c0e763d5', '1d3014b271089a267c77f8fb123a71fd', 'SYS_TEMP_TYPE', 'SMS', '短信', NULL, '0', 2, 1, 1, '0', '0', '2024-10-26 22:29:40', '0', '0', '2024-10-26 22:29:40');
INSERT INTO `lsys_lov_value` VALUES ('9c83195783781a5dc5e4fecec24fff92', '1d3014b271089a267c77f8fb123a71fd', 'SYS_TEMP_TYPE', 'ANNOUNCEMENT', '公告', NULL, '0', 6, 1, 1, '0', '0', '2024-10-26 22:31:20', '0', '0', '2024-10-26 22:31:20');
INSERT INTO `lsys_lov_value` VALUES ('b83bd6ec28f6c1136edb17b30230270c', '644fcf7e2063f79f4852cfdb73edd859', 'SYS_LOV_TYPE', 'URL', 'URL', NULL, '0', 6, 1, 1, '0', '0', '2024-10-27 00:12:06', '0', '0', '2024-10-27 00:14:23');
INSERT INTO `lsys_lov_value` VALUES ('c61d14b6a2d8e106d115cdef5d79bb76', '1d3014b271089a267c77f8fb123a71fd', 'SYS_TEMP_TYPE', 'EMAIL', '邮件', '', '0', 7, 1, 1, '0', '0', '2024-10-26 22:27:40', '0', '0', '2024-10-26 22:37:40');

-- ----------------------------
-- Table structure for lsys_resource
-- ----------------------------
DROP TABLE IF EXISTS `lsys_resource`;
CREATE TABLE `lsys_resource`  (
  `resource_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `parent_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '父级ID',
  `menu_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源类型:菜单(MENU),目录(CATALOGUE),按钮(BUTTON)',
  `icon` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
  `menu_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '路由名称',
  `path` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '路由路径',
  `component` varchar(480) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '组件页面路径',
  `describe` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源描述',
  `level` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '等级:0-顶级;1-(依次排列)',
  `is_hide` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否隐藏',
  `is_link` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '链接地址',
  `is_keep_alive` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否缓存',
  `is_spread` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '是否折叠',
  `is_full` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否全屏',
  `is_affix` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '是否固定',
  `auth` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限',
  `enabled_flag` tinyint NOT NULL DEFAULT 0 COMMENT '资源是否启用',
  `sorted` int NOT NULL DEFAULT 1 COMMENT '排序',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '权限资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_resource
-- ----------------------------

-- ----------------------------
-- Table structure for lsys_role
-- ----------------------------
DROP TABLE IF EXISTS `lsys_role`;
CREATE TABLE `lsys_role`  (
  `role_id` varchar(120) CHARACTER SET utf32 COLLATE utf32_general_ci NOT NULL COMMENT '主键ID',
  `role_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色编码',
  `role_desc` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `enable_flag` tinyint NOT NULL DEFAULT 0 COMMENT '角色是否启用',
  `sorted` bigint NULL DEFAULT 1 COMMENT '排序',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `role_code_unique`(`role_code` ASC) USING BTREE COMMENT '角色编码是唯一值'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_role
-- ----------------------------

-- ----------------------------
-- Table structure for lsys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `lsys_role_resource`;
CREATE TABLE `lsys_role_resource`  (
  `role_resource_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `res_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源编码',
  `role_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色编码',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`role_resource_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色与资源关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_role_resource
-- ----------------------------

-- ----------------------------
-- Table structure for lsys_temp
-- ----------------------------
DROP TABLE IF EXISTS `lsys_temp`;
CREATE TABLE `lsys_temp`  (
  `temp_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模版ID',
  `temp_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '编码',
  `temp_desc` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模版描述',
  `temp_type` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板类型',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模版内容',
  `enabled_flag` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`temp_id`) USING BTREE,
  UNIQUE INDEX `temp_code_unique`(`temp_code` ASC) USING BTREE COMMENT '模板编码唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模版' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_temp
-- ----------------------------
INSERT INTO `lsys_temp` VALUES ('486426b384cb6b7197b9599df3f2f5ca', 'REGISTER_CAPTCHA_CODE_TEMPLATE', '《命运迷雾》官方后台用户注册邮件', 'EMAIL', '尊敬的用户您好! 您的验证码是: {captchaCode}, 请在10分钟内进行验证. 如果该验证码不是您本人申请,请无视.', 1, '0', 1, '0', '0', '2024-10-02 14:25:06', '1', NULL, '2024-10-27 23:12:38');
INSERT INTO `lsys_temp` VALUES ('73083ed156dfe989282a2e01c2df7177', 'RESET_PASSWORD_CAPTCHA_CODE_TEMPLATE', '《命运迷雾》官方后台用户注册邮件', 'EMAIL', '尊敬的用户您好! 您目前正在重置密码, 验证码是: {captchaCode}, 请在10分钟内进行验证. 如果该验证码不是您本人申请,请无视.', 1, '0', 1, '1', NULL, '2024-10-27 23:12:11', '1', NULL, '2024-10-27 23:12:32');

-- ----------------------------
-- Table structure for lsys_temp_user
-- ----------------------------
DROP TABLE IF EXISTS `lsys_temp_user`;
CREATE TABLE `lsys_temp_user`  (
  `temp_user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `temp_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '模板编码',
  `user_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户ID',
  `username` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户编码',
  `enabled_flag` tinyint NULL DEFAULT 0 COMMENT '状态：1-启用；0-未启用',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`temp_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模板接收人员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_temp_user
-- ----------------------------

-- ----------------------------
-- Table structure for lsys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `lsys_tenant`;
CREATE TABLE `lsys_tenant`  (
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `tenant_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '租户编码',
  `tenant_name` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '租户名称',
  `enabled_flag` tinyint NOT NULL DEFAULT 0 COMMENT '租户是否启用',
  `certified_flag` tinyint NULL DEFAULT 0 COMMENT '认证：true（1）；没认证：false（0）',
  `certified_time` datetime NULL DEFAULT NULL COMMENT '认证时间',
  `domain_flag` tinyint NULL DEFAULT 0 COMMENT '域名标记',
  `domain_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '租户域名',
  `version_num` bigint NULL DEFAULT NULL COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '租户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_tenant
-- ----------------------------

-- ----------------------------
-- Table structure for lsys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `lsys_user_role`;
CREATE TABLE `lsys_user_role`  (
  `user_role_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `role_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色编码',
  `user_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户编码(username)',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户与角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_user_role
-- ----------------------------

-- ----------------------------
-- Table structure for lsys_user_tenant
-- ----------------------------
DROP TABLE IF EXISTS `lsys_user_tenant`;
CREATE TABLE `lsys_user_tenant`  (
  `user_tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键ID',
  `user_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户编码',
  `tenant_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '租户编码',
  `default_flag` tinyint NOT NULL DEFAULT 0 COMMENT '用户与租户的默认标识',
  `tenant_id` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建用户',
  `created_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新用户',
  `updated_by_emp` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户与租户关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lsys_user_tenant
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
