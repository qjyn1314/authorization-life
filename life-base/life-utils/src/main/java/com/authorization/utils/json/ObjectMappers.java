package com.authorization.utils.json;

import com.authorization.utils.json.convert.DateDeserializer;
import com.authorization.utils.json.convert.DateSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 对objectMapper进行时间转换等配置改造
 * <p>
 * 相关设置参考：
 * <p>
 * https://blog.csdn.net/Seky_fei/article/details/109960178
 * <p>
 * https://www.jianshu.com/p/89f8040fe956
 */
public class ObjectMappers {

    /**
     * @return 标准配置新objectMapper
     */
    protected static ObjectMapper configMapper() {
        return configMapper(new ObjectMapper());
    }

    /**
     * 对传入objectMapper进行配置
     *
     * @param objectMapper 源objectMapper
     * @return 对源objectMapper进行配置
     */
    private static ObjectMapper configMapper(ObjectMapper objectMapper) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATE)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATE)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATETIME)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATETIME)));

        objectMapper.registerModule(javaTimeModule);
        // 设置时间格式
        objectMapper.setDateFormat(JsonDateUtil.newSimpleFormat(JsonDateUtil.DATETIME));
        // 默认开启，将Date类型序列化为数字时间戳(毫秒表示)。关闭后，序列化为文本表现形式(2019-10-23T01:58:58.308+0000)，若设置时间格式化。那么均输出格式化的时间类型。
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 关闭属性为 "" （空字符串）或者 为 NULL 进行序列化功能，默认是开启，此处需将设置为 JsonInclude.Include.NON_EMPTY。
        // 对于 Include 枚举类的说明参考： https://blog.csdn.net/qq_31960623/article/details/120438533
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // 忽略未知字段 DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES默认是true，此处需将设置为false。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 关闭空对象(没有任何字段)不让序列化功能
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 开启属性名没有双引号的非标准json字符串
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 开启属性名没有双引号的非标准json字符串
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 仅当WRITE_ENUMS_USING_INDEX为禁用时(默认禁用)，该配置生效
        // 默认关闭，枚举类型序列化方式，默认情况下使用Enum.name()。开启后，使用Enum.toString()。注：需重写Enum的toString方法;
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        // 默认关闭，即使用BigDecimal.toString()序列化。开启后，使用 BigDecimal.toPlainString 序列化，不输出科学计数法的值。
        objectMapper.configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        // 允许出现特殊字符和转义符
        objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        // 序列化结果格式化，美化输出
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        return objectMapper;
    }
}
