package com.authorization.system.test_redis;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.authorization.system.log.LogAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class RedisServerTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 定时任务进行redis的消息发布。
     */
    @LogAdvice(name = "定时任务进行redis的消息发布")
//    @Scheduled(cron = "0/5 * * * * ?") //每秒执行一次
    public void redisNewsRelease() {
        //获取当前时间并设置时分秒为 00:00:00 ，即 2020-05-13 00:00:00
//        LocalDateTime zeroTime = LocalDateTime.now().withHour(0).withSecond(0).withMinute(0);
//        String zeroTimeFormat = DateUtil.format(zeroTime, DatePattern.NORM_DATETIME_PATTERN);
//        log.info("开始执行-查询条件是-{}", zeroTimeFormat);
        LocalDateTime nowTime = LocalDateTime.now();
        String nowTimeFormat = DateUtil.format(nowTime, DatePattern.NORM_DATETIME_PATTERN);
        log.info("执行发布-消息内容是-{}", nowTimeFormat);
        //发布消息
        redisTemplate.convertAndSend(RedisTestConstants.REDIS_TEST_TOPIC_01, nowTimeFormat);
    }

//    /**
//     * 定时任务进行redis的保存数据。
//     */
//    @LogAdvice(name = "定时任务进行redis的保存数据")
//    @Scheduled(cron = "0/5 * * * * ?") //每几秒执行一次
//    public void redisDataSave() {
//        //保存数据
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        LocalDateTime nowTime = LocalDateTime.now();
//        String nowTimeFormat = DateUtil.format(nowTime, DatePattern.NORM_DATETIME_PATTERN);
//
//        LocalDateTime zeroTime = nowTime.withHour(0).withSecond(0);
//        String zeroTimeFormat = DateUtil.format(zeroTime, DatePattern.NORM_DATETIME_PATTERN);
//        valueOperations.set(zeroTimeFormat + "nowTimeFormat", nowTimeFormat);
//
//    }


}
