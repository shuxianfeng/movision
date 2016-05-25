package com.zhuhuibao.utils;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zhuhuibao.alipay.util.PayVerify;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);


    public static Map<String, String> sendRequest(String url,
                                                  Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(
                        Integer.valueOf(PayVerify
                                .getPropertyValue("socketTimeout")))
                .setConnectTimeout(
                        Integer.valueOf(PayVerify
                                .getPropertyValue("connectTimeout")))
                .setConnectionRequestTimeout(
                        Integer.valueOf(PayVerify
                                .getPropertyValue("connectionRequestTimeout")))
                .build();
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<String> keyList = new ArrayList<String>(params.keySet());
            for (String key : keyList) {
                uriBuilder.setParameter(key, params.get(key));
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            try {
                logger.info("statusCode="
                        + response.getStatusLine().getStatusCode());
                HttpEntity entity = response.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String content = EntityUtils.toString(entity);
                logger.info("content=" + content);
                Map<String, String> resultMap = JsonUtils
                        .getMapFromJsonString(content);
                resultMap.put("statusCode", String.valueOf(response
                        .getStatusLine().getStatusCode()));
                EntityUtils.consume(entity);
                return resultMap;
            } finally {
                response.close();
            }

        } catch (ConnectTimeoutException e) {
            logger.error("请求连接超时：" + e.getMessage());
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            logger.error("返回超时：" + e.getMessage());
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
        return null;
    }

    public static Map<String, String> sendRequest1(String url,
                                                   Map<String, String> params) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setSocketTimeout(
                        Integer.valueOf(PayVerify
                                .getPropertyValue("socketTimeout")))
                .setConnectTimeout(
                        Integer.valueOf(PayVerify
                                .getPropertyValue("connectTimeout")))
                .setConnectionRequestTimeout(
                        Integer.valueOf(PayVerify
                                .getPropertyValue("connectionRequestTimeout")))
                .build();
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<String> keyList = new ArrayList<String>(params.keySet());
            for (String key : keyList) {
                uriBuilder.setParameter(key, params.get(key));
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpGet);

            try {
                logger.info("statusCode="
                        + response.getStatusLine().getStatusCode());
                HttpEntity entity = response.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String content = EntityUtils.toString(entity);
                logger.info("content=" + content);
                Map<String, String> resultMap = (Map<String, String>) JSON.parse(content);
                resultMap.put("statusCode", String.valueOf(response
                        .getStatusLine().getStatusCode()));
                EntityUtils.consume(entity);
                return resultMap;
            } finally {
                response.close();
            }

        } catch (ConnectTimeoutException e) {
            logger.error("请求连接超时：" + e.getMessage());
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            logger.error("返回超时：" + e.getMessage());
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
        return null;
    }


    /**
     * HTTP Get 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static Map<String, String> doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            logger.error("请求地址为空！");
            return null;
        }
        Map<String, String> rspMap = new HashMap<String, String>();
        CloseableHttpClient httpClient;
        try {

            RequestConfig config = RequestConfig
                    .custom()
                    .setConnectTimeout(60000)
                    .setSocketTimeout(15000)
                    .build();

            httpClient = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(config)
                    .build();

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                rspMap.put("status", String.valueOf(statusCode));
                rspMap.put("result", "HTTP GET ERROR! " + String.valueOf(statusCode));
                return rspMap;
//                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();

            rspMap.put("status", String.valueOf(statusCode));
            rspMap.put("result", result);

            return rspMap;

        } catch (Exception e) {
            logger.error("HTTP GET 请求异常：" + e);
            rspMap.put("status", String.valueOf(500));
            rspMap.put("result", "HTTP GET ERROR! " + e.getMessage());
            return rspMap;
        }

    }

    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static Map<String, String> doPost(String url, Map<String, String> params, String charset) {
        if (StringUtils.isBlank(url)) {
            logger.error("请求地址为空！");
            return null;
        }
        Map<String, String> rspMap = new HashMap<String, String>();
        CloseableHttpClient httpClient;
        try {
            RequestConfig config = RequestConfig
                    .custom()
                    .setConnectTimeout(60000)
                    .setSocketTimeout(15000)
                    .build();

            httpClient = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(config)
                    .build();

            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                rspMap.put("status", String.valueOf(statusCode));
                rspMap.put("result", "HTTP POST ERROR! ");
                return rspMap;
//                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();

            rspMap.put("status", String.valueOf(statusCode));
            rspMap.put("result", result);

            return rspMap;
        } catch (Exception e) {
            logger.error("HTTP POST 请求异常：" + e);
            rspMap.put("status", String.valueOf(500));
            rspMap.put("result", "HTTP POST ERROR! " + e.getMessage());
            return rspMap;
        }
    }

}
