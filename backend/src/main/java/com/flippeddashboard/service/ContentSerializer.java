package com.flippeddashboard.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

final class ContentSerializer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private ContentSerializer() {}

    @SuppressWarnings("unchecked")
    static Map<String, Object> parse(String content) {
        if (content == null) return new LinkedHashMap<>();
        try {
            return MAPPER.readValue(content, Map.class);
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    static String write(Map<String, Object> map) {
        try {
            return MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize content", e);
        }
    }
}
