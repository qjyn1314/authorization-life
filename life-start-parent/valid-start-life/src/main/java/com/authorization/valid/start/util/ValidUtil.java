package com.authorization.valid.start.util;

import cn.hutool.core.collection.CollUtil;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 手动验证对象的工具类
 *
 * @author wangjunming
 * @date 2022/12/22 18:03
 */
public class ValidUtil {


    /**
     * 校验单个对象，并返回效验结果
     *
     * @param validator 验证器
     * @param bean    Bean
     * @param groups    验证组
     * @param <T>       Bean 泛型
     */
    public static <T> Set<ConstraintViolation<T>> validate(javax.validation.Validator validator, T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> violationSet;
        if (groups == null) {
            violationSet = validator.validate(bean);
        } else {
            violationSet = validator.validate(bean, groups);
        }
        return violationSet;
    }

    /**
     * 校验集合对象，并返回效验结果
     *
     * @param validator 验证器
     * @param collection Bean集合
     * @param groups    验证组
     * @param <T>       Bean 泛型
     */
    public static <T> Map<Integer, Set<ConstraintViolation<T>>> validate(javax.validation.Validator validator, Collection<T> collection, Class<?>... groups) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }
        int index = 0;
        Map<Integer, Set<ConstraintViolation<T>>> resultMap = new HashMap<>(collection.size());
        for (T obj : collection) {
            Set<ConstraintViolation<T>> violationSet;
            if (groups == null) {
                violationSet = validator.validate(obj);
            } else {
                violationSet = validator.validate(obj, groups);
            }
            if (!CollectionUtils.isEmpty(violationSet)) {
                resultMap.put(index, violationSet);
            }
            ++index;
        }
        return resultMap;
    }

    /**
     * 校验对象，如果错误则直接抛出异常。
     * @param validator 验证器
     * @param bean Bean
     * @param groups 验证组
     * @param <T> Bean 泛型
     */
    public static <T> void validateAndThrow(javax.validation.Validator validator, T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> validateRes = validate(validator, bean, groups);
        if (CollUtil.isNotEmpty(validateRes)) {
            process(validateRes);
        }
    }

    /**
     * 校验对象，如果错误则直接抛出异常。
     * @param validator 验证器
     * @param collection 集合
     * @param groups 验证组
     * @param <T> Bean 泛型
     */
    public static <T> void validateAndThrow(javax.validation.Validator validator, Collection<T> collection, Class<?>... groups) {
        Map<Integer, Set<ConstraintViolation<T>>> validateRes = validate(validator, collection, groups);
        if (CollUtil.isNotEmpty(validateRes)) {
            process(validateRes);
        }
    }

    private static <T> void process(Set<ConstraintViolation<T>> resultSet) {
        if (CollUtil.isEmpty(resultSet)){
            return;
        }
        String error = resultSet.stream()
                .map(ValidUtil::errorMsg)
                .collect(Collectors.joining("\n"));
        throw new ValidException(error);
    }

    private static <T> void process(Map<Integer, Set<ConstraintViolation<T>>> resultMap) {
        if (CollUtil.isEmpty(resultMap)){
            return;
        }
        String error = resultMap.entrySet().stream()
                .map(entry ->
                        "[index: " + entry.getKey()
                    + " , error: " +  entry.getValue().stream().map(ValidUtil::errorMsg).collect(Collectors.joining("\n")) + "]")
                .collect(Collectors.joining("\n"));
        throw new ValidException(error);
    }

    private static <T> String errorMsg(ConstraintViolation<T> item) {
        return item.getPropertyPath().toString() + " ";// + MessageAccessor.message(item.getMessage()).desc() + "; ";
    }

}