package org.linkedon.client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Json {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    // Serialize object to JSON string
    public static <T> String toJson(T object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }


    // Deserialize JSON string to object
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }

    // Deserialize JSON from InputStream to object without
    public static <T> T fromJson(InputStream inputStream, Class<T> clazz) throws IOException {
        return mapper.readValue(inputStream, clazz);
    }

    // Serialize ArrayList to JSON string
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
