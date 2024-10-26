package com.authorization.life.lov.start.lov.handler;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.authorization.life.lov.start.lov.anno.LovValue;
import com.authorization.life.lov.start.lov.entity.LovDetail;
import com.authorization.life.lov.start.lov.entity.LovValueDetail;
import com.authorization.life.lov.start.lov.exception.LovException;
import com.authorization.life.lov.start.lov.helper.LovUserHelper;
import com.authorization.life.lov.start.lov.service.LovService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.reflect.FieldUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * LovTranslator 默认实现
 */
@Slf4j
@Component
public class DefaultLovTranslator implements LovTranslator {

    /**
     * 获取返回CPU核数
     *
     * @return 返回CPU核数，默认为8
     */
    public static int getCpuProcessors() {
        return Runtime.getRuntime() != null && Runtime.getRuntime().availableProcessors() > 0 ?
                Runtime.getRuntime().availableProcessors() : 8;
    }

    private static final String DEFAULT_TARGET_FIELD_SUFFIX = "Content";
    private static final char SPLITOR = '.';
    private final LoadingCache<LocalCacheKey, LovDetail> lovCache;
    private final LoadingCache<LocalCacheKey, Map<String, String>> lovValueCache;

    public DefaultLovTranslator() {
        this.lovCache = CacheBuilder.newBuilder()
                // 同时写缓存的线程数为cpu线程数，默认为8
                .concurrencyLevel(getCpuProcessors())
                // 设置缓存容器的初始容量为100
                .initialCapacity(100)
                // 设置缓存最大容量为5000，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(5000)
                // 设置写缓存后5分钟过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 设置1分钟后刷新缓存
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                // 通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<LocalCacheKey, LovDetail>() {
                    @Override
                    public LovDetail load(@NonNull LocalCacheKey key) {
                        return lovService.selectLov(key.getTenantId(), key.getLovCode());
                    }
                });
        this.lovValueCache = CacheBuilder.newBuilder()
                // 同时写缓存的线程数为cpu线程数，默认为8
                .concurrencyLevel(getCpuProcessors())
                // 设置缓存容器的初始容量为100
                .initialCapacity(100)
                // 设置缓存最大容量为5000，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(5000)
                // 设置写缓存后5分钟过期
                .expireAfterWrite(5, TimeUnit.MINUTES)
                // 设置1分钟后刷新缓存
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                // 通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<LocalCacheKey, Map<String, String>>() {
                    @Override
                    public Map<String, String> load(@NonNull LocalCacheKey key) {
                        return lovService.selectLovValue(key.getTenantId(), key.getLovCode())
                                .stream().collect(Collectors.toMap(LovValueDetail::getValueCode,
                                        LovValueDetail::getValueContent));
                    }
                });
    }

    @Autowired
    private LovService lovService;
    @Autowired
    private LovUserHelper lovUserHelper;


    @Override
    public Object translateObject(String[] targetFields, Object object) {
        try {
            if (object == null) {
                // 返回值为null直接跳过
                return null;
            }
            log.debug("lov translate begin");
            log.debug("target fields is [{}]", Arrays.toString(targetFields));
            if (object instanceof Collection) {
                // 如果传入对象为集合, 处理其中的节点
                this.processCollection(targetFields, (Collection<?>) object);
            } else {
                // 默认视为对象进行处理
                this.processObject(targetFields, object);
            }
            log.debug("lov translate end");
        } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException | SecurityException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        return object;
    }

    @Override
    public LovDetail getLov(String lovCode) {
        try {
            return lovCache.get(LocalCacheKey
                    .of(Optional.ofNullable(lovUserHelper.getTenantId()).orElse(0L), lovCode));
        } catch (ExecutionException e) {
            return null;
        }
    }

    @Override
    public Map<String, String> getLovValue(String lovCode) {
        try {
            return lovValueCache.get(LocalCacheKey
                    .of(Optional.ofNullable(lovUserHelper.getTenantId()).orElse(0L), lovCode));
        } catch (ExecutionException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * 处理集合
     *
     * @param targetFields 待处理字段名称
     * @param collection   集合
     */
    protected void processCollection(String[] targetFields, Collection<?> collection) throws IllegalAccessException,
            NoSuchFieldException, ExecutionException {
        log.debug("process collection, target field is [{}]", Arrays.toString(targetFields));
        // 检查集合是否为空
        Optional<?> optional = Optional.ofNullable(collection)
                .map(list -> list.stream().filter(Objects::nonNull).findFirst());
        if (!optional.isPresent()) {
            log.debug("target collection is empty, skip...");
            return;
        }
        for (Object object : collection) {
            if (object == null) {
                continue;
            }
            this.processObject(targetFields, object);
        }
    }

    /**
     * 处理对象
     * @param targetFields 待处理字段名称
     * @param object 待处理对象
     */
    protected void processObject(String[] targetFields, Object object) throws IllegalAccessException,
            NoSuchFieldException, ExecutionException {
        log.debug("process by default method, target field is [{}]", Arrays.toString(targetFields));
        if (targetFields == null || targetFields.length == 0) {
            log.debug("target fields is empty and will process target Object itself");
            // 如果没有传入指定字段,则默认扫描传入对象本身
            this.processOne(object);
        } else {
            // 否则遍历指定字段
            for (String targetField : targetFields) {
                if (StrUtil.isBlank(targetField)) {
                    log.debug("target fields is empty and will process target Object itself");
                    // 如果自定字段中包含空字符串(""或null),则扫描传入对象本身
                    this.processOne(object);
                } else {
                    // 否则扫描指定字段所对应的对象
                    // 按.分隔, 分段递归解析
                    int indexOfSplitor = targetField.indexOf(SPLITOR);
                    Class<?> clazz = object.getClass();
                    Field field;
                    Object value;
                    if (indexOfSplitor > 0) {
                        log.debug("recursive process result.{}", targetField.substring(0, indexOfSplitor));
                        field = this.getField(clazz, targetField.substring(0, indexOfSplitor));
                        field.setAccessible(true);
                        value = field.get(object);
                        this.translateObject(new String[]{targetField.substring(indexOfSplitor + 1)}, value);
                    } else {
                        log.debug("recursive process result.{}", targetField);
                        field = this.getField(clazz, targetField);
                        field.setAccessible(true);
                        value = field.get(object);
                        this.translateObject(null, value);
                    }

                }
            }
        }
    }

    /**
     * 直接处理当前对象
     * @param obj 当前对象
     */
    private void processOne(Object obj) throws IllegalAccessException, ExecutionException {
        if (obj == null) {
            return;
        }
        // 声明各种变量
        Class<?> clazz = obj.getClass();
        Field[] fields = FieldUtils.getAllFields(clazz);
        Map<String, String> lovValueMap;
        String content;
        Field contentField;
        Object value;
        String lovCode;
        Long tenantId = Optional.ofNullable(lovUserHelper.getTenantId()).orElse(0L);
        log.debug("current tenantId is: [{}]", tenantId);
        // 循环处理对象中的所有字段
        for (Field field : fields) {
            // 找到class中所有被@LovValue注解的字段
            LovValue lovValueAnnotation = AnnotationUtils.getAnnotation(field, LovValue.class);
            if (lovValueAnnotation == null) {
                continue;
            }
            // 准备数据
            lovCode = lovValueAnnotation.lovCode();
            Assert.notBlank(lovCode,"zcb-client-tenant.error.lov-code-not-null");
            field.setAccessible(true);
            value = field.get(obj);
            if (value == null) {
                // 待翻译对象为null,跳过
                log.debug("field value [{}] is null, skip...", field.getName());
                continue;
            }
            LovDetail lov = lovCache.get(LocalCacheKey.of(tenantId, lovCode));
            if(lov == null || lov.getLovTypeCode() == null) {
                log.debug("invalid lov define [{}]", lovCode);
                // 无效的值集头
                content = null;
            }else {
                lovValueMap = lovValueCache.get(LocalCacheKey.of(tenantId, lovCode));
                content = valueCode2Content(lovValueMap, value, lov);
            }
            // 将content回写到对象中
            String contentFieldName = lovValueAnnotation.contentField();
            try {
                if (StrUtil.isBlank(contentFieldName)) {
                    log.debug("no content field name is assigned in field [{}], use default content field name", field.getName());
                    // 如果没有指定content字段名,则使用默认映射
                    contentField = this.getField(clazz, field.getName() + DEFAULT_TARGET_FIELD_SUFFIX);
                } else {
                    log.debug("using target field name [{}]", contentFieldName);
                    // 如果指定了有效的content字段名,则将content写入该字段中
                    contentField = this.getField(clazz, contentFieldName);
                }
                contentField.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException e) {
                // 如果指定了的content字段名无效,跳过执行
                log.warn("The content field [{}] not in [{}]", contentFieldName, clazz.getSimpleName());
                continue;
            }
            // 没有找到有效的content时, 如果@LovValue指定了默认值,则使用该默认值, 否则使用value的原值
            if(content == null) {
                log.warn("can not get any translate result by lov code [{}] and value [{}], do fallback process", lovCode, value);
                content = StrUtil.isBlank(lovValueAnnotation.defaultContent())
                        ? String.valueOf(value) : lovValueAnnotation.defaultContent();
            }
            log.debug("field lov value [{}] translate result is [{}]", lovCode, content);
            contentField.set(obj, content);
        }

    }

    /**
     * 值代码转换为值内容
     * @param lovValueMap 固定值集map
     * @param valueCode 值代码
     * @param lov 值集
     * @return 值内容
     */
    private String valueCode2Content(Map<String, String> lovValueMap, Object valueCode, LovDetail lov) {
        String content;
        if (!Objects.equals(lov.getLovTypeCode(), LovDetail.LovType.FIXED)){
            // 非法的值集类型
            throw new LovException("此处获取得到的不是固定值集，请检查代码。");
        }
        log.debug("translating FIXED lov values [{}]", lov.getLovCode());
        // 获取content
        if (MapUtil.isEmpty(lovValueMap)) {
            log.debug("lov values [{}] local cache is missing while translate, skip...", lov.getLovCode());
            content = null;
        } else {
            // 如果本地缓存中有该值集的有效缓存,则直接进行映射
            content = getContent(lovValueMap, valueCode);
        }
        return content;
    }

    /**
     * 获得给定类及其所有父类中指定的字段,检索公有/私有/保护字段
     *
     * @param clazz 给定类
     * @return 指定的字段
     */
    protected Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field[] fields = FieldUtils.getAllFields(clazz);
        for (Field field : fields) {
            if (Objects.equals(fieldName, field.getName())) {
                return field;
            }
        }
        throw new NoSuchFieldException(fieldName);
    }


    /**
     * 支持集合类映射
     * @param lovValueMap lovValue缓存map
     * @param value 待翻译的值
     * @return 翻译后的值
     */
    private String getContent(Map<String, String> lovValueMap, Object value){
        if(value instanceof Collection){
            StringBuilder builder = new StringBuilder("[");
            for (Object part : (Collection<?>) value){
                String partValue = String.valueOf(part);
                String partMeaning = lovValueMap.get(partValue);
                builder.append(",").append(partMeaning);
            }
            return builder.append("]").deleteCharAt(1).toString();
        }
        return lovValueMap.get(String.valueOf(value));
    }
}
