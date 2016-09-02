package com.zhuhuibao.fsearch.service;

import com.zhuhuibao.fsearch.service.exception.DetailedServiceException;
import com.zhuhuibao.fsearch.service.exception.ServiceException;
import com.zhuhuibao.fsearch.service.exception.UnknownServiceException;
import com.zhuhuibao.fsearch.utils.*;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.G;
import com.zhuhuibao.utils.L;

import java.util.*;

public class Searcher {
    private static String URL = null;
    private static String KEY = null;
    private static String SECRET = null;

    static {
        Map<?, ?> map = G.getConfig().getJSONObject("searchserver");
        URL = map.get("url").toString();
        KEY = map.get("key").toString();
        SECRET = map.get("secret").toString();
    }

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

    public static Object request(String api, Map<String, Object> params)
            throws ServiceException {
        HttpClient client = new HttpClient();
        Map<String, Object> headers = CollectionUtil.arrayAsMap("X-Search-Api",
                api, "X-Search-Key", KEY, "X-Search-Time", "nope");
        String sign = makeSign(headers, SECRET);
        headers.put("X-Search-Sign", sign);
        try {
            client.doPost(URL, params, headers);
        } catch (Exception e) {
            throw new UnknownServiceException(e);
        }
        if (L.isInfoEnabled()) {
            L.error("SEARCH params: " + params + ", result: "
                    + client.getResponseBody());
        }
        Map<String, Object> map = JSONUtil.parseAsMap(client.getResponseBody());
        if (map == null) {
            throw new UnknownServiceException("Bad result: "
                    + client.getResponseBody());
        }
        Map<?, ?> errorAsMap = (Map<?, ?>) map.get("error");
        if (errorAsMap != null) {
            String msg = FormatUtil.parseString(errorAsMap.get("msg"));
            if (msg == null) {
                throw new UnknownServiceException("Bad result: "
                        + client.getResponseBody());
            }
            throw new DetailedServiceException(msg);
        }
        Object result = map.get("result");
        if (result == null) {
            throw new UnknownServiceException("Bad result: "
                    + client.getResponseBody());
        }
        return result;
    }

    public static Map<String, Object> wrapEqualQuery(
            Map<String, Map<String, Object>> query, String key, Object value) {
        return query.put(key,
                CollectionUtil.arrayAsMap("type", "equal", "value", value));
    }

    public static Map<String, Object> wrapDateRangeQuery(
            Map<String, Map<String, Object>> query, String key,
            Date start, Date end, boolean maxInclusive, boolean minInclusive, String resolution) {

        String sdate, edate;
        sdate = DateUtils.dateToString(start, resolution);
        long min = Long.valueOf(sdate);

        edate = DateUtils.dateToString(end, resolution);
        long max = Long.valueOf(edate);

        return query.put(key,
                CollectionUtil.arrayAsMap(
                        "type", "numberrange",
                        "maxInclusive", maxInclusive ? "true" : "false",
                        "minInclusive", minInclusive ? "true" : "fasle",
                        "max", max,
                        "min", min));
    }

    public static void main(String[] args) {
        Date d1 = new Date();
        Date d2 = new Date();

        Map<String, Map<String, Object>> query = new HashMap<>();
        Searcher.wrapDateRangeQuery(query,"startDate",d1,d2,true,true,"DAY");
        System.out.println(query);
    }
}
