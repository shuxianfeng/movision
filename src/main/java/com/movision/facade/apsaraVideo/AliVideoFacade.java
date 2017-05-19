package com.movision.facade.apsaraVideo;

import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.sun.deploy.net.URLEncoder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.dsig.SignatureMethod;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Author zhuangyuhao
 * @Date 2017/5/19 14:04
 */
@Service
public class AliVideoFacade {

    private static Logger log = LoggerFactory.getLogger(AliVideoFacade.class);

    private static final String AccessKeyId = PropertiesLoader.getValue("access.key.id");

    private static final String AccessKeySecret = PropertiesLoader.getValue("access.key.secret");

    private final String ALGORITHM = "HmacSHA1";

    private static final String SEPARATOR = "&";
    private static final String EQUAL = "=";
    private static final String HTTP_METHOD = "GET";
    private static final String ENCODE_TYPE = "UTF-8";
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    /**
     * 生成请求的url
     *
     * @param action  类似：GetVideoPlayAuth
     * @param videoid
     * @return
     * @throws IOException
     */
    public String generateRequestUrl(String action, String videoid) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        //以下Java示例代码演示了如何添加公共请求参数、如何构造用请求参数构造规范化请求字符串，以及如何构造stringToSign字符串
        Map<String, String> parameterMap = new HashMap<String, String>();
        // 加入请求公共参数
        parameterMap.put("Action", action);
        parameterMap.put("Version", "2017-03-21");  //API版本号，为日期形式：YYYY-MM-DD，本版本对应为2017-03-21
        parameterMap.put("AccessKeyId", AccessKeyId); //此处请替换成您自己的AccessKeyId
//        parameterMap.put("Timestamp", "2017-03-29T12:09:11Z");//此处将时间戳固定只是测试需要，这样此示例中生成的签名值就不会变，方便您对比验证，可变时间戳的生成需要用下边这句替换
        parameterMap.put("Timestamp", formatIso8601Date(new Date()));
        parameterMap.put("SignatureMethod", "HMAC-SHA1");   //签名方式，目前支持HMAC-SHA1
        parameterMap.put("SignatureVersion", "1.0");    //签名算法版本，目前版本是1.0
//        parameterMap.put("SignatureNonce", "578a50c1-280d-4a34-bffc-e06aa6b2df76");//此处将唯一随机数固定只是测试需要，这样此示例中生成的签名值就不会变，方便您对比验证，可变唯一随机数的生成需要用下边这句替换
        parameterMap.put("SignatureNonce", UUID.randomUUID().toString());
        parameterMap.put("Format", "JSON"); //返回值的类型，支持JSON与XML，默认为XML。
        // 加入方法特有参数
        parameterMap.put("VideoId", videoid);
        // 对参数进行排序
        List<String> sortedKeys = new ArrayList<String>(parameterMap.keySet());
        Collections.sort(sortedKeys);
        // 生成stringToSign字符
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(HTTP_METHOD).append(SEPARATOR);
        stringToSign.append(percentEncode("/")).append(SEPARATOR);
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            // 此处需要对key和value进行编码
            String value = parameterMap.get(key);
            canonicalizedQueryString.append(SEPARATOR).append(percentEncode(key)).append(EQUAL).append(percentEncode(value));
        }
        // 此处需要对canonicalizedQueryString进行编码
        stringToSign.append(percentEncode(canonicalizedQueryString.toString().substring(1)));
        SecretKey key = new SecretKeySpec((AccessKeySecret + SEPARATOR).getBytes(ENCODE_TYPE), SignatureMethod.HMAC_SHA1);
        Mac mac = Mac.getInstance(ALGORITHM);
        mac.init(key);
        String signature = URLEncoder.encode(new String(new Base64().encode(mac.doFinal(stringToSign.toString().getBytes(ENCODE_TYPE))),
                ENCODE_TYPE), ENCODE_TYPE);

        // 生成请求URL
        StringBuilder requestURL;
        requestURL = new StringBuilder("http://vod.cn-shanghai.aliyuncs.com?"); //VOD API 的服务接入地址
        requestURL.append(URLEncoder.encode("Signature", ENCODE_TYPE)).append("=").append(signature);
        for (Map.Entry<String, String> e : parameterMap.entrySet()) {
            requestURL.append("&").append(percentEncode(e.getKey())).append("=").append(percentEncode(e.getValue()));
        }
        return requestURL.toString();
    }

    /**
     * 生成符合规范的 Timestamp 字符串(适用于ali视频点播的API调用接口)
     *
     * @param date
     * @return
     */
    private static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /**
     * 生成规范化请求字符串（示例中的 canonicalizedQueryString 变量）
     *
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String percentEncode(String value) throws UnsupportedEncodingException {
        if (value == null) return null;
        return java.net.URLEncoder.encode(value, ENCODE_TYPE).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }


}
