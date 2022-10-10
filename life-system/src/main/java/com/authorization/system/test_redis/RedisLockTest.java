package com.authorization.system.test_redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisLockTest {

    @Autowired
    private RedissonClient redisson;

    public void lockTest(){
        RLock lock = redisson.getLock("");
        lock.lock();
        lock.unlock();
    }

}
