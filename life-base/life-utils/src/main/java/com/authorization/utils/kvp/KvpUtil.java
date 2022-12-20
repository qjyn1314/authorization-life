package com.authorization.utils.kvp;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * KV数据结构中常用的方法
 * <p>
 * Function<K,V>
 * K ->  入参
 * V ->  出参
 * <p>
 *
 * @author wangjunming
 * @since 2022/4/17 13:15
 */
public final class KvpUtil {

    private KvpUtil() {
        throw new UnsupportedOperationException("此处不允许创建对象");
    }

    /**
     * 两个数相加
     */
    public static final java.util.function.Function<BigDecimal, java.util.function.Function<BigDecimal, BigDecimal>> ADD = x -> x::add;
    /**
     * 两个数相减
     */
    public static final java.util.function.Function<BigDecimal, java.util.function.Function<BigDecimal, BigDecimal>> SUBTRACT = x -> x::subtract;
    /**
     * 两个数相乘
     */
    public static final java.util.function.Function<BigDecimal, java.util.function.Function<BigDecimal, BigDecimal>> MULTIPLY = x -> x::multiply;

    /**
     * 两个数相除，默认结果向上取整并默认保留两位小数,也可自定义保留的位数
     * <p>
     * 即，小学时代的四舍五入，此处包含五，也会进一位。
     *
     * @param divisor  除数
     * @param dividend 被除数
     * @param scale    小数位
     * @return 商
     */
    public static BigDecimal divideHalfUp(BigDecimal divisor, BigDecimal dividend, String scale) {
        Assert.notNull(divisor, "The divisor cannot be empty");
        Assert.notNull(dividend, "The dividend cannot be empty");
        Assert.isTrue(dividend.compareTo(BigDecimal.ZERO) > 0, "The dividend must be greater than zero");
        //配置自定义保留小数位
        Integer finalScale = KvpUtil.STR_TO_INT.apply(Kvp.ofVal(scale, 2));
        return divisor.divide(dividend, finalScale, RoundingMode.HALF_UP);
    }

    /**
     * 两个数相除，默认结果向上取整并保留两位小数
     * <p>
     * 即，小学时代的四舍五入，此处包含五，也会进一位。
     * <p>
     * Kvp<K,V>，即，K是除数，V是被除数。
     * <p>
     * 示例：
     * <p>
     * KvpUtil.DIVIDE_UP.apply(Kvp.of(new BigDecimal("10"),new BigDecimal("3"))) = 3.33
     */
    public static final java.util.function.Function<Kvp<BigDecimal, BigDecimal>, BigDecimal> DIVIDE_UP = x -> {
        BigDecimal key = x.getKey();
        BigDecimal value = x.getValue();
        return divideHalfUp(key, value, "2");
    };

    /**
     * 将 BigDecimal 数值向上取整，并可以自定义保留几位小数
     * <p>
     * Kvp<K,V>，即，K是被操作数值，V是 自定义保留小数位。
     * <p>
     * 示例：
     * <p>
     * KvpUtil.HALF_UP.apply(Kvp.of(new BigDecimal("5000.355"), 2)) = 5000.36
     */
    public static final java.util.function.Function<Kvp<BigDecimal, Integer>, BigDecimal> HALF_UP = decimal -> {
        BigDecimal key = decimal.getKey();
        Integer value = decimal.getValue();
        return key.setScale(value, RoundingMode.HALF_UP);
    };

    /**
     * String转换为Integer
     * <p>
     * 如果 key 为空时，则返回value，否则将 key转换为 value
     * <p>
     * Kvp<K,V>，即，K是String，V是Integer。
     * <p>
     * 示例：
     * <p>
     * KvpUtil.STR_TO_INT.apply(Kvp.ofVal(null, 2)) = 2
     * <p>
     * KvpUtil.STR_TO_INT.apply(Kvp.ofVal("", 2)) = 2
     * <p>
     * KvpUtil.STR_TO_INT.apply(Kvp.ofVal("3", 2)) = 3
     */
    public static final java.util.function.Function<Kvp<String, Integer>, Integer> STR_TO_INT = decimal -> {
        String key = decimal.getKey();
        Integer value = decimal.getValue();
        if (StrUtil.isBlank(key)) {
            return value;
        }
        return Integer.parseInt(key);
    };


}
