//package com.authorization.life.datasource.start.datasource.sharding;
//
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//
//import java.util.Collection;
//import java.util.Optional;
//
///**
// * TODO 请填写类描述
// *
// * @author wangjunming
// * @date 2023/3/31 16:30
// */
//public class FetchStringTenantIdTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {
//
//    @Override
//    public String doSharding(Collection<String> collection, PreciseShardingValue<String> shardingValue) {
//
//        // 特殊处理，为了不用每次添加客户就重新建表，用来将数据
//        Optional<String> first = collection.stream().findFirst();
//        if (first.isPresent()) {
//            String str = first.get();
//            String substring = str.substring(0, str.lastIndexOf('_'));
//            //优化当前的分键值
//            return substring + "_" + shardingValue.getValue();
//
//        } else {
//            throw new UnsupportedOperationException();
//        }
//
//
//    }
//
//
//}