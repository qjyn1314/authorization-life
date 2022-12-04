package com.authorization.utils.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

/**
 * objectMapper 工具类
 * 若有更改objectMapper的需要，请在本工具类内配置，不要修改暴露出去的objectMapper,便于维护。
 */
public class JsonHelper {

    private static ObjectMapper objectMapper = ObjectMappers.configMapper();

    /**
     * 为ObjectMapper重新设置值
     */
    public static void setNewObjectMapper(ObjectMapper newObjectMapper) {
        objectMapper = newObjectMapper;
    }


    public static String writeValueAsString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T readValue(String content, Class<T> clazz) {
        try {
            return objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T readValue(String content, JavaType valueType) {
        try {
            return objectMapper.readValue(content, valueType);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    public static JsonNode readTree(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    public static TypeFactory getTypeFactory() {
        return objectMapper.getTypeFactory();
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
