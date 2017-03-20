package com.movision.fsearch.service;

import com.movision.fsearch.service.exception.DetailedServiceException;
import com.movision.fsearch.service.exception.ServiceException;
import com.movision.fsearch.service.exception.UnknownServiceException;
import com.movision.fsearch.utils.*;
import com.movision.utils.DateUtils;
import com.movision.utils.G;
import com.movision.utils.L;

import java.util.*;

/**
 * 搜索器
 *
 * @Author zhuangyuhao
 * @Date 2017/3/20 14:59
 */
public class Searcher {
    private static String URL = null;
    private static String KEY = null;
    private static String SECRET = null;

    static {
        //加载静态资源，
        //searchserver = {"url":"http://139.196.189.100:10010/search", "key":"fsearch", "secret": "81ac307f7d4547b787aed88f1dc509d6"}
        Map<?, ?> map = G.getConfig().getJSONObject("searchserver");
        URL = map.get("url").toString();
        KEY = map.get("key").toString();
        SECRET = map.get("secret").toString();
    }

    /**
     * 生成签名
     *
     * @param kv     map
     * @param secret
     * @return
     */
    private static String makeSign(Map<?, ?> kv, String secret) {
        Set<?> keySet = kv.keySet();
        List<String> keys = new ArrayList<>(keySet.size());
        for (Object key : keySet) {
            keys.add(key.toString());
        }
        Collections.sort(keys);

        String encodeString;
        {
            StringBuilder buf = new StringBuilder();
            for (String key : keys) {
                Object value = kv.get(key);
                buf.append(key);
                if (value != null) {
                    buf.append(value);
                }
            }
            buf.append(secret);
            encodeString = buf.toString();
        }
        String realSign;
        try {
            realSign = EncryptUtil.md5(encodeString);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode sign", e);
        }
        return realSign;
    }


    /**
     * HttpClient请求fsearch服务器，根据params获取搜索结果
     *
     * @param api
     * @param params
     * @return
     * @throws ServiceException
     */
    public static Object request(String api, Map<String, Object> params) throws ServiceException {
        HttpClient client = new HttpClient();
        //封装请求头
        Map<String, Object> headers = CollectionUtil.arrayAsMap("X-Search-Api", api, "X-Search-Key", KEY, "X-Search-Time", "nope");
        String sign = makeSign(headers, SECRET);
        //在请求头的X-Search-Sign中放入生成的sign
        headers.put("X-Search-Sign", sign);
        try {
            //发起post请求
            client.doPost(URL, params, headers);
        } catch (Exception e) {
            throw new UnknownServiceException(e);
        }
        //日志记录搜索的参数和返回结果
        if (L.isInfoEnabled()) {
            L.error("SEARCH params: " + params + ", result: " + client.getResponseBody());
        }
        //把返回值解析成map
        Map<String, Object> map = JSONUtil.parseAsMap(client.getResponseBody());
        if (map == null) {
            throw new UnknownServiceException("Bad result: " + client.getResponseBody());
        }
        //若有？异常处理
        Map<?, ?> errorAsMap = (Map<?, ?>) map.get("error");
        if (errorAsMap != null) {
            String msg = FormatUtil.parseString(errorAsMap.get("msg"));
            if (msg == null) {
                throw new UnknownServiceException("Bad result: " + client.getResponseBody());
            }
            throw new DetailedServiceException(msg);
        }
        //返回的结果，真正需要的结果
        Object result = map.get("result");
        if (result == null) {
            throw new UnknownServiceException("Bad result: " + client.getResponseBody());
        }
        return result;
    }

    public static Map<String, Object> wrapPhraseEqualQuery(Map<String, Map<String, Object>> query, String key, Object value) {
        return query.put(key, CollectionUtil.arrayAsMap("type", "phrase", "value", value));
    }

    /**
     * 封装key和value到 query 这个map中
     *
     * @param query 容器map
     * @param key   目标key
     * @param value 目标value
     * @return
     */
    public static Map<String, Object> wrapEqualQuery(Map<String, Map<String, Object>> query, String key, Object value) {

        return query.put(key, CollectionUtil.arrayAsMap("type", "equal", "value", value));
    }

    public static Map<String, Object> wrapDateRangeQuery(Map<String, Map<String, Object>> query, String key, Date start, Date end, boolean maxInclusive, boolean minInclusive, String resolution) {

        String sdate, edate;
        sdate = DateUtils.dateToString(start, resolution);
        long min = Long.valueOf(sdate);

        edate = DateUtils.dateToString(end, resolution);
        long max = Long.valueOf(edate);

        return query.put(key,
                CollectionUtil.arrayAsMap("type", "numberrange", "maxInclusive", maxInclusive ? "true" : "false", "minInclusive", minInclusive ? "true" : "fasle", "max", max, "min", min));
    }

    public static void main(String[] args) {
        Date d1 = new Date();
        Date d2 = new Date();

        Map<String, Map<String, Object>> query = new HashMap<>();
        Searcher.wrapDateRangeQuery(query, "startDate", d1, d2, true, true, "DAY");
        System.out.println(query);
    }
}
