package com.authorization.valid.start.util;

import cn.hutool.core.collection.CollUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.HibernateValidator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 手动验证对象的工具类
 *
 * @author wangjunming
 * @date 2022/12/22 18:03
 */
@Component
public class ValidUtil implements CommandLineRunner {

    private static volatile Validator validatorStatic = null;

    static {
        validatorStatic = Validation.byProvider(HibernateValidator.class).configure()
                // 快速失败模式 将fail_fast设置为true即可，如果想验证全部，则设置为false(默认)或者取消配置即可
                // 快速失败模式在校验过程中，当遇到第一个不满足条件的参数时就立即返回，不再继续后面参数的校验。否则会一次性校验所有参数，并返回所有不符合要求的错误信息
                .failFast(true).buildValidatorFactory().getValidator();
    }

    @Override
    public void run(String... args) throws Exception {
        if (validatorStatic != null) {
            return;
        }
        validatorStatic = Validation.byProvider(HibernateValidator.class).configure()
                // 快速失败模式 将fail_fast设置为true即可，如果想验证全部，则设置为false(默认)或者取消配置即可
                // 快速失败模式在校验过程中，当遇到第一个不满足条件的参数时就立即返回，不再继续后面参数的校验。否则会一次性校验所有参数，并返回所有不符合要求的错误信息
                .failFast(true).buildValidatorFactory().getValidator();
    }

    /**
     * 校验对象，如果错误则直接抛出异常。
     *
     * @param bean   Bean
     * @param groups 验证组
     * @param <T>    Bean 泛型
     */
    public static <T> void validateAndThrow(T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> validateRes = validate(validatorStatic, bean, groups);
        if (CollUtil.isEmpty(validateRes)) {
            return;
        }
        process(validateRes);
    }

    /**
     * 校验对象，如果错误则直接抛出异常。
     *
     * @param collection 集合
     * @param groups     验证组
     * @param <T>        Bean 泛型
     */
    public static <T> void validateAndThrow(Collection<T> collection, Class<?>... groups) {
        Map<Integer, Set<ConstraintViolation<T>>> validateRes = validate(validatorStatic, collection, groups);
        if (CollUtil.isEmpty(validateRes)) {
            return;
        }
        process(validateRes);
    }

    /**
     * 校验对象，如果错误则直接抛出异常。
     *
     * @param validator 验证器
     * @param bean      Bean
     * @param groups    验证组
     * @param <T>       Bean 泛型
     */
    private static <T> void validateAndThrow(Validator validator, T bean, Class<?>... groups) {
        Set<ConstraintViolation<T>> validateRes = validate(validator, bean, groups);
        if (CollUtil.isNotEmpty(validateRes)) {
            process(validateRes);
        }
    }

    /**
     * 校验对象，如果错误则直接抛出异常。
     *
     * @param validator  验证器
     * @param collection 集合
     * @param groups     验证组
     * @param <T>        Bean 泛型
     */
    private static <T> void validateAndThrow(Validator validator, Collection<T> collection, Class<?>... groups) {
        Map<Integer, Set<ConstraintViolation<T>>> validateRes = validate(validator, collection, groups);
        if (CollUtil.isNotEmpty(validateRes)) {
            process(validateRes);
        }
    }

    /**
     * 校验单个对象，并返回效验结果
     *
     * @param validator 验证器
     * @param bean      Bean
     * @param groups    验证组
     * @param <T>       Bean 泛型
     */
    private static <T> Set<ConstraintViolation<T>> validate(Validator validator, T bean, Class<?>... groups) {
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
     * @param validator  验证器
     * @param collection Bean集合
     * @param groups     验证组
     * @param <T>        Bean 泛型
     */
    private static <T> Map<Integer, Set<ConstraintViolation<T>>> validate(Validator validator, Collection<T> collection, Class<?>... groups) {
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

    private static <T> void process(Set<ConstraintViolation<T>> resultSet) {
        if (CollUtil.isEmpty(resultSet)) {
            return;
        }
        String error = resultSet.stream()
                .map(ValidUtil::errorMsg)
                .collect(Collectors.joining("\n"));
        throw new ValidException(error);
    }

    private static <T> void process(Map<Integer, Set<ConstraintViolation<T>>> resultMap) {
        if (CollUtil.isEmpty(resultMap)) {
            return;
        }
        String error = resultMap.entrySet().stream()
                .map(entry ->
                        "[index: " + entry.getKey()
                                + " , error: " + entry.getValue().stream().map(ValidUtil::errorMsg).collect(Collectors.joining("\n")) + "]")
                .collect(Collectors.joining("\n"));
        throw new ValidException(error);
    }

    private static <T> String errorMsg(ConstraintViolation<T> item) {

        return item.getMessage();

//        return item.getPropertyPath().toString() + " ";
//        return item.getPropertyPath().toString() + " " + MessageAccessor.message(item.getMessage()).desc() + "; ";
    }

}