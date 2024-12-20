package com.authorization.utils.excutor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 自定义线程池配置类
 * </p>
 *
 * @author wangjunming
 * @since 2024-08-14 12:13
 */
@Slf4j
public class TaskExecutorService {


    /**
     * 定时任务的线程池名称
     */
    public static final String SCHEDULED_TASKS_NAME = "AUTHORIZATION-LIFE-SCHEDULED-TASK-";
    /**
     * 核心线程数:设置核心线程数为 CPU 核数，获取不到时，将创建默认核心线程数为 8 个。
     */
    private static final int CORE_POOL_SIZE = ExecutorManager.getCpuProcessors();

    /**
     * 设置线程池的目的是需要让定时任务不浪费主线程的情况下，尽最大努力的去执行任务，如果执行到了拒绝策略，那么交给调用方执行此任务。
     * <p>
     * 拒绝策略：jdk中自带的 ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务
     * <p>
     * 线程池中线程名字：spring自带的  CustomizableThreadFactory
     * <p>
     * 参考：
     * <p>
     * jdk自带的拒绝策略详解：https://blog.csdn.net/suifeng629/article/details/98884972
     * <p>
     * 第三方框架对于拒绝策略的实现(例如dubbo、activemq、netty、)：http://www.kailing.pub/article/index/arcid/255.html
     * <p>
     * 线程池中的线程名字：https://blog.csdn.net/u010648555/article/details/106137206
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService scheduledAuthExecutor() {
        log.info("Init ScheduledExecutorService corePoolSize ：{}", CORE_POOL_SIZE);
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE,
                new CustomizableThreadFactory(SCHEDULED_TASKS_NAME),
                new ThreadPoolExecutor.CallerRunsPolicy());
        ExecutorManager.registerAndMonitorThreadPoolExecutor(SCHEDULED_TASKS_NAME, scheduledThreadPoolExecutor);
        return scheduledThreadPoolExecutor;
    }

    /**
     * 自定义线程池
     *
     * @author wangjunming
     * @since 2020/11/22 21:35
     */
    @Bean(name = "defaultExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor defaultExecutor() {
        return ExecutorManager.getDefaultExecutor();
    }

}
