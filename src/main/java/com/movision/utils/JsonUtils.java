package com.movision.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final ObjectMapper om = new ObjectMapper();

    /**
     * json串转换为 Map<String, String>
     *
     * @param msg   string
     * @return
     * @throws IOException
     */
    public static Map<String, String> getStringMapFromJsonString(String msg)
            throws IOException {
        return om.readValue(msg, new TypeReference<Map<String, String>>() {
        });
    }

    /**
     *  json串转换为 Map<String, Object>
     * @param msg
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getObjectMapFromJsonString(String msg)
            throws IOException {
        return om.readValue(msg, new TypeReference<Map<String, Object>>() {
        });
    }

    /**
     * map转换为json串
     *
     * @param map   hashmap
     * @return
     * @throws IOException
     */
    public static String getJsonStringFromMap(Map<String, String> map) throws IOException {
        Map<String, String> paramMap = new HashMap<String, String>();
        List<String> keyList = new ArrayList<String>(map.keySet());
        for (String key : keyList) {
            if (map.get(key) != null) {
                paramMap.put(key, map.get(key));
            }
        }
        return om.writeValueAsString(paramMap);
    }
    
    /**
     * 对象转换成json字符串
     * @param obj  object
     * @return
     * @throws IOException
     */
    public static String getJsonStringFromObj(Object obj) throws IOException
    {
    	return om.writeValueAsString(obj);
    }

    public static void main(String[] args) throws IOException {
        String a = "{\"code\":200,\"info\":{\"token\":\"a967478ef49bd18cfaa369dec8b6a74f\",\"accid\":\"test_create_user\",\"name\":\"\"}}";
        System.out.println(getObjectMapFromJsonString(a));
    }
}
