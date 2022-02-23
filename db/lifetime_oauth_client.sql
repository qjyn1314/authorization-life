/*
    oauth2相关登录表
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lifetime_oauth_client
-- ----------------------------
DROP TABLE IF EXISTS `lifetime_oauth_client`;
CREATE TABLE `lifetime_oauth_client`  (
  `oauth_client_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'oauth客户端表主键',
  `client_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `client_secret` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端密钥',
  `grant_types` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权类型',
  `scopes` varchar(90) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权域',
  `redirect_uri` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '重定向地址',
  `access_token_timeout` int(11) NOT NULL COMMENT '访问授权超时时间',
  `refresh_token_timeout` int(11) NOT NULL COMMENT '刷新授权超时时间',
  `additional_information` varchar(240) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '附加信息',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
  `created_by_user` bigint(20) NULL DEFAULT 0 COMMENT '创建用户',
  `created_by_emp` bigint(20) NULL DEFAULT 0 COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` bigint(20) NULL DEFAULT 0 COMMENT '最后更新用户',
  `updated_by_emp` bigint(20) NULL DEFAULT 0 COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`oauth_client_id`) USING BTREE,
  UNIQUE INDEX `ziam_oauth_client_u1`(`client_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'oauth客户端表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lifetime_user
-- ----------------------------
DROP TABLE IF EXISTS `lifetime_user`;
CREATE TABLE `lifetime_user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户表主键',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户编码',
  `real_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `lang` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'zh_CN' COMMENT '语言,默认是中文',
  `locale` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'CN' COMMENT '地区,默认是中国',
  `gender` tinyint(3) NULL DEFAULT 1 COMMENT '用户性别:1:男 2:女 3：未知',
  `hash_password` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `tel_area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '+86' COMMENT '电话区号。',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `phone_checked_flag` tinyint(3) NULL DEFAULT 0 COMMENT '手机号通过验证标识',
  `email` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `email_checked_flag` tinyint(3) NULL DEFAULT 0 COMMENT '邮箱通过验证标识',
  `birthday` datetime NULL DEFAULT NULL COMMENT '出生日期',
  `effective_start_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生效开始日期',
  `effective_end_date` datetime NULL DEFAULT NULL COMMENT '生效截至日期',
  `actived_flag` tinyint(3) NULL DEFAULT 0 COMMENT '已激活标识',
  `locked_flag` tinyint(3) NULL DEFAULT 0 COMMENT '已锁定标识',
  `locked_time` datetime NULL DEFAULT NULL COMMENT '锁定时间',
  `enabled_flag` tinyint(3) NULL DEFAULT 0 COMMENT '状态：1-启用；0-未启用',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
  `version_num` bigint(20) NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` bigint(20) NULL DEFAULT 0 COMMENT '创建用户',
  `created_by_emp` bigint(20) NULL DEFAULT 0 COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` bigint(20) NULL DEFAULT 0 COMMENT '最后更新用户',
  `updated_by_emp` bigint(20) NULL DEFAULT 0 COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ziam_user_u1`(`username`) USING BTREE,
  UNIQUE INDEX `ziam_user_u2`(`phone`) USING BTREE,
  UNIQUE INDEX `ziam_user_u3`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for lifetime_user_group
-- ----------------------------
DROP TABLE IF EXISTS `lifetime_user_group`;
CREATE TABLE `lifetime_user_group`  (
  `user_group_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户组表主键',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `user_group_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户组编码, 与SCOPE编码保持一致',
  `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID',
  `created_by_user` bigint(20) NULL DEFAULT 0 COMMENT '创建用户',
  `created_by_emp` bigint(20) NULL DEFAULT 0 COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` bigint(20) NULL DEFAULT 0 COMMENT '最后更新用户',
  `updated_by_emp` bigint(20) NULL DEFAULT 0 COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_group_id`) USING BTREE,
  UNIQUE INDEX `ziam_user_group_u1`(`user_id`, `user_group_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户组表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
