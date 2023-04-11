create table if not exists conf_datasource
(
    datasource_id   int auto_increment comment '主键'
        primary key,
    service_name    varchar(60)                        null comment '服务名称',
    datasource_name varchar(64)                        null comment 'SPRING中动态数据源的名称',
    database_name   varchar(64)                        null comment '数据库名称',
    database_ip     varchar(64)                        null comment '数据库IP',
    database_port   varchar(10)                        null comment '数据库端口号',
    username        varchar(64)                        null comment '用户名',
    password        varchar(64)                        null comment '密码',
    url             varchar(500)                       null comment 'jdbc_url',
    create_date     datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_date     datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新',
    del_flag        char     default '0'               null comment '删除标记'
)
    comment '数据源表';


create table if not exists life_tenant
(
    tenant_id       bigint auto_increment comment '租户id'
        primary key,
    tenant_name     varchar(60)                        not null comment '租户名称',
    tenant_code     varchar(30)                        not null comment '租户编码',
    certified_flag  tinyint  default 0                 null comment '认证：true（1）；没认证：false（0）',
    certified_time  datetime                           null comment '认证时间',
    company_id      bigint                             null comment '公司id',
    supplier_flag   tinyint  default 0                 null comment '供应商标识',
    purchase_flag   tinyint  default 0                 null comment '采购商标识',
    version_num     bigint   default 1                 null comment '版本号',
    created_by_user bigint   default 0                 null comment '创建用户',
    created_by_emp  bigint   default 0                 null comment '创建员工',
    created_time    datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updated_by_user bigint   default 0                 null comment '最后更新用户',
    updated_by_emp  bigint   default 0                 null comment '最后更新员工',
    updated_time    datetime default CURRENT_TIMESTAMP null comment '最后更新时间',
    constraint life_tenant_u1
        unique (tenant_code)
)
    comment '租户信息表' row_format = DYNAMIC;

