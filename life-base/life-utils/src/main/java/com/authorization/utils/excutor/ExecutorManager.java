package com.authorization.utils.excutor;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.util.Map;
import java.util.concurrent.*;


/**
 * 线程池管理器
 */
@Slf4j
public class ExecutorManager {

    /**
     * 线程池管理器
     */
    private static final ConcurrentHashMap<String, ThreadPoolExecutor> EXECUTORS = new ConcurrentHashMap<>(8);
    /**
     * 默认线程池
     */
    private static final ThreadPoolExecutor DEFAULT_EXECUTOR;

    /**
     * 默认线程池的名字
     */
    public static final String DEFAULT_EXECUTOR_NAME = "DefaultExecutor";

    static {
        DEFAULT_EXECUTOR = buildThreadFirstExecutor(DEFAULT_EXECUTOR_NAME);
    }

    /**
     * 获取默认构造的通用线程池，线程池核心是为 CPU 核数，最大线程数为 8倍 CPU 核数
     *
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor getDefaultExecutor() {
        return DEFAULT_EXECUTOR;
    }

    /**
     * 构建线程优先的线程池
     * <p>
     * 默认线程池, 是当核心线程数满了后，将任务添加到工作队列中，当工作队列满了之后，再创建线程直到达到最大线程数。
     * <p>
     * 优先线程的线程池, 是在核心线程满了之后，继续创建线程，直到达到最大线程数之后，再把任务添加到工作队列中。
     * <p>
     * 此方法默认设置核心线程数为 CPU 核数，最大线程数为 8倍 CPU 核数，空闲线程超过 5 分钟销毁，工作队列大小为 65536。
     *
     * @param poolName 线程池名称
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor buildThreadFirstExecutor(String poolName) {
        int coreSize = ExecutorManager.getCpuProcessors();
        int maxSize = coreSize * 8;
        return buildThreadFirstExecutor(coreSize, maxSize, 5, TimeUnit.MINUTES, 1 << 16, poolName);
    }

    /**
     * 构建线程优先的线程池
     * <p>
     * jdk线程池默认是当核心线程数满了后，将任务添加到工作队列中，当工作队列满了之后，再创建线程直到达到最大线程数。
     * <p>
     * 线程优先的线程池，就是在核心线程满了之后，继续创建线程，直到达到最大线程数之后，再把任务添加到工作队列中,进入拒绝策略的时候也将任务放入队列。
     *
     * @param corePoolSize    核心线程数
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime   空闲线程的空闲时间
     * @param unit            时间单位
     * @param workQueueSize   工作队列容量大小
     * @param poolName        线程池名称
     * @return ThreadPoolExecutor
     */
    public static ThreadPoolExecutor buildThreadFirstExecutor(int corePoolSize,
                                                              int maximumPoolSize,
                                                              long keepAliveTime,
                                                              TimeUnit unit,
                                                              int workQueueSize,
                                                              String poolName) {
        // 自定义队列，优先开启更多线程，而不是放入队列
        LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(workQueueSize) {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public boolean offer(@Nonnull Runnable o) {
                return false; // 造成队列已满的假象
            }
        };

        // 当线程达到 maximumPoolSize 时会触发拒绝策略，此时将任务 put 到队列中
        RejectedExecutionHandler rejectedExecutionHandler = (runnable, executor) -> {
            try {
                // 任务拒绝时，通过 offer 放入队列
                queue.put(runnable);
            } catch (InterruptedException e) {
                log.warn("{} Queue offer interrupted. ", poolName, e);
                Thread.currentThread().interrupt();
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                queue,
                new ThreadFactoryBuilder()
                        .setNameFormat(poolName + "-%d")
                        .setUncaughtExceptionHandler((Thread thread, Throwable throwable) ->
                                log.error("{} catching the uncaught exception, ThreadName: [{}]", poolName, thread.toString(), throwable))
                        .build(),
                rejectedExecutionHandler
        );

        executor.allowCoreThreadTimeOut(true);

        registerAndMonitorThreadPoolExecutor(poolName, executor);

        return executor;
    }

    /**
     * 根据一定周期输出线程池的状态
     *
     * @param threadPool     线程池
     * @param threadPoolName 线程池名称
     */
    private static void displayThreadPoolStatus(ThreadPoolExecutor threadPool, String threadPoolName) {
        // 每60*3秒输出一下线程池的状态
        int randomInt = 180;//RandomUtil.randomInt(1800);
        log.info("ThreadPool Name: [{}], in every [{}] seconds output state. ", threadPoolName, randomInt);
        displayThreadPoolStatus(threadPool, threadPoolName, randomInt, TimeUnit.SECONDS);
    }

    /**
     * 根据一定周期输出线程池的状态
     *
     * @param threadPool     线程池
     * @param threadPoolName 线程池名称
     * @param period         周期
     * @param unit           时间单位
     */
    private static void displayThreadPoolStatus(ThreadPoolExecutor threadPool, String threadPoolName, long period, TimeUnit unit) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            String payload = "[ExecutorManager] Executor Status:[ThreadPool Name: [{}], Pool Status: [shutdown={}, Terminated={}], Pool Thread Size: {}, Largest Pool Size: {}, Active Thread Count: {}, Task Count: {}, Tasks Completed: {}, Tasks in Queue: {}]";
            Object[] params = new Object[]{threadPoolName,
                    threadPool.isShutdown(), threadPool.isTerminated(), // 线程是否被终止
                    threadPool.getPoolSize(), // 线程池线程数量
                    threadPool.getLargestPoolSize(), // 线程最大达到的数量
                    threadPool.getActiveCount(), // 工作线程数
                    threadPool.getTaskCount(), // 总任务数
                    threadPool.getCompletedTaskCount(), // 已完成的任务数
                    threadPool.getQueue().size()// 队列数量
            };

            if (threadPool.getQueue().remainingCapacity() < 64) {
                log.warn(payload, params);
            } else {
                log.info(payload, params);
            }
        }, 0, period, unit);
    }

    /**
     * 添加Hook在Jvm关闭时优雅的关闭线程池
     *
     * @param threadPool     线程池
     * @param threadPoolName 线程池名称
     */
    private static void hookShutdownThreadPool(ExecutorService threadPool, String threadPoolName) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("[ExecutorManager.ShutdownHook] Start to shutdown the thead pool: [{}]", threadPoolName);
            // 使新任务无法提交
            threadPool.shutdown();
            try {
                // 等待未完成任务结束
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                    threadPool.shutdownNow(); // 取消当前执行的任务
                    log.warn("[ExecutorManager.ShutdownHook] Interrupt the worker, which may cause some task inconsistent. Please check the biz logs.");

                    // 等待任务取消的响应
                    if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                        log.error("[ExecutorManager.ShutdownHook] Thread pool can't be shutdown even with interrupting worker threads, which may cause some task inconsistent. Please check the biz logs.");
                    }
                }
            } catch (InterruptedException ie) {
                // 重新取消当前线程进行中断
                threadPool.shutdownNow();
                log.error("[ExecutorManager.ShutdownHook] The current server thread is interrupted when it is trying to stop the worker threads. This may leave an inconsistent state. Please check the biz logs.");

                // 保留中断状态
                Thread.currentThread().interrupt();
            }

            log.info("[ExecutorManager.ShutdownHook] Finally shutdown the thead pool: [{}]", threadPoolName);
        }));
    }

    /**
     * 获取返回CPU核数
     * <p>
     * 核心线程数:设置核心线程数为 CPU 核数，获取不到时，将创建默认核心线程数为 8 个。
     *
     * @return 返回CPU核数，默认为8
     */
    public static int getCpuProcessors() {
        return Runtime.getRuntime() != null && Runtime.getRuntime().availableProcessors() > 0 ?
                Runtime.getRuntime().availableProcessors() : 8;
    }

    /**
     * 向管理器注册线程池
     *
     * @param threadPoolName 线程池名称
     * @param executor       ThreadPoolExecutor
     */
    private static void registerThreadPoolExecutor(String threadPoolName, ThreadPoolExecutor executor) {
        EXECUTORS.put(threadPoolName, executor);
    }

    /**
     * 向管理器注册线程池，并监控线程池状态
     *
     * @param threadPoolName 线程池名称
     * @param executor       ThreadPoolExecutor
     */
    public static void registerAndMonitorThreadPoolExecutor(String threadPoolName, ThreadPoolExecutor executor) {
        registerThreadPoolExecutor(threadPoolName, executor);
        ExecutorManager.displayThreadPoolStatus(executor, threadPoolName);
        ExecutorManager.hookShutdownThreadPool(executor, threadPoolName);
    }

    /**
     * 根据名称获取线程池
     *
     * @param threadPoolName 线程池名称
     */
    public static ThreadPoolExecutor getThreadPoolExecutor(String threadPoolName) {
        return EXECUTORS.get(threadPoolName);
    }

    /**
     * 获取所有已注册的线程池
     *
     * @return ThreadPoolExecutor
     */
    private static Map<String, ThreadPoolExecutor> getAllThreadPoolExecutor() {
        return ImmutableMap.copyOf(EXECUTORS);
    }

    /**
     * 根据名称移除已注册的线程池
     *
     * @param threadPoolName 线程池名称
     */
    private static void removeThreadPoolExecutor(String threadPoolName) {
        EXECUTORS.remove(threadPoolName);
    }

}
