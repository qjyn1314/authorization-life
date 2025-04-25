package com.authorization.utils.converter;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean之间的转换,例如: dto->entity, entity->vo
 * <p>
 * 转换推荐工具类的比较:
 * <p>
 * <a href="https://www.cnblogs.com/javaguide/p/11861749.html">转换推荐工具类的比较</a>
 * <p>
 * 选用: orika
 * 使用文档参考:
 * <p>
 * <a href="https://www.baeldung.com/orika-mapping">orika使用文档参考</a>
 * <p>
 * 转换中需要使用反射:
 * // 参考: https://blog.csdn.net/FatalFlower/article/details/122589921
 * // 由于 JDK 8 中有关反射相关的功能自从 JDK 9 开始就已经被限制了，为了兼容原先的版本，需要在运行项目时添加 --add-opens java.base/java.lang=ALL-UNNAMED 选项来开启这种默认不被允许的行为。
 *
 * @author wangjunming
 * @date 2023/1/18 16:26
 */
public class BeanConverter {

    private static final MapperFactory MAPPER_FACTORY;
    private static final MapperFacade MAPPER_FACADE;

    private static final MapperFactory MAPPER_NULL_FACTORY;
    private static final MapperFacade MAPPER_NULL_FACADE;

    static {
        //默认的构建,包含空值的转换
        DefaultMapperFactory.MapperFactoryBuilder<DefaultMapperFactory, DefaultMapperFactory.Builder> builder
                = new DefaultMapperFactory.Builder();
        MAPPER_FACTORY = builder.build();
        MAPPER_FACADE = MAPPER_FACTORY.getMapperFacade();

        //非空转换,为空不转换
        DefaultMapperFactory.MapperFactoryBuilder<DefaultMapperFactory, DefaultMapperFactory.Builder> nullBuilder
                = new DefaultMapperFactory.Builder().mapNulls(false);
        MAPPER_NULL_FACTORY = nullBuilder.build();
        MAPPER_NULL_FACADE = MAPPER_NULL_FACTORY.getMapperFacade();
    }

    /**
     * 默认字段实例集合
     */
    private static final Map<String, MapperFacade> CACHE_MAPPER_FACADE_MAP = new ConcurrentHashMap<>();

    public static <S, T> T convert(S origin, Class<T> destClz) {
        if (origin == null || destClz == null) {
            return null;
        }
        return MAPPER_FACADE.map(origin, destClz);
    }

    public static <S, D> D convert(S origin, D dest) {
        if (origin == null || dest == null) {
            return null;
        }
        MAPPER_FACADE.map(origin, dest);
        return dest;
    }

    public static <S, T> T convert(S origin, Type<S> sourceType, Type<T> desType) {
        // 泛型转换
        if (origin == null || sourceType == null || desType == null) {
            return null;
        }
        return MAPPER_FACADE.map(origin, sourceType, desType);
    }

    public static <S, T> T convert(S origin, Class<T> destClz, Map<String, String> fieldsMap) {
        if (origin == null || destClz == null) {
            return null;
        }

        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return null;
        }

