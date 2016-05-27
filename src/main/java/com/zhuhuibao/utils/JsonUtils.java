package com.zhuhuibao.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JsonUtils {

    private static final ObjectMapper om = new ObjectMapper();

    /**
     * json串转换为字符串
     *
     * @param msg   string
     * @return
     * @throws IOException
     */
    public static Map<String, String> getMapFromJsonString(String msg)
            throws IOException {
        return om.readValue(msg, new TypeReference<Map<String, String>>() {
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
}
