package com.authorization.mybatis.start;

import com.authorization.mybatis.start.entity.AuditEntity;
import com.authorization.utils.security.UserThreadUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * <p>
 * mybatis的sql注入器
 * </p>
 *
 * @author wangjunming
 * @since 2024-10-01 21:09
 */
public class InitAuditMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName(AuditEntity.FIELD_TENANT_ID, UserThreadUtil.getUserContext().getTenantId(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_CREATED_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_CREATED_BY_USER, UserThreadUtil.getUserContext().getUserId(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_CREATED_BY_EMP, UserThreadUtil.getUserContext().getEmpId(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_UPDATED_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_UPDATED_BY_USER, UserThreadUtil.getUserContext().getUserId(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_UPDATED_BY_EMP, UserThreadUtil.getUserContext().getEmpId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName(AuditEntity.FIELD_UPDATED_TIME, LocalDateTime.now(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_UPDATED_BY_USER, UserThreadUtil.getUserContext().getUserId(), metaObject);
        this.setFieldValByName(AuditEntity.FIELD_UPDATED_BY_EMP, UserThreadUtil.getUserContext().getEmpId(), metaObject);
    }
}
