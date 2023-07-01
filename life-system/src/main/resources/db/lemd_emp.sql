SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lemd_emp
-- ----------------------------
DROP TABLE IF EXISTS `lemd_emp`;
CREATE TABLE `lemd_emp`  (
  `emp_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `emp_num` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工编号',
  `emp_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `locale` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'CN' COMMENT '地区,默认是中国',
  `tel_area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '+86' COMMENT '区号：默认+86',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `gender` tinyint NULL DEFAULT 1 COMMENT '用户性别:1:男 2:女 3：未知',
  `birthday` datetime NULL DEFAULT NULL COMMENT '出生日期',
  `office_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任职状态：实习，试用，在职，交接，离职',
  `enabled_flag` tinyint NULL DEFAULT 1 COMMENT '启用：true（1）；禁用：false（0）',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` bigint NULL DEFAULT 0 COMMENT '创建用户',
  `created_by_emp` bigint NULL DEFAULT 0 COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` bigint NULL DEFAULT 0 COMMENT '最后更新用户',
  `updated_by_emp` bigint NULL DEFAULT 0 COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`emp_id`) USING BTREE,
  UNIQUE INDEX `lemd_emp_u1`(`emp_num` ASC, `tenant_id` ASC) USING BTREE,
  UNIQUE INDEX `lemd_emp_u2`(`phone` ASC, `tenant_id` ASC) USING BTREE,
  UNIQUE INDEX `lemd_emp_u3`(`email` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `lemd_emp_n1`(`emp_num` ASC, `phone` ASC, `email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lemd_emp
-- ----------------------------
DROP TABLE IF EXISTS `lemd_emp_0`;
CREATE TABLE `lemd_emp_0`  (
  `emp_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `emp_num` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工编号',
  `emp_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '员工名称',
  `locale` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'CN' COMMENT '地区,默认是中国',
  `tel_area_code` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '+86' COMMENT '区号：默认+86',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `email` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `gender` tinyint NULL DEFAULT 1 COMMENT '用户性别:1:男 2:女 3：未知',
  `birthday` datetime NULL DEFAULT NULL COMMENT '出生日期',
  `office_status` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任职状态：实习，试用，在职，交接，离职',
  `enabled_flag` tinyint NULL DEFAULT 1 COMMENT '启用：true（1）；禁用：false（0）',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` bigint NULL DEFAULT 0 COMMENT '创建用户',
  `created_by_emp` bigint NULL DEFAULT 0 COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` bigint NULL DEFAULT 0 COMMENT '最后更新用户',
  `updated_by_emp` bigint NULL DEFAULT 0 COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`emp_id`) USING BTREE,
  UNIQUE INDEX `lemd_emp_u1`(`emp_num` ASC, `tenant_id` ASC) USING BTREE,
  UNIQUE INDEX `lemd_emp_u2`(`phone` ASC, `tenant_id` ASC) USING BTREE,
  UNIQUE INDEX `lemd_emp_u3`(`email` ASC, `tenant_id` ASC) USING BTREE,
  INDEX `lemd_emp_n1`(`emp_num` ASC, `phone` ASC, `email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
