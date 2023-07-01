SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for lemd_tenant
-- ----------------------------
DROP TABLE IF EXISTS `lemd_tenant`;
CREATE TABLE `lemd_tenant`  (
  `tenant_id` bigint NOT NULL AUTO_INCREMENT COMMENT '租户id',
  `tenant_name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户名称',
  `tenant_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户编码',
  `certified_flag` tinyint NULL DEFAULT 0 COMMENT '认证：true（1）；没认证：false（0）',
  `certified_time` datetime NULL DEFAULT NULL COMMENT '认证时间',
  `company_id` bigint NULL DEFAULT NULL COMMENT '公司id',
  `supplier_flag` tinyint NULL DEFAULT 0 COMMENT '供应商标识',
  `purchase_flag` tinyint NULL DEFAULT 0 COMMENT '采购商标识',
  `version_num` bigint NULL DEFAULT 1 COMMENT '版本号',
  `created_by_user` bigint NULL DEFAULT 0 COMMENT '创建用户',
  `created_by_emp` bigint NULL DEFAULT 0 COMMENT '创建员工',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by_user` bigint NULL DEFAULT 0 COMMENT '最后更新用户',
  `updated_by_emp` bigint NULL DEFAULT 0 COMMENT '最后更新员工',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`tenant_id`) USING BTREE,
  UNIQUE INDEX `lemd_tenant_u1`(`tenant_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户信息表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
