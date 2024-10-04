package org.LinkedOn.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Json {

    public static class Views {
        public static class Brief {
        }

        public static class Full extends Brief {
        }
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    // Serialize object to JSON string with a specific view
    public static <T> String toJsonWithView(T object, Class<?> viewClass) throws JsonProcessingException {
        return mapper.writerWithView(viewClass).writeValueAsString(object);
    }

    // Serialize object to JSON string without any view
    public static <T> String toJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    // Deserialize JSON string to object with a specific view
    public static <T> T fromJsonWithView(String json, Class<T> clazz, Class<?> viewClass) throws JsonProcessingException {
        return mapper.readerWithView(viewClass).forType(clazz).readValue(json);
    }

    // Deserialize JSON string to object without any view
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }

    // Deserialize JSON from InputStream to object without any view
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        return mapper.readValue(inputStream, clazz);
    }

    // Serialize ArrayList to JSON string with a specific view
    public static <T> String toJsonArrayWithView(List<T> list, Class<?> viewClass) throws JsonProcessingException {
        return mapper.writerWithView(viewClass).writeValueAsString(list);
    }

    // Serialize ArrayList to JSON string without any view
    public static <T> String toJsonArray(List<T> list) throws JsonProcessingException {
        return mapper.writeValueAsString(list);
    }

    // Deserialize JSON string to ArrayList
    public static <T> List<T> fromJsonArray(String json, Class<T[]> clazz) throws JsonProcessingException {
        T[] array = mapper.readValue(json, clazz);
        List<T> list = new ArrayList<>();
        for (T item : array) {
            list.add(item);
        }
        return list;
    }
}
