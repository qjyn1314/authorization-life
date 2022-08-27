package com.authorization.start.util.json;

import com.authorization.start.util.json.convert.DateDeserializer;
import com.authorization.start.util.json.convert.DateSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 */
public class ObjectMappers {

    /**
     * @return 标准配置新objectMapper
     */
    public static ObjectMapper configMapper() {
        return configMapper(new ObjectMapper());
    }

    /**
     * 对传入objectMapper进行配置
     *
     * @param objectMapper 源objectMapper
     * @return 对源objectMapper进行配置
     */
    public static ObjectMapper configMapper(ObjectMapper objectMapper) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Date.class, new DateSerializer());
        javaTimeModule.addDeserializer(Date.class, new DateDeserializer());
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATE)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATE)));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATETIME)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(JsonDateUtil.DATETIME)));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
}
