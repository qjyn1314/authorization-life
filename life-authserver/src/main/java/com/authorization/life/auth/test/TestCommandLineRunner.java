package com.authorization.life.auth.test;

import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.GroupDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 整个项目启动完毕后 所需要执行的操作
 *
 * @author wangjunming
 * @date 2023/2/22 18:07
 */
@Slf4j
@Component
public class TestCommandLineRunner implements CommandLineRunner {

    @Autowired
    private List<DataSource> dateSourceList;

    @Autowired
    private DataSource dataSource;
    @Resource
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Override
    public void run(String... args) {
        log.info("dateSourceList->{}", JSONUtil.toJsonStr(dateSourceList));
        log.info("dateSourceList->{}", DynamicDataSourceContextHolder.peek());
        log.info("dynamicRoutingDataSource->{}", dynamicRoutingDataSource);
        DynamicRoutingDataSource routingDataSource = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> dataSources = routingDataSource.getDataSources();
        Map<String, GroupDataSource> groupDataSources = routingDataSource.getGroupDataSources();
        log.info("routingDataSource->{}", routingDataSource);
        log.info("dataSources->{}", dataSources);
        log.info("groupDataSources->{}", groupDataSources);
    }

}
