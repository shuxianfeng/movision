package com.movision.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/21 11:23
 */
public class IpUtil {

    private static final Logger log = LoggerFactory.getLogger(IpUtil.class);


    /**
     * 获取请求的客户端的公网ip（本地localhost调试不可用）
     *
     * @param request
     * @return
     */
    public static String getRequestClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }



    /***
     * 调用淘宝ip库
     *
     * @param urlStr    请求的地址
     * @param content   请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding  服务器端请求编码。如GBK,UTF-8等
     * @return
     */
    private static String getResult(String urlStr, String content, String encoding) {

        URL url = null;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(60000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(60000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            out.flush();// 刷新
            out.close();// 关闭输出流
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
        return null;
    }

    public static String getCitycode(String content, String encoding) {
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        String returnStr = getResult(urlStr, content, encoding);
        if (returnStr != null) {
            log.debug("returnStr=" + returnStr);

            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                log.error("无效IP，局域网测试");
                return "0";
            }
            String citycode = (temp[8].split(":"))[1].replaceAll("\"", "");
            log.debug("citycode=" + citycode);
            return citycode;
        }
        return null;
    }

    public static void main(String[] args) {
//        doGet("http://ip.taobao.com/service/getIpInfo.php", map);
        String ip = "222.95.81.58";
        String city = getCitycode("ip=" + ip, "utf-8");
        log.debug("city=" + city);
    }

}
