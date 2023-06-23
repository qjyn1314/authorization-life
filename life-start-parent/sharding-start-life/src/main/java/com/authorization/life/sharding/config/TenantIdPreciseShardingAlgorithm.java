package com.authorization.life.sharding.config;


import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;

/**
 * 此处使用标准的分片算法, 重写 doSharding 方法
 */
@Slf4j
public class TenantIdPreciseShardingAlgorithm implements StandardShardingAlgorithm<Long> {

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        // shuchu -> ["lemd_emp_0","lemd_emp_1","lemd_emp_2","lemd_emp_3",...]
//        log.info("availableTargetNames-return String->{}", JSONUtil.toJsonStr(availableTargetNames));
        // shuchu -> {"logicTableName":"lemd_emp","columnName":"tenant_id","dataNodeInfo":{"prefix":"lemd_emp_","suffixMinLength":1,"paddingChar":"0"},"value":1}
        log.info("shardingValue-return String->{}", JSONUtil.toJsonStr(shardingValue));
        String doShardingTableName = shardingValue.getLogicTableName() + "_" + shardingValue.getValue();
        log.info("Actual Execute TableName->{}", doShardingTableName);
        return doShardingTableName;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Long> shardingValue) {
        log.info("availableTargetNames-return Collection<String>->{}", availableTargetNames);
        log.info("shardingValue-return Collection<String>->{}", shardingValue);
        /*
         availableTargetNames    所有分片表集合
         shardingValue  分片属性
           >> logicTableName  逻辑表
           >> columnName 分片健（字段）
           >> dataNodeInfo 为从 SQL 中解析出的分片健的值
           >> prefix 表前缀 -> lemd_emp_
           >> value 表后缀 -> 1
          */
        //拿到分片键的值
        return availableTargetNames;
    }

    @Override
    public String getType() {
        return "CLASS_BASED";
    }

}
