package com.authorization.utils.kvp;

import org.springframework.util.Assert;

/**
 * <p>
 * 增强键值对数据结构
 * </p>
 *
 * @param <K> 泛型key-键
 * @param <V> 泛型value-值
 * @author wangjunming
 * @since 2022/4/17 13:15
 */
public final class Kvp<K, V> implements java.util.function.Supplier<Kvp<K, V>>, java.io.Serializable {

    private final K key;
    private final V value;

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    /**
     * key 不可以为空
     * value 不可以为空
     *
     * @param key   the input key
     * @param value the input value
     * @param <K>   key 的泛型
     * @param <V>   value 的泛型
     * @return KeyValue<K, V>
     */
    public static <K, V> Kvp<K, V> of(K key, V value) {
        Assert.notNull(key, "key cannot be null");
        Assert.notNull(value, "value cannot be null");
        if (key instanceof String) {
            Assert.hasText((String) key, "key cannot be null");
        }
        if (value instanceof String) {
            Assert.hasText((String) value, "value cannot be null");
        }
        return new Kvp<>(key, value);
    }

    /**
     * key 不可以为空
     * value 可以为空
     *
     * @param key   the input key
     * @param value the input value
     * @param <K>   key 的泛型
     * @param <V>   value 的泛型
     * @return KeyValue<K, V>
     */
    public static <K, V> Kvp<K, V> ofKey(K key, V value) {
        Assert.notNull(key, "value cannot be null");
        if (key instanceof String) {
            Assert.hasText((String) key, "value cannot be null");
        }
        return new Kvp<>(key, value);
    }

    /**
     * key 可以为空
     * value 不可以为空
     *
     * @param key   the input key
     * @param value the input value
     * @param <K>   key 的泛型
     * @param <V>   value 的泛型
     * @return KeyValue<K, V>
     */
    public static <K, V> Kvp<K, V> ofVal(K key, V value) {
        Assert.notNull(value, "value cannot be null");
        if (value instanceof String) {
            Assert.hasText((String) value, "value cannot be null");
        }
        return new Kvp<>(key, value);
    }

    /**
     * value 不可以为空
     *
     * @param value the input value
     * @param <K>   key 的泛型
     * @param <V>   value 的泛型
     * @return KeyValue<K, V>
     */
    public static <K, V> Kvp<K, V> onlyVal(V value) {
        Assert.notNull(value, "value cannot be null");
        if (value instanceof String) {
            Assert.hasText((String) value, "value cannot be null");
        }
        return new Kvp<>(null, value);
    }

    private Kvp(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public Kvp<K, V> get() {
        return this;
    }

    /**
     * 自定义判断
     *
     * @param predicate 判断函数
     * @return boolean
     */
    public boolean valid(java.util.function.Predicate<Kvp<K, V>> predicate) {
        return predicate.test(this);
    }


}
