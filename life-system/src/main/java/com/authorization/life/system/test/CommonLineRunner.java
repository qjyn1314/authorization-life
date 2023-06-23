package com.authorization.life.system.test;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 整个项目启动完毕后 所需要执行的操作
 *
 * @author wangjunming
 * @date 2023/2/22 18:07
 */
@Slf4j
@Component
public class CommonLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        try {
            DynamicRoutingDataSource routingDataSource = SpringUtil.getBean(DynamicRoutingDataSource.class);
            final Map<String, DataSource> currentDataSources = routingDataSource.getDataSources();
            log.info("当前服务所拥有的的数据源信息->{}", JSONUtil.toJsonStr(currentDataSources));
        } catch (Exception e) {
            log.error("打印数据源信息失败...", e);
        }
    }

}
