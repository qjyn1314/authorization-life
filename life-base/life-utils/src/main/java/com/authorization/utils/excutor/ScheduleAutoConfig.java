package com.authorization.utils.excutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 定时任务配置信息
 *
 * @author wangjunming
 * @since 2022/5/13 14:08
 */
@Slf4j
@EnableScheduling
public class ScheduleAutoConfig implements SchedulingConfigurer {

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(scheduledExecutorService);
        log.info("Init Scheduled...");
    }

}
