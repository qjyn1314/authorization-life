package com.authorization.redis.start.util;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 存储存字符传的redis工具类，
 */
@Slf4j
public class StrRedisHelper {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    public StrRedisHelper(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * 删除key
     *
     * @param key key
     */
    public void delKey(String key) {
        this.currentRestTemplate().delete(key);
    }

    /**
     * 设置过期时间,默认一天
     *
     * @param key key
     * @return object Boolean
     */
    public Boolean setExpire(String key) {
        return this.setExpire(key, RedisConsts.DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间,默认时间单位:秒
     *
     * @param key    key
     * @param expire expire 存活时长
     * @return object
     */
    public Boolean setExpire(String key, long expire) {
        return this.setExpire(key, expire, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key      key
     * @param expire   expire 存活时长
     * @param timeUnit 时间单位
     * @return object
     */
    public Boolean setExpire(String key, long expire, TimeUnit timeUnit) {
        return this.currentRestTemplate().expire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
    }

    /**
     * 像订阅通道发布消息
     *
     * @param channel 订阅topic
     * @param message 消息
     */
    public void convertAndSend(String channel, Object message) {
        this.currentRestTemplate().convertAndSend(channel, toJson(message));
    }

    /**
     * String 设置值
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     */
    public void strSet(String key, String value, long expire, TimeUnit timeUnit) {
        this.getValueOpr().set(key, value, expire, timeUnit);
    }

    /**
     * String 设置值
     *
     * @param key    key
     * @param value  value
     * @param expire expire
     */
    public <T> void strSet(String key, T value, long expire, TimeUnit timeUnit) {
        this.getValueOpr().set(key, this.toJson(value), expire, timeUnit);
    }

    /**
     * String 设置值
     *
     * @param key   key
     * @param value value
     */
    public void strSet(String key, String value) {
        this.getValueOpr().set(key, value);
    }

    /**
     * String 设置值
     *
     * @param key   key
     * @param value value
     */
    public <T> void strSet(String key, T value) {
        this.getValueOpr().set(key, this.toJson(value));
    }

    /**
     * String 获取值
     *
     * @param key key
     * @return object
     */
    public String strGet(String key) {
        return this.getValueOpr().get(key);
    }

    /**
     * String 获取值
     *
     * @param key    key
     * @param expire expire
     * @return object
     */
    public String strGet(String key, long expire, TimeUnit timeUnit) {
        String value = this.getValueOpr().get(key);
        if (!Objects.equals(expire, RedisConsts.NOT_EXPIRE)) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }
        return value;
    }

    /**
     * String 获取值
     *
     * @param key   key
     * @param clazz clazz
     * @return object
     */
    public <T> T strGet(String key, Class<T> clazz) {
        String value = this.getValueOpr().get(key);
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * String 设置值
     *
     * @param key    key
     * @param clazz  clazz
     * @param expire expire
     * @return object
     */
    public <T> T strGet(String key, Class<T> clazz, long expire, TimeUnit timeUnit) {
        String value = this.getValueOpr().get(key);
        if (!Objects.equals(expire, RedisConsts.NOT_EXPIRE)) {
            this.setExpire(key, expire, timeUnit == null ? TimeUnit.SECONDS : timeUnit);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * String 获取值
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return object
     */
    public String strGet(String key, Long start, Long end) {
        return this.getValueOpr().get(key, start, end);
    }

    /**
     * String 获取自增字段，递减字段可使用delta为负数的方式
     *
     * @param key   key
     * @param delta 步长
     * @return object
     */
    public Long strIncrement(String key, Long delta) {
        return this.getValueOpr().increment(key, delta);
    }

    /**
     * List 推入数据至列表左端
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long lstLeftPush(String key, String value) {
        return this.getListOpr().leftPush(key, value);
    }

    /**
     * List 推入数据至列表左端
     *
     * @param key    key
     * @param values Collection集合
     * @return object
     */
    public Long lstLeftPushAll(String key, Collection<String> values) {
        return this.getListOpr().leftPushAll(key, values);
    }

    /**
     * List 推入数据至列表右端
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long lstRightPush(String key, String value) {
        return this.getListOpr().rightPush(key, value);
    }

    /**
     * List 推入数据至列表右端
     *
     * @param key    key
     * @param values Collection集合
     * @return object
     */
    public Long lstRightPushAll(String key, Collection<String> values) {
        return this.getListOpr().rightPushAll(key, values);
    }

    /**
     * List 返回列表键key中，从索引start至索引end范围的所有列表项。两个索引都可以是正数或负数
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return object
     */
    public List<String> lstRange(String key, long start, long end) {
        return this.getListOpr().range(key, start, end);
    }

    /**
     * List 返回列表键key中所有的元素
     *
     * @param key key
     * @return object
     */
    public List<String> lstAll(String key) {
        return this.lstRange(key, 0, this.lstLen(key));
    }

    /**
     * List 移除并返回列表最左端的项
     *
     * @param key key
     * @return object
     */
    public String lstLeftPop(String key) {
        return this.getListOpr().leftPop(key);
    }

    /**
     * List 移除并返回列表最右端的项
     *
     * @param key key
     * @return object
     */
    public String lstRightPop(String key) {
        return this.getListOpr().rightPop(key);
    }

    /**
     * List 返回指定key的长度
     *
     * @param key key
     * @return object
     */
    public Long lstLen(String key) {
        return this.getListOpr().size(key);
    }

    /**
     * List 设置指定索引上的列表项。将列表键 key索引index上的列表项设置为value。 如果index参数超过了列表的索引范围，那么命令返回了一个错误
     *
     * @param key   key
     * @param index 索引位置
     * @param value value
     */
    public void lstSet(String key, long index, String value) {
        this.getListOpr().set(key, index, value);
    }

    /**
     * List 根据参数 count的值，移除列表中与参数value相等的元素。 count的值可以是以下几种：count > 0 :从表头开始向表尾搜索，移除与 value相等的元素，数量为
     * count
     *
     * @param key   key
     * @param index 索引位置
     * @param value value
     * @return object
     */
    public Long lstRemove(String key, long index, String value) {
        return this.getListOpr().remove(key, index, value);
    }

    /**
     * List 返回列表键key中，指定索引index上的列表项。index索引可以是正数或者负数
     *
     * @param key   key
     * @param index 索引位置
     * @return object
     */
    public Object lstIndex(String key, long index) {
        return this.getListOpr().index(key, index);
    }

    /**
     * List 对一个列表进行修剪(trim)，让列表只保留指定索引范围内的列表项，而将不在范围内的其它列表项全部删除。 两个索引都可以是正数或者负数
     *
     * @param key   key
     * @param start 起始位置
     * @param end   结束位置
     */
    public void lstTrim(String key, long start, long end) {
        this.getListOpr().trim(key, start, end);
    }

    /**
     * Set 将一个或多个元素添加到给定的集合里面，已经存在于集合的元素会自动的被忽略， 命令返回新添加到集合的元素数量。
     *
     * @param key    key
     * @param values values
     * @return object
     */
    public Long setAdd(String key, String... values) {
        return this.getSetOpr().add(key, values);
    }

    /**
     * Set 将返回集合中所有的元素。
     *
     * @param key key
     * @return object
     */
    public Set<String> setMembers(String key) {
        return this.getSetOpr().members(key);
    }

    /**
     * Set 检查给定的元素是否存在于集合
     *
     * @param key key
     * @param o   被检测元素
     * @return object
     */
    public Boolean setIsmember(String key, String o) {
        return this.getSetOpr().isMember(key, o);
    }

    /**
     * Set 返回集合包含的元素数量（也即是集合的基数）
     *
     * @param key key
     * @return object
     */
    public Long setSize(String key) {
        return this.getSetOpr().size(key);
    }

    /**
     * Set 计算所有给定集合的交集，并返回结果
     *
     * @param key      key
     * @param otherKey otherKey
     * @return object
     */
    public Set<String> setIntersect(String key, String otherKey) {
        return this.getSetOpr().intersect(key, otherKey);
    }

    /**
     * Set 计算所有的并集并返回结果
     *
     * @param key      key
     * @param otherKey otherKey
     * @return object
     */
    public Set<String> setUnion(String key, String otherKey) {
        return this.getSetOpr().union(key, otherKey);
    }

    /**
     * Set 计算所有的并集并返回结果
     *
     * @param key       key
     * @param otherKeys otherKeys
     * @return object
     */
    public Set<String> setUnion(String key, Collection<String> otherKeys) {
        return this.getSetOpr().union(key, otherKeys);
    }

    /**
     * Set 返回一个集合的全部成员，该集合是所有给定集合之间的差集
     *
     * @param key      key
     * @param otherKey otherKey
     * @return object
     */
    public Set<String> setDifference(String key, String otherKey) {
        return this.getSetOpr().difference(key, otherKey);
    }

    /**
     * Set 返回一个集合的全部成员，该集合是所有给定集合之间的差集
     *
     * @param key       key
     * @param otherKeys otherKeys
     * @return object
     */
    public Set<String> setDifference(String key, Collection<String> otherKeys) {
        return this.getSetOpr().difference(key, otherKeys);
    }

    /**
     * set 删除数据
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long setDel(String key, String value) {
        return this.getSetOpr().remove(key, value);
    }

    /**
     * Set 批量删除数据
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long setRemove(String key, Object[] value) {
        return this.getSetOpr().remove(key, value);
    }

    /**
     * ZSet Zadd 命令用于将一个或多个成员元素及其分数值加入到有序集当中。 如果某个成员已经是有序集的成员，那么更新这个成员的分数值，并通过重新插入这个成员元素，来保证该成员在正确的位置上。
     * 分数值可以是整数值或双精度浮点数。 如果有序集合 key 不存在，则创建一个空的有序集并执行 ZADD 操作。 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key   key
     * @param value value
     * @param score score
     */
    public Boolean zSetAdd(String key, String value, double score) {
        return this.getzSetOpr().add(key, value, score);
    }

    /**
     * ZSet 返回有序集合中，指定元素的分值
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Double zSetScore(String key, String value) {
        return this.getzSetOpr().score(key, value);
    }

    /**
     * ZSet 为有序集合指定元素的分值加上增量increment，命令返回执行操作之后，元素的分值 可以通过将 increment设置为负数来减少分值
     *
     * @param key   key
     * @param value value
     * @param delta 步长
     * @return object
     */
    public Double zSetIncrementScore(String key, String value, double delta) {
        return this.getzSetOpr().incrementScore(key, value, delta);
    }

    /**
     * ZSet 返回指定元素在有序集合中的排名，其中排名按照元素的分值从小到大计算。排名以 0 开始
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long zSetRank(String key, String value) {
        return this.getzSetOpr().rank(key, value);
    }

    /**
     * ZSet 返回成员在有序集合中的逆序排名，其中排名按照元素的分值从大到小计算
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long zSetReverseRank(String key, String value) {
        return this.getzSetOpr().reverseRank(key, value);
    }

    /**
     * ZSet 返回有序集合的基数
     *
     * @param key key
     * @return object
     */
    public Long zSetSize(String key) {
        return this.getzSetOpr().size(key);
    }

    /**
     * ZSet 删除数据
     *
     * @param key   key
     * @param value value
     * @return object
     */
    public Long zSetRemove(String key, String value) {
        return this.getzSetOpr().remove(key, value);
    }

    /**
     * ZSet 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。 具有相同分数值的成员按字典序的逆序(reverse lexicographical order
     * )排列。
     *
     * @param key   key Redis Key
     * @param start 开始位置
     * @param end   结束位置
     * @return object Set
     */
    public Set<String> zSetRange(String key, long start, long end) {
        return this.getzSetOpr().range(key, start, end);
    }

    /**
     * ZSet
     *
     * @param key   key
     * @param start 开始位置
     * @param end   结束位置
     * @return object
     */
    public Set<String> zSetReverseRange(String key, long start, long end) {
        return this.getzSetOpr().reverseRange(key, start, end);
    }

    /**
     * ZSet 返回有序集合在按照分值升序排列元素的情况下，分值在 min 和 max范围之内的所有元素
     *
     * @param key key
     * @param min min
     * @param max max
     * @return object
     */
    public Set<String> zSetRangeByScore(String key, double min, double max) {
        return this.getzSetOpr().rangeByScore(key, min, max);
    }

    /**
     * ZSet 返回有序集合在按照分值升序排列元素的情况下，分值在 min 和 max范围之内的所有元素以及元素的分数
     *
     * @param key key
     * @param min min
     * @param max max
     * @return object
     */
    public Set<ZSetOperations.TypedTuple<String>> zSetRangeByScoreWithScores(String key, double min, double max) {
        return this.getzSetOpr().rangeByScoreWithScores(key, min, max);
    }


    /**
     * ZSet 返回有序集合在按照分值降序排列元素的情况下，分值在 min 和 max范围之内的所有元素
     *
     * @param key key
     * @param min min
     * @param max max
     * @return object
     */
    public Set<String> zSetReverseRangeByScore(String key, double min, double max) {
        return this.getzSetOpr().reverseRangeByScore(key, min, max);
    }

    /**
     * ZSet 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从小到大)的次序排列。 具有相同分数值的成员按字典序的顺序(reverse lexicographical order
     * )排列。
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset 偏移量
     * @param count  count
     * @return object Set
     */
    public Set<String> zSetRangeByScore(String key, double min, double max, long offset, long count) {
        return this.getzSetOpr().rangeByScore(key, min, max, offset, count);
    }


    /**
     * ZSet 返回有序集中指定分数区间内的所有的成员并携带分数。有序集成员按分数值递减(从小到大)的次序排列。 具有相同分数值的成员按字典序的顺序(reverse lexicographical order
     * )排列。
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset 偏移量
     * @param count  count
     * @return object Set
     */
    public Set<ZSetOperations.TypedTuple<String>> zSetRangeByScoreWithScores(String key, double min, double max, long offset, long count) {
        return this.getzSetOpr().rangeByScoreWithScores(key, min, max, offset, count);
    }


    /**
     * 返回有序集中指定分数区间内的所有的成员。有序集成员按分数值递减(从大到小)的次序排列。 具有相同分数值的成员按字典序的逆序(reverse lexicographical order )排列。
     *
     * @param key    key
     * @param min    min
     * @param max    max
     * @param offset 偏移量
     * @param count  count
     * @return object
     */
    public Set<String> zSetReverseRangeByScore(String key, double min, double max, long offset, long count) {
        return this.getzSetOpr().reverseRangeByScore(key, min, max, offset, count);
    }

    /**
     * ZSet 返回有序集合在升序排列元素的情况下，分值在 min和 max范围内的元素数量
     *
     * @param key key
     * @param min min
     * @param max max
     * @return object
     */
    public Long zSetCount(String key, Double min, Double max) {
        return this.getzSetOpr().count(key, min, max);
    }

    /**
     * Hash 将哈希表 key 中的域 field的值设为 value。如果 key不存在，一个新的哈希表被创建并进行HSET操作。 如果域 field已经存在于哈希表中，旧值将被覆盖
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     */
    public void hashPut(String key, String hashKey, String value) {
        this.getHashOpr().put(key, hashKey, value);
    }

    /**
     * Hash 批量插入值，Map的key代表Field
     *
     * @param key key
     * @param map map
     */
    public void hashPutAll(String key, Map<String, String> map) {
        this.getHashOpr().putAll(key, map);
    }

    /**
     * 获取hash对象中的对象序列字符
     *
     * @param key     key
     * @param hashKey hashKey
     * @return object
     */
    public byte[] hashGetSerial(String key, String hashKey) {
        RedisSerializer<String> redisSerializer = this.currentRestTemplate().getStringSerializer();
        Assert.notNull(redisSerializer, "redisSerializer not null");
        return this.currentRestTemplate().execute((RedisCallback<byte[]>) connection -> {
            try {
                return connection.hGet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey));
            } catch (Exception e) {
                log.error("获取HASH对象序列失败", e);
            }
            return null;
        });
    }

    /**
     * 插入hash对象序列值
     *
     * @param key     key
     * @param hashKey hashKey
     * @param value   value
     * @return object
     */
    public Boolean hashPutSerial(String key, String hashKey, byte[] value) {
        RedisSerializer<String> redisSerializer = this.currentRestTemplate().getStringSerializer();
        return this.currentRestTemplate().execute((RedisCallback<Boolean>) connection -> {
            try {
                return connection.hSet(redisSerializer.serialize(key), redisSerializer.serialize(hashKey),
                        value);
            } catch (Exception e) {
                log.error("插入HASH对象序列失败", e);
            }
            return Boolean.FALSE;
        });
    }

    /**
     * Hash 返回哈希表 key 中给定域 field的值，返回值：给定域的值。当给定域不存在或是给定 key不存在时，返回 nil。
     *
     * @param key     key
     * @param hashKey hashKey
     * @return object
     */
    public String hashGet(String key, String hashKey) {
        return this.getHashOpr().get(key, hashKey);
    }

    /**
     * Hash 返回散列键 key 中，一个或多个域的值，相当于同时执行多个 HGET
     *
     * @param key      key
     * @param hashKeys hashKeys
     * @return object
     */
    public List<String> hashMultiGet(String key, Collection<String> hashKeys) {
        return this.getHashOpr().multiGet(key, hashKeys);
    }

    /**
     * Hash 获取散列Key中所有的键值对
     *
     * @param key key
     * @return object
     */
    public Map<String, String> hashGetAll(String key) {
        return this.getHashOpr().entries(key);
    }

    /**
     * Hash 查看哈希表 key 中，给定域 field是否存在
     *
     * @param key     key
     * @param hashKey hashKey
     * @return object
     */
    public Boolean hashHasKey(String key, String hashKey) {
        return this.getHashOpr().hasKey(key, hashKey);
    }

    /**
     * Hash 返回哈希表 key 中的所有域
     *
     * @param key key
     * @return object
     */
    public Set<String> hashKeys(String key) {
        return this.getHashOpr().keys(key);
    }

    /**
     * Hash 返回散列键 key 中，所有域的值
     *
     * @param key key
     * @return object
     */
    public List<String> hashVals(String key) {
        return this.getHashOpr().values(key);
    }

    /**
     * Hash 返回散列键 key中指定Field的域的值
     *
     * @param key      key
     * @param hashKeys hashKeys
     * @return object
     */
    public List<String> hashVals(String key, Collection<String> hashKeys) {
        return this.getHashOpr().multiGet(key, hashKeys);
    }

    /**
     * Hash 散列键 key的数量
     *
     * @param key key
     * @return object
     */
    public Long hashSize(String key) {
        return this.getHashOpr().size(key);
    }

    /**
     * Hash 删除散列键 key 中的一个或多个指定域，以及那些域的值。不存在的域将被忽略。命令返回被成功删除的域值对数量
     *
     * @param key      key
     * @param hashKeys hashKeys
     */
    public void hashDelete(String key, Object... hashKeys) {
        this.getHashOpr().delete(key, hashKeys);
    }

    /**
     * Hash 删除散列键 key的数组
     *
     * @param key      key
     * @param hashKeys hashKeys
     */
    public void hashRemove(String key, Object[] hashKeys) {
        this.getHashOpr().delete(key, hashKeys);
    }

    /**
     * Object转成JSON数据
     *
     * @param object object
     * @param <T>    object预期类型
     * @return object
     */
    public <T> String toJson(T object) {
        if (object == null) {
            return RedisConsts.STR_EMPTY;
        }
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return RedisConsts.STR_EMPTY;
        }
    }

    /**
     * JSON数据，转成Object
     *
     * @param json  json字符串
     * @param clazz 预期类型
     * @param <T>   泛型
     * @return object
     */
    public <T> T fromJson(String json, Class<T> clazz) {
        if (StrUtil.isBlank(json) || clazz == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            return null;
        }
    }

    /**
     * JSON数据，转成 List<Object>
     *
     * @param json  json
     * @param clazz clazz
     * @param <T>   list 泛型
     * @return object
     */
    public <T> List<T> fromJsonList(String json, Class<T> clazz) {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
        try {
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    /**
     * 将对象直接以json数据不设置过期时间的方式保存
     *
     * @param key    key 键
     * @param object object
     * @author cuijiaxu@zhichubao.com 2018-06-11 13:46
     */
    public <T> void objectSet(String key, T object) {
        this.strSet(key, this.toJson(object));
    }

    /**
     * 根据一个前缀来删除所有匹配的key
     *
     * @param keyPrefix Prefix 前缀
     * @return object 删除的数量
     */
    public int deleteKeysWithPrefix(String keyPrefix) {
        Set<String> keys = this.currentRestTemplate().keys(keyPrefix + '*');
        if (keys == null || keys.isEmpty()) {
            return 0;
        }
        this.currentRestTemplate().delete(keys);
        return keys.size();
    }

    /**
     * 根据表达式获取匹配的所有key
     *
     * @param pattern 表达式
     * @return object 匹配的所有key
     */
    public Set<String> keys(String pattern) {
        return this.currentRestTemplate().keys(pattern);
    }

    private ValueOperations<String, String> getValueOpr() {
        return this.currentRestTemplate().opsForValue();
    }

    private HashOperations<String, String, String> getHashOpr() {
        return this.currentRestTemplate().opsForHash();
    }

    private ListOperations<String, String> getListOpr() {
        return this.currentRestTemplate().opsForList();
    }

    private SetOperations<String, String> getSetOpr() {
        return this.currentRestTemplate().opsForSet();
    }

    private ZSetOperations<String, String> getzSetOpr() {
        return this.currentRestTemplate().opsForZSet();
    }

    private RedisTemplate<String, String> currentRestTemplate() {
        return this.redisTemplate;
    }
}
