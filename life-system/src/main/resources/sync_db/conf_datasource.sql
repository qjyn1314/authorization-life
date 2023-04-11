create table if not exists conf_datasource
(
    id              int auto_increment comment '主键'
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
) comment '数据源表';