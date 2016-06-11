package com.zhuhuibao.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

public class LocalTest {


    public void testContants() {
        CloseableHttpClient httpclient = HttpClients.custom()
                .build();
        try {
            String key = "awj7yd98wf6l2trsdsw0lo6zu4w66c3y";
            String str = "_input_charset=utf-8&out_trade_no=135&partner=2088102135237642&service=single_trade_query";

            String sign = DigestUtils.md5Hex(str + key);
            System.out.println(sign);
            HttpGet httpGet = new HttpGet("https://openapi.alipaydev.com/gateway.do?sign=" + sign + "&sign_type=MD5&" + str);

            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            System.out.println(EntityUtils.toString(response1.getEntity(), "UTF-8"));
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void testQuery() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("_input_charset", "utf-8");
        params.put("out_trade_no", "135");
        //params.put("trade_no", "131111115");
        params.put("service", "single_trade_query");

        String partnerkey = "awj7yd98wf6l2trsdsw0lo6zu4w66c3y";

        String result = this.request("https://openapi.alipaydev.com/gateway.do", partnerkey, "2088102135237642", params);

        System.out.println(result);
    }

    private String request(String url, String partnerkey, String partner, Map<String, String> params) {
        params.put("partner", partner);
        List<String> keySet = new ArrayList<String>(params.keySet());
        Collections.sort(keySet);
        StringBuffer sb = new StringBuffer();
        for (String key : keySet) {
            sb.append(key).append('=').append(params.get(key)).append('&');
        }
        sb.deleteCharAt(sb.length() - 1);
        String str = sb.toString();

        String sign = DigestUtils.md5Hex(str + partnerkey);

        HttpGet httpGet = new HttpGet(url + "?sign=" + sign + "&sign_type=MD5&" + str);

        CloseableHttpClient httpclient = HttpClients.custom()
                .build();
        CloseableHttpResponse response1;
        try {
            response1 = httpclient.execute(httpGet);
            return EntityUtils.toString(response1.getEntity(), "UTF-8");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
