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

import com.zhuhuibao.mybatis.entity.JsonResult;

public class JsonUtils {

    private static final ObjectMapper om = new ObjectMapper();

    /**
     * json串转换为字符串 wangxiang2
     *
     * @param msg
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static Map<String, String> getMapFromJsonString(String msg)
            throws JsonParseException, JsonMappingException, IOException {
        return om.readValue(msg, new TypeReference<Map<String, String>>() {
        });
    }

    /**
     * map转换为json串 wangxiang2
     *
     * @param map
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public static String getJsonStringFromMap(Map<String, String> map) throws JsonGenerationException, JsonMappingException, IOException {
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
     * @param obj
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String getJsonStringFromObj(Object obj) throws JsonGenerationException, JsonMappingException, IOException
    {
    	return om.writeValueAsString(obj);
    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
    	JsonResult result = new JsonResult();
    	result.setCode(200);
    	result.setData("123");
    	System.out.println(om.writeValueAsString(result));
    }
    	
}
