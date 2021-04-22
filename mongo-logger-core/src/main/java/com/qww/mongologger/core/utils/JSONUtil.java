package com.qww.mongologger.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

public class JSONUtil {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JSONUtil.class);
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        // parse的java对象不存在某属性时不报错
        // mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JSONUtil() {}

    /**
     * Serialize any Java value as a String.
     */
    public static String stringify(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // log.error("转JSON字符串失败");
        }
        return object.toString();
    }

    /**
     * Deserialize JSON content from given JSON content String.
     */
    public static <T> T parse(String content, Class<T> valueType) throws IOException {
        return mapper.readValue(content, valueType);
    }

    public static <T> List<?> parseList(String content) throws IOException {
        return parse(content, List.class);
    }

    public static JsonNode parse(String content) throws IOException {
        return mapper.readTree(content);
    }
}
