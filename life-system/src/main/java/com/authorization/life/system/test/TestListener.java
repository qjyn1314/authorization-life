package com.authorization.life.system.test;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * TODO 请填写类描述
 *
 * @author wangjunming
 * @date 2023/2/20 16:11
 */
@Slf4j
@Component
public class TestListener {

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationContextEvent.class)
    public void startedListener(ApplicationContextEvent contextEvent) throws SQLException {
        log.info("dataSource-{}",dataSource);
        log.info("dataSource-{}",dataSource.getConnection());
    }

}
