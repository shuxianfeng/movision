package com.movision.utils.baidu;

import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/4 15:42
 * 工具方法：根据地址计算当前输入的地址的经纬度
 */
public class SnCal {

    private static Logger log = LoggerFactory.getLogger(SnCal.class);
    /**
     * 秘钥
     */
    private static final String BAIDU_MAP_API_SK = PropertiesLoader.getValue("baidu.sk");

    public static String getSn(Map paramsMap) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {
        SnCal snCal = new SnCal();

        // 调用下面的toQueryString方法，对LinkedHashMap内所有value作utf8编码，拼接返回结果address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourak
        String paramsStr = snCal.toQueryString(paramsMap);

        // 对paramsStr前面拼接上/geocoder/v2/?，后面直接拼接yoursk得到/geocoder/v2/?address=%E7%99%BE%E5%BA%A6%E5%A4%A7%E5%8E%A6&output=json&ak=yourakyoursk
        String wholeStr = new String("/geocoder/v2/?" + paramsStr + BAIDU_MAP_API_SK);
        log.debug("----------wholeStr----------:" + wholeStr);

        // 对上面wholeStr再作utf8编码
        String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

        // 调用下面的MD5方法得到最后的sn签名7de5a22212ffaa9e326444c75a58f9a0
        return snCal.MD5(tempStr);
    }

    // 对Map内所有value作utf8编码，拼接返回结果

    /**
     * 不过，官网中的示例并不能处理一种参数形式，那就是参数中不只有中文，还有其他特殊字符，
     * 比如：http://api.map.baidu.com/geocode ... &location=39.983424,116.322987&output=json&pois=1
     * 其中location的参数值“39.983424,116.322987”包含了特殊字符“，”，而这个特殊字符如果与中文一起编码就会导致sn计算错误。
     * 因此在进行sn计算时，应该区分中文和特殊字符，只对中文进行UTF-8编码。
     * 例如下面的代码修改了toQueryString()函数，增加了对“，”的处理：
     * <p>
     * 以上示例只是针对location参数的处理，开发者应根据自己的情况进行编码，将特殊字符与参数值分开处理。
     * <p>
     * 特殊字符为下面{}中出现的字符：
     * {/:=&?#+!$,;'@()*[]}
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {

            queryString.append(pair.getKey() + "=");
            //增加了对“，”的处理：
            String ss[] = pair.getValue().toString().split(",");
            if (ss.length > 1) {
                for (String s : ss) {
                    queryString.append(URLEncoder.encode(s, "UTF-8") + ",");
                }
                queryString.deleteCharAt(queryString.length() - 1);
                queryString.append("&");
            } else {
                queryString.append(URLEncoder.encode((String) pair.getValue(),
                        "UTF-8") + "&");
            }


        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }


    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
