package com.example.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;


public class JsonUtil {


    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final static ObjectMapper IGNORE_NULL_MAPPER = new ObjectMapper();

    static {
        IGNORE_NULL_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    /**
     * 转换为 JSON 字符串
     */
    public static String obj2json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException ignore) {
            throw new RuntimeException("Json 解析异常 %s".formatted(obj));
        }
    }

    /**
     * 转换为 JSON 字符串，忽略空值
     */
    public static String obj2jsonIgnoreNull(Object obj) {
        try {
            return IGNORE_NULL_MAPPER.writeValueAsString(obj);
        } catch (Exception ignore) {
            throw new RuntimeException("Json 解析异常 %s".formatted(obj));
        }
    }

    /**
     * 转换为 JavaBean
     */
    public static <T> T json2pojo(String jsonString, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json 反序列化异常: %s".formatted(jsonString), e);
        }
    }

    /**
     * 转换为 JavaBean (支持嵌套泛型，如 Result<User>)
     */
    public static <T> T json2pojo(String jsonString, Class<?> outer, Class<?>... inner) {
        try {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(outer, inner);
            return OBJECT_MAPPER.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new RuntimeException("Json 反序列化异常: %s".formatted(jsonString), e);
        }
    }

    /**
     * 字符串转换为 Map<String, Object>
     */
    public static <T> Map<String, Object> json2map(String jsonString) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Json 解析异常 %s".formatted(jsonString));
        }
    }

    /**
     * 字符串转换为 Map<String, T>
     */
    public static <T> Map<String, T> json2map(String jsonString, Class<T> clazz) {
        try {
            Map<String, Object> map = OBJECT_MAPPER.readValue(jsonString, new TypeReference<>() {});
            Map<String, T> result = new HashMap<>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                result.put(entry.getKey(), obj2pojo(entry.getValue(), clazz));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Json 反序列化为 Map 异常: %s".formatted(jsonString), e);
        }
    }

    /**
     * 深度转换 JSON 成 Map
     */
    public static Map<String, Object> json2mapDeeply(String json) throws Exception {
        return json2MapRecursion(json, OBJECT_MAPPER);
    }

    /**
     * 把 JSON 解析成 List，如果 List 内部的元素存在 jsonString，继续解析
     */
    private static List<Object> json2ListRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }
        List<Object> list = mapper.readValue(json, List.class);
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            if (obj instanceof String objStr) {
                String trimmed = objStr.trim();
                if ((trimmed.startsWith("[") || trimmed.startsWith("{"))) {
                    try {
                        JsonNode node = mapper.readTree(trimmed);
                        if (node.isArray()) {
                            list.set(i, json2ListRecursion(trimmed, mapper));
                        } else if (node.isObject()) {
                            list.set(i, json2MapRecursion(trimmed, mapper));
                        }
                    } catch (Exception ignore) {}
                }
            }
        }
        return list;
    }

    /**
     * 把 JSON 解析成 Map，如果 Map 内部的 Value 存在 jsonString，继续解析
     */
    private static Map<String, Object> json2MapRecursion(String json, ObjectMapper mapper) throws Exception {
        if (json == null) {
            return null;
        }
        Map<String, Object> map = mapper.readValue(json, new TypeReference<LinkedHashMap<String, Object>>() {});
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object obj = entry.getValue();
            if (obj instanceof String objStr) {
                String trimmed = objStr.trim();
                if ((trimmed.startsWith("[") || trimmed.startsWith("{"))) {
                    try {
                        JsonNode node = mapper.readTree(trimmed);
                        if (node.isArray()) {
                            map.put(entry.getKey(), json2ListRecursion(trimmed, mapper));
                        } else if (node.isObject()) {
                            map.put(entry.getKey(), json2MapRecursion(trimmed, mapper));
                        }
                    } catch (Exception ignore) {}
                }
            }
        }
        return map;
    }

    /**
     * 将 JSON 数组转换为集合
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) {
        JavaType javaType = getCollectionType(ArrayList.class, clazz);
        List<T> list = null;
        try {
            list = OBJECT_MAPPER.readValue(jsonArrayStr, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 解析异常 %s".formatted(jsonArrayStr));
        }
        return list;
    }

    /**
     * 获取泛型的 Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 将 Map 转换为 JavaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * 将 Map 转换为 JSON
     */
    public static String mapToJson(Map map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json 解析异常 %s".formatted(map));
        }
    }

    /**
     * 将 JSON 对象转换为 JavaBean
     */
    public static <T> T obj2pojo(Object obj, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(obj, clazz);
    }

    public static <T,R> List<T> list2List(List<R> list, Class<T> clazz){
        return list.stream()
                .map(it->obj2pojo(it,clazz))
                .collect(Collectors.toList());
    }

    /**
     * 将指定节点的 JSON 数据转换为 JavaBean
     */
    public static <T> T json2pojoByTree(String jsonString, String treeNode, Class<T> clazz) throws Exception {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonString);
        JsonNode data = jsonNode.findPath(treeNode);
        return json2pojo(data.toString(), clazz);
    }

    /**
     * 将指定节点的 JSON 数组转换为集合
     */
    public static <T> List<T> json2listByTree(String jsonStr, String treeNode, Class<T> clazz) throws Exception {
        JsonNode jsonNode = OBJECT_MAPPER.readTree(jsonStr);
        JsonNode data = jsonNode.findPath(treeNode);
        return json2list(data.toString(), clazz);
    }
}
