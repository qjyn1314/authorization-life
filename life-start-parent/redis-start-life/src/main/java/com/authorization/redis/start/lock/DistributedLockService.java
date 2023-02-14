package com.authorization.redis.start.lock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;

/**
 * 分布式锁的实现类
 * <p>
 * springboot 的 扩展点 :
 * https://juejin.cn/post/6999603401239576583
 * <p>
 * https://segmentfault.com/a/1190000023033670
 * <p>
 * 带有结论:
 * https://zhuanlan.zhihu.com/p/385038512
 * <p>
 * https://blog.csdn.net/justry_deng/article/details/124284200
 *
 * @author wangjunming
 * @date 2023/2/13 10:20
 */
@Slf4j
public class DistributedLockService {

    private static RedissonClient staticRedissonClient;
    private static RedissonReactiveClient staticRedissonReactiveClient;

    public DistributedLockService(RedissonClient redissonClient, RedissonReactiveClient redissonReactiveClient) {
        DistributedLockService.staticRedissonClient = redissonClient;
        DistributedLockService.staticRedissonReactiveClient = redissonReactiveClient;
    }

    public static RedissonClient getStaticRedissonClient() {
        return staticRedissonClient;
    }

    public static RedissonReactiveClient getStaticRedissonReactiveClient() {
        return staticRedissonReactiveClient;
    }

    /**
     * 加锁
     *
     * @param lockKey 锁的key
     */
    public void lock(String lockKey) {
        RLock lock = getStaticRedissonClient().getLock(lockKey);
        lock.lock();
    }

    /**
     * 解锁
     *
     * @param lockKey 锁的key
     */
    public void unlock(String lockKey) {
        RLock lock = getStaticRedissonClient().getLock(lockKey);
        lock.unlock();
    }

}
