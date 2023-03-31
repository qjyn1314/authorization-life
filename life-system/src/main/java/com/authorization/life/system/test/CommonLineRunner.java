package com.authorization.life.system.test;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * 整个项目启动完毕后 所需要执行的操作
 *
 * @author wangjunming
 * @date 2023/2/22 18:07
 */
@Slf4j
@Component
public class CommonLineRunner implements CommandLineRunner {

    @Autowired
    private List<DataSource> dateSourceList;
    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        log.info("dateSourceList->{}", JSONUtil.toJsonStr(dateSourceList));
        log.info("dataSource-{}", dataSource);
        try {
            log.info("dataSource-{}", dataSource.getConnection());
        } catch (SQLException e) {
            log.error("获取数据库连接失败, ", e);
        }
    }

}