        MapperFacade mapperFacade = getMapperFacade(origin.getClass(), destClz,
                fieldsMap, null, null);
        return mapperFacade.map(origin, destClz);
    }

    public static <S, D> D convert(S origin, D dest, Map<String, String> fieldsMap) {
        if (origin == null || dest == null) {
            return null;
        }

        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return null;
        }

        MapperFacade mapperFacade = getMapperFacade(origin.getClass(), dest.getClass(),
                fieldsMap, null, null);
        mapperFacade.map(origin, dest);
        return dest;
    }

    public static <S, T> T convertSupportDateString(S origin, Class<T> destClz,
                                                    Map<String, String> customConverterFieldsMap) {
        if (origin == null || destClz == null) {
            return null;
        }

        if (customConverterFieldsMap == null || customConverterFieldsMap.isEmpty()) {
            return null;
        }

        // 注册自定义转换器
        MapperFacade mapperFacade = getMapperFacade(origin.getClass(), destClz, null, DatetimeStringConverter.NAME,
                customConverterFieldsMap);
        return mapperFacade.map(origin, destClz);
    }

    public static <S, T> T convertSupportDateString(S origin, Class<T> destClz, Map<String, String> fieldsMap,
                                                    Map<String, String> customConverterFieldsMap) {

        if (origin == null || destClz == null) {
            return null;
        }

        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return null;
        }

        if (customConverterFieldsMap == null || customConverterFieldsMap.isEmpty()) {
            return null;
        }

        // 注册自定义转换器
        MapperFacade mapperFacade = getMapperFacade(origin.getClass(), destClz, fieldsMap, DatetimeStringConverter.NAME,
                customConverterFieldsMap);
        return mapperFacade.map(origin, destClz);

    }

    public static <S, T> void convertSupportDateString(S origin, T dest, Map<String, String> fieldsMap,
                                                       Map<String, String> customConverterFieldsMap) {

        if (origin == null || dest == null) {
            return;
        }

        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return;
        }

        if (customConverterFieldsMap == null || customConverterFieldsMap.isEmpty()) {
            return;
        }

        // 注册自定义转换器
        MapperFacade mapperFacade = getMapperFacade(origin.getClass(), dest.getClass(),
                fieldsMap, DatetimeStringConverter.NAME,
                customConverterFieldsMap);
        mapperFacade.map(origin, dest);
    }

    public static <S, T> List<T> convert(List<S> originList, Class<T> destClz) {
        if (CollectionUtils.isEmpty(originList) || destClz == null) {
            return new ArrayList<>(0);
        }
        return MAPPER_FACADE.mapAsList(originList, destClz);
    }

    public static <S, T> List<T> convert(List<S> originList, Class<T> destClz, Map<String, String> fieldsMap) {
        if (CollectionUtils.isEmpty(originList) || destClz == null) {
            return new ArrayList<>(0);
        }

        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return new ArrayList<>(0);
        }

        MapperFacade mapperFacade = getMapperFacade(originList.get(0).getClass(), destClz,
                fieldsMap, null, null);
        return mapperFacade.mapAsList(originList, destClz);
    }

    public static <S, T> List<T> convertSupportDateString(
            List<S> originList, Class<T> destClz, Map<String, String> fieldsMap,
            Map<String, String> customConverterFieldsMap) {

        if (CollectionUtils.isEmpty(originList) || destClz == null) {
            return new ArrayList<>(0);
        }

        if (fieldsMap == null || fieldsMap.isEmpty()) {
            return new ArrayList<>(0);
        }

        if (customConverterFieldsMap == null || customConverterFieldsMap.isEmpty()) {
            return null;
        }

        // 注册自定义转换器
        MapperFacade mapperFacade = getMapperFacade(originList.get(0).getClass(), destClz, fieldsMap,
                DatetimeStringConverter.NAME, customConverterFieldsMap);
        return mapperFacade.mapAsList(originList, destClz);
    }

    public static <S, T> Set<T> convert(Set<S> originSet, Class<T> destClz) {
        if (CollectionUtils.isEmpty(originSet) || destClz == null) {
            return new HashSet<>(0);
        }
        return MAPPER_FACADE.mapAsSet(originSet, destClz);
    }

    public static <Sk, Sv, Dk, Dv> Map<Dk, Dv> convert(Map<Sk, Sv> originMap, Type<? extends Map<Dk, Dv>> destMapType) {
        if (CollectionUtils.isEmpty(originMap) || destMapType == null) {
            return new HashMap<>(0);
        }
        return MAPPER_FACADE.mapAsMap(originMap, new TypeBuilder<Map<Sk, Sv>>() {
        }.build(), destMapType);
    }

    private static <S, T> MapperFacade getMapperFacade(Class<S> sourceClass, Class<T> toClass,
                                                       Map<String, String> configMap, String customConverterName,
                                                       Map<String, String> customConverterFieldsMap) {
        String mapKey = toClass.getCanonicalName() + "->" + sourceClass.getCanonicalName();
        MapperFacade mapperFacade = CACHE_MAPPER_FACADE_MAP.get(mapKey);
        if (Objects.nonNull(mapperFacade)) {
            return mapperFacade;
        }
        MapperFactory factory = new DefaultMapperFactory.Builder().build();
        ClassMapBuilder<S, T> classMapBuilder = factory.classMap(sourceClass, toClass);
        if (configMap != null && !configMap.isEmpty()) {
            configMap.forEach(classMapBuilder::field);
        }
        if (!CollectionUtils.isEmpty(customConverterFieldsMap) && StringUtils.hasText(customConverterName)) {
            factory.getConverterFactory()
                    .registerConverter(customConverterName, DatetimeStringConverter.getInstance());

            customConverterFieldsMap.forEach((key, value) ->
                    classMapBuilder.fieldMap(key, value).converter(customConverterName).add());
        }
        classMapBuilder.byDefault().register();
        mapperFacade = factory.getMapperFacade();
        CACHE_MAPPER_FACADE_MAP.put(mapKey, mapperFacade);
        return mapperFacade;
    }

    public static <S, D> D convertWithoutNull(S origin, D dest) {
        if (origin == null || dest == null) {
            return null;
        }
        MAPPER_NULL_FACADE.map(origin, dest);
        return dest;
    }


}
