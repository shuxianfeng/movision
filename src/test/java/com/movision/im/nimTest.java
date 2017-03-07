package com.movision.im;

import com.movision.common.constant.ImConstant;
import com.movision.test.SpringTestCase;
import com.movision.utils.SignUtil;
import com.movision.utils.im.CheckSumBuilder;
import org.junit.Test;
import ytx.org.apache.http.HttpResponse;
import ytx.org.apache.http.NameValuePair;
import ytx.org.apache.http.client.entity.UrlEncodedFormEntity;
import ytx.org.apache.http.client.methods.HttpPost;
import ytx.org.apache.http.impl.client.DefaultHttpClient;
import ytx.org.apache.http.message.BasicNameValuePair;
import ytx.org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/6 14:22
 */
public class nimTest extends SpringTestCase {

    @Test
    public void testIm() throws Exception {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.netease.im/nimserver/user/create.action";
        HttpPost httpPost = new HttpPost(url);

        String appKey = ImConstant.APP_KEY;
        String appSecret = ImConstant.APP_SECRET;
        String nonce = SignUtil.generateString(32);
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        //创建一个accid=test_create_user的用户
        nvps.add(new BasicNameValuePair("accid", "test_create_user"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);

        // 打印执行结果
        // {"code":200,"info":{"token":"a967478ef49bd18cfaa369dec8b6a74f","accid":"test_create_user","name":""}}
        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
    }
}
