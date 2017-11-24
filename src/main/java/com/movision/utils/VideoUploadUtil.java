package com.movision.utils;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.SimpleTimeZone;
import com.movision.mybatis.dinyuehao.entity.Dinyuehao;
import com.movision.mybatis.dinyuehao.service.DinyuehaoService;
import com.movision.mybatis.fuwuhao.entity.Fuwuhao;
import com.movision.mybatis.fuwuhao.service.FuwuhaoService;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import com.movision.utils.redis.RedisClient;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.dsig.SignatureMethod;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.util.*;


/**
 * @Author zhanglei
 * @Date 2017/5/22 11:44
 */
@Service
public class VideoUploadUtil {

    private static final Logger log = LoggerFactory.getLogger(VideoUploadUtil.class);
    //正式
    public static String accessKeyId = PropertiesLoader.getValue("access.key.id");
    public static String accessKeySecret = PropertiesLoader.getValue("access.key.secret");
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private DinyuehaoService dinyuehaoService;

    @Autowired
    private FuwuhaoService fuwuhaoService;
    /** @Autowired
    private WeixinGuangzhuService weixinGuangzhuService;
    @Autowired private WeixinListService weixinListService;*/
    public String videoUpload(String fileName, String title, String description, String coverimg, String tatges) {

        //fileName为上传文件所在的绝对路径(必须包含扩展名)
        //String fileName = "/*/*/文件名称.mp4";
        //String title = "视频标题";
        //构造上传请求实例
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
        //视频分类ID
        request.setCateId(0);
        //视频标签,多个用逗号分隔
        request.setTags(tatges);
        //视频自定义封面URL
        request.setCoverURL(coverimg);
        //设置上传完成后的回调URL
        // request.setCallback("http://www.mofo.shop/movision/app/post/update_post_isdel");
        //可指定分片上传时每个分片的大小，默认为10M字节
        request.setPartSize(10 * 1024 * 1024L);
        //可指定分片上传时的并发线程数，默认为1 (注: 该配置会占用服务器CPU资源，需根据服务器情况指定）
        request.setTaskNum(1);
        request.setDescription(description);
        //设置是否使用水印
        request.setIsShowWaterMark(true);
        String videoid = "";
        try {
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadVideoResponse response = uploader.uploadVideo(request);
            videoid = response.getVideoId();
            //上传成功后返回视频ID
            System.out.print("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++--");
            System.out.print(response.getVideoId());
            System.out.print("+++++++++++++=================+++++++++++====================++++++++++++++++++");
        } catch (Exception e) {
            System.out.println("UploadVideoRequest Server Exception:");
            e.printStackTrace();
            System.out.print(e.getCause());
            System.out.print(e.getMessage());
        }


        return videoid;
    }


    /**
     * 获取视频上传凭证和地址
     *
     * @param client
     * @param filename
     * @param description
     * @param targes
     * @param title
     * @return
     */
    public static Map createUploadVideo(DefaultAcsClient client, String filename, String description, String targes, String title, long filesize) {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        CreateUploadVideoResponse response = null;
        Map map = new HashMap();
        try {
            /*必选，视频源文件名称（必须带后缀, 支持 "3GP","AVI","FLV","MP4","M3U8","MPG","ASF","WMV","MKV","MOV","TS",    "WebM","MPEG","RM","RMVB","DAT","ASX","WVX","MPE","MPA","F4V","MTS","VOB","GIF"）*/
            int index = filename.lastIndexOf(".") + 1;
            String subFile = filename.substring(index);
            if (subFile.equalsIgnoreCase("3GP") || subFile.equalsIgnoreCase("AVI") || subFile.equalsIgnoreCase("FLV") || subFile.equalsIgnoreCase("MP4") || subFile.equalsIgnoreCase("M3U8") || subFile.equalsIgnoreCase("MPG") || subFile.equalsIgnoreCase("ASF") || subFile.equalsIgnoreCase("ASX")
                    || subFile.equalsIgnoreCase("WMV") || subFile.equalsIgnoreCase("MKV") || subFile.equalsIgnoreCase("MOV") || subFile.equalsIgnoreCase("TS") || subFile.equalsIgnoreCase("WebM")
                    || subFile.equalsIgnoreCase("DAT") || subFile.equalsIgnoreCase("RMVB") || subFile.equalsIgnoreCase("RM") || subFile.equalsIgnoreCase("MPEG") || subFile.equalsIgnoreCase("WVX") || subFile.equalsIgnoreCase("MPE") || subFile.equalsIgnoreCase("VOB") || subFile.equalsIgnoreCase("GIF")
                    || subFile.equalsIgnoreCase("MPA") || subFile.equalsIgnoreCase("F4V") || subFile.equalsIgnoreCase("MTS")) {
                request.setFileName(filename);
                //必选，视频源文件字节数
                request.setFileSize(filesize);
                //必选，视频标题
                request.setTitle(title);
                //可选，分类ID
                request.setCateId(0);
                //可选，视频标签，多个用逗号分隔
                request.setTags(targes);
                //可选，视频描述
                request.setDescription(description);
                //可选，视频上传所在区域IP
                //request.setIP("127.0.0.1");
                response = client.getAcsResponse(request);
                String videoid = response.getVideoId();
                String UploadAuth = response.getUploadAuth();
                String UploadAddress = response.getUploadAddress();
                map.put("videoid", videoid);
                map.put("UploadAuth", UploadAuth);
                map.put("UploadAddress", UploadAddress);
                map.put("accessKeyId", VideoUploadUtil.accessKeyId);
                map.put("accessKeySecret", VideoUploadUtil.accessKeySecret);
                log.debug("RequestId:" + response.getRequestId());
                log.debug("UploadAuth:" + response.getUploadAuth());
                log.debug("UploadAddress:" + response.getUploadAddress());
            } else {
                map.put("error", 0);
            }
        } catch (ServerException e) {
            log.error("CreateUploadVideoRequest Server Exception:", e);
            e.printStackTrace();
        } catch (ClientException e) {
            log.error("CreateUploadVideoRequest Client Exception:", e);
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 刷新视频上传凭证
     *
     * @param client
     * @param videoId
     */
    public static void refreshUploadVideo(DefaultAcsClient client, String videoId) {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        RefreshUploadVideoResponse response = null;
        try {
            request.setVideoId(videoId);
            response = client.getAcsResponse(request);
        } catch (ServerException e) {
            System.out.println("RefreshUploadVideoRequest Server Exception:");
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("RefreshUploadVideoRequest Client Exception:");
            e.printStackTrace();
        }
        System.out.println("RequestId:" + response.getRequestId());
        System.out.println("UploadAuth:" + response.getUploadAuth());
    }


    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private static String formatIso8601Date(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    private static final String ENCODE_TYPE = "UTF-8";

    private static String percentEncode(String value) throws UnsupportedEncodingException {
        if (value == null) return null;
        return URLEncoder.encode(value, ENCODE_TYPE).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

    /**
     * 删除视频
     *
     * @param vid
     * @return
     */
    public static String deleteVideo(String vid) {
        String result = "";
        BufferedReader in = null;
        try {
            final String HTTP_METHOD = "GET";
            Map<String, String> parameterMap = new HashMap<String, String>();
            // 加入请求公共参数
            parameterMap.put("Action", "DeleteVideo");
            parameterMap.put("Version", "2017-03-21");
            parameterMap.put("AccessKeyId", accessKeyId); //此处请替换成您自己的AccessKeyId
            parameterMap.put("Timestamp", formatIso8601Date(new Date()));//此处将时间戳固定只是测试需要，这样此示例中生成的签名值就不会变，方便您对比验证，可变时间戳的生成需要用下边这句替换
            parameterMap.put("SignatureMethod", "HMAC-SHA1");
            parameterMap.put("SignatureVersion", "1.0");
            parameterMap.put("SignatureNonce", UUID.randomUUID().toString());//此处将唯一随机数固定只是测试需要，这样此示例中生成的签名值就不会变，方便您对比验证，可变唯一随机数的生成需要用下边这句替换
            parameterMap.put("Format", "JSON");
            // 加入方法特有参数
            parameterMap.put("VideoIds", vid);
            // 对参数进行排序
            List<String> sortedKeys = new ArrayList<String>(parameterMap.keySet());
            Collections.sort(sortedKeys);

            // 生成stringToSign字符
            final String SEPARATOR = "&";
            final String EQUAL = "=";
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
            final String ALGORITHM = "HmacSHA1";
            final String secret = accessKeySecret;//此处请替换成您的AccessKeySecret
            SecretKey key = new SecretKeySpec((secret + SEPARATOR).getBytes(ENCODE_TYPE), SignatureMethod.HMAC_SHA1);
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);
            String signature = URLEncoder.encode(new String(new Base64().encode(mac.doFinal(stringToSign.toString().getBytes(ENCODE_TYPE)))), ENCODE_TYPE);

            // 生成请求URL
            StringBuilder requestURL;
            requestURL = new StringBuilder("http://vod.cn-shanghai.aliyuncs.com?");
            requestURL.append(URLEncoder.encode("Signature", ENCODE_TYPE)).append("=").append(signature);
            for (Map.Entry<String, String> e : parameterMap.entrySet()) {
                requestURL.append("&").append(percentEncode(e.getKey())).append("=").append(percentEncode(e.getValue()));
            }
            try {
                URL realUrl = new URL(requestURL.toString());
                // 打开和URL之间的连接
                URLConnection connection = realUrl.openConnection();
                // 设置通用的请求属性
                connection.setRequestProperty("accept", "*/*");
                connection.setRequestProperty("connection", "Keep-Alive");
                connection.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                // 建立实际的连接
                connection.connect();
                // 获取所有响应头字段
                Map<String, List<String>> map = connection.getHeaderFields();
                // 遍历所有的响应头字段
                for (String keyd : map.keySet()) {
                    System.out.println(key + "--->" + map.get(keyd));
                }
                // 定义 BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (Exception e) {
                System.out.println("发送GET请求出现异常！" + e);
                e.printStackTrace();
            }
            // 使用finally块来关闭输入流
            finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取请求用户信息的access_token
     *
     * @param code
     * @return
     */
    //美番（MOFO）公众号
    //服务号
    static String APPID = "wxfe9eb21fdb46a1a6";
    static String APPSECRET = "c20dc2afd2d8e38a4c49abebf4d0f532";

    //三元佳美公众号
    //服务号
    static String APPIDs = "wx1a8d32888a41fcb2";
    static String APPSECRETs = "58f2162e7c0253e8486b4d8679e787dd";

    //美番（MOFO）公众号
    //订阅号
    static String DYAPPID = "wxd5b48a6ca0c168fa";
    static String DYAPPSECRET = "f89ff5a40b7e440a87e9fff74327e52c";

    //三元佳美公众号
    //订阅号
    //static String APPIDs = "wx1a8d32888a41fcb2";
    //static String DYAPPID = "wx02f87b0f2283d306";
    //static String DYAPPSECRET = "ce7d888e707af276e21464d114995281";


    //https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID

    /**
     * 服务号拉取用户信息
     *
     * @return
     */
    public Map GetFUUserList() {
        Map map = new HashMap();
        String acc = getaccesstoken();
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + acc + "";
        String result = GetHttp(url);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String total = jsonObject.get("total").toString();
        String count = jsonObject.get("count").toString();
        String data = jsonObject.get("data").toString();
        String next_openid = jsonObject.get("next_openid").toString();
        map.put("total", total);
        map.put("count", count);
        map.put("data", data);
        map.put("next_openid", next_openid);
        return map;
    }

    /**
     * 订阅号拉取用户信息
     *
     * @return
     */
    public Map GetDYUserList() {
        Map map = new HashMap();
        String acc = getaccesstokenDY();
        String url = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + acc + "";
        String result = GetHttp(url);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String total = jsonObject.get("total").toString();
        String count = jsonObject.get("count").toString();
        String data = jsonObject.get("data").toString();
        String next_openid = jsonObject.get("next_openid").toString();
        map.put("total", total);
        map.put("count", count);
        map.put("data", data);
        map.put("next_openid", next_openid);
        return map;

    }

    private String charset = "utf-8";
    private String proxyHost = null;
    private Integer proxyPort = null;

    public String doGet(String url) throws Exception {

        URL localURL = new URL(url);

        URLConnection connection = openConnection(localURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
        }

        try {
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }

        } finally {

            if (reader != null) {
                reader.close();
            }

            if (inputStreamReader != null) {
                inputStreamReader.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

        }

        return resultBuffer.toString();
    }

    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }


    public Map doPost(HttpServletRequest request, String code) {
        String openid = "";
        Map map = new HashMap();
        try {
            String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
            //第一次请求 获取access_token 和 openid
            String oppid = doGet(requestUrl);
            net.sf.json.JSONObject oppidObj = net.sf.json.JSONObject.fromObject(oppid);
            openid = (String) oppidObj.get("openid");
            map.put("openid", openid);
            getUserInformationH5(openid);
            //根据openid查询对应的unionid（服务号表）
            String unionid = fuwuhaoService.openidByUnionid(openid);
            //根据unionid去订阅表中查有没有该用户的openid
            int have = dinyuehaoService.unionidByOpenid(unionid);
            int count = 0;
            if (have == 0) {
                count = 0;
            } else {
                count = 1;
            }
            map.put("count", count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * 获取临时acctoken
     *
     * @param code
     * @return
     */
    public Map<String, String> getUserInfoAccessToken(String code) {
        Map<String, String> data = new HashMap();
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
            String result = GetHttp(url);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
            data.put("openid", jsonObject.get("openid").toString());
            data.put("access_token", jsonObject.get("access_token").toString());
            data.put("refresh_token", jsonObject.get("refresh_token").toString());
            String acctoken = jsonObject.get("access_token").toString();
            String refresh_token = jsonObject.get("refresh_token").toString();
            String openid = jsonObject.get("openid").toString();
            redisClient.remove("acctoken");
            redisClient.remove("refresh_token");
            redisClient.remove("openid");
            redisClient.set("acctoken", acctoken);
            redisClient.set("refresh_token", refresh_token);
            redisClient.set("openid", openid);
            data.put("code", "200");
        } catch (Exception ex) {
            ex.printStackTrace();
            data.put("code", "300");
        }
        return data;
    }

    // https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxfe9eb21fdb46a1a6&secret=c20dc2afd2d8e38a4c49abebf4d0f532

    public String GetHttp(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 遍历所有的响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取全局acctoken
     *
     * @param
     * @param
     * @return
     */
    public String getaccesstoken() {
        String urls = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET + "";
        String result = GetHttp(urls);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String acctoken = jsonObject.get("access_token").toString();
        String expires_in = jsonObject.get("expires_in").toString();
        log.info("刚刚拿出来的------------------------------------------" + acctoken);
        redisClient.remove("acctokens");
        redisClient.remove("expires_in");
        redisClient.remove("acctokendata");
        redisClient.set("acctokens", acctoken);
        redisClient.set("expires_in", expires_in);
        redisClient.set("acctokendata", new Date());
        return acctoken;
    }

    /**
     * 获取全局acctoken
     * 订阅号
     *
     * @param
     * @param
     * @return
     */
    public String getaccesstokenDY() {
        String urls = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + DYAPPID + "&secret=" + DYAPPSECRET + "";
        String result = GetHttp(urls);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String acctoken = jsonObject.get("access_token").toString();
        String expires_in = jsonObject.get("expires_in").toString();
        log.info("刚刚拿出来的------------------------------------------" + acctoken);
        redisClient.remove("DYacctokens");
        redisClient.remove("DYexpires_in");
        redisClient.remove("DYacctokendata");
        redisClient.set("DYacctokens", acctoken);
        redisClient.set("DYexpires_in", expires_in);
        redisClient.set("DYacctokendata", new Date());
        return acctoken;
    }


    /**
     * 生成签名
     *
     * @param urls
     * @return
     */
    public Map getSignature(String urls) {
        boolean flag = redisClient.exists("tickets");
        String tick = "";
        if (flag) {//如果有缓存
            Date date = (Date) redisClient.get("ticketdate");
            String dateq = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateq = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Date date1 = new Date(date);
            log.info("缓存日期-------------------" + dateq);
            if ((new Date().getTime() - date.getTime()) >= (7000 * 1000)) {//过期
                log.info("ticket过期");
                tick = getticket();
            } else {//没过期
                log.info("ticket没过期");
                tick = redisClient.get("tickets").toString();
            }
        } else {//没有缓存
            tick = getticket();
        }
        String noncestr = UUID.randomUUID().toString();
        String jsapi_ticket = tick;
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + urls + "";
        String signature = getSha1(string1);
        Map map = new HashMap();
        map.put("noncestr", noncestr);
        map.put("timestamp", timestamp);
        map.put("signature", signature);
        map.put("tick", tick);
        return  map;
    }
    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取用户信息
     *
     * @param
     * @param openid
     * @return
     */
    public Map getUserInformation(String openid) {
        BufferedReader in = null;
        String url = "";
        boolean flag = redisClient.exists("acctokens");
        if (flag) {//如果有缓存
            Date date = (Date) redisClient.get("acctokendata");
            String dateq = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateq = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Date date1 = new Date(date);
            log.info("缓存token日期-------------------" + dateq);
            //Date date1 = new Date(date);
            if ((new Date().getTime() - date.getTime()) >= (7000 * 1000)) {//过期
                log.info("token过期");
                String acc = getaccesstoken();
                url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + acc + "&openid=" + openid + "&lang=zh_CN";
            } else {//没过期
                log.info("token没过期");
                url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + redisClient.get("acctokens") + "&openid=" + openid + "&lang=zh_CN";
            }
        } else {//没有缓存
            String acc = getaccesstoken();
            url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + acc + "&openid=" + openid + "&lang=zh_CN";
        }
        String result = GetHttp(url);
        Map map = new HashMap();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String subscribe = jsonObject.get("subscribe").toString();
        String nickname = jsonObject.get("nickname").toString();
        String sex = jsonObject.get("sex").toString();
        long subscribe_time = Long.valueOf(jsonObject.get("subscribe_time").toString());
        String headimgurl = jsonObject.get("headimgurl").toString();
        String openids = jsonObject.get("openid").toString();
        String city = jsonObject.get("city").toString();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sd.format(new Date(subscribe_time * 1000));
        Date dates = null;
        try {
            dates = sd.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /** int many = 0;
        if (Integer.parseInt(subscribe) == 1) {
            //查询关注表中有没有记录 只加一次
            int count = weixinGuangzhuService.selectCount(openids);
            if (count == 0) {
                //说明已经关注过了
                WeixinGuangzhu weixinGuangzhu = new WeixinGuangzhu();
                weixinGuangzhu.setIntime(dates);
                weixinGuangzhu.setCity(city);
                weixinGuangzhu.setHeadimgurl(headimgurl);
                weixinGuangzhu.setNickname(nickname);
                weixinGuangzhu.setOpenid(openids);
                weixinGuangzhu.setSex(Integer.parseInt(sex));
                weixinGuangzhu.setSubscribe(Integer.parseInt(subscribe));
                weixinGuangzhu.setCount(0);
                weixinGuangzhu.setMany(4);
                weixinGuangzhuService.insertSelective(weixinGuangzhu);
                int id = weixinGuangzhu.getId();//id
                //修改用户抽奖次数+1
                weixinGuangzhuService.updateCount(id);
            }
        }
        //查询用户抽奖次数
         many = weixinGuangzhuService.overplusMany(openid);*/
        redisClient.set("openids", openids);
        map.put("subscribe", subscribe);
        map.put("nickname", nickname);
        map.put("subscribe_time", subscribe_time);
        map.put("headimgurl", headimgurl);
        map.put("openids", openids);
        map.put("city", city);
        map.put("sex", sex);
        //map.put("many", many);
        return map;

    }
    //https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID


    /**
     * 用户抽奖
     *
     * @param
     * @return
     */
    /** public Map choujiang(int type) {
        Map map = new HashMap();
        int lessCount = 0;
        int overplus = 0;
        int many = 0;
        String openid = redisClient.get("openids").toString();
        if (type == 0) {
            //减次数
            lessCount = weixinGuangzhuService.lessCount(openid);
            //剩余抽奖次数
            overplus = weixinGuangzhuService.overplusMany(openid);
            //改用户抽到几等奖
            many = weixinGuangzhuService.manyC(openid);
            //向记录表差数据
            WeixinList weixinList = new WeixinList();
            //查询昵称
            String nickname = weixinGuangzhuService.nickn(openid);
            weixinList.setRemark("四等奖");
            weixinList.setNickname(nickname);
            weixinListService.insertSelective(weixinList);
            map.put("many", many);
            map.put("overplus", overplus);
            map.put("lessCount", lessCount);
        } else if (type == 1) {
            //剩余抽奖次数
            overplus = weixinGuangzhuService.overplusMany(openid);
            map.put("overplus", overplus);
        }
        return map;
     }*/


    /**
     * 获取r
     *
     * @param
     * @param
     * @return
     */
    public static Map getrefulshtoken(String retoken) {
        String result = "";
        BufferedReader in = null;
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=wxfe9eb21fdb46a1a6&grant_type=refresh_token&refresh_token=" + retoken+ "";
        try {
            URL realUrl = new URL(url.toString());
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 遍历所有的响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        return jsonObject;

    }


    /**
     * 获取签名
     *
     * @param
     * @param
     * @return
     */
    public String getticket() {
        String result = "";
        String url = "";
        // redisClient.remove("acctoken");
        boolean flag = redisClient.exists("acctokens");
        if (flag) {//如果有缓存
            Date date = (Date) redisClient.get("acctokendata");
            String dateq = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateq = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Date date1 = new Date(date);
            log.info("缓存token日期-------------------" + dateq);
            //Date date1 = new Date(date);
            if ((new Date().getTime() - date.getTime()) >= (7000 * 1000)) {//过期
                log.info("token过期");
                String acc = getaccesstoken();
                url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + acc + "&type=jsapi";
            } else {//没过期
                log.info("token没过期");
                url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + redisClient.get("acctokens") + "&type=jsapi";
            }
        } else {//没有缓存
            String acc = getaccesstoken();
            url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + acc + "&type=jsapi";
        }
        result = GetHttp(url);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String ticket = jsonObject.get("ticket").toString();
        String expires_in = jsonObject.get("expires_in").toString();
        redisClient.remove("tickets");
        redisClient.remove("expires_in");
        redisClient.remove("ticketdate");
        redisClient.set("tickets", ticket);
        redisClient.set("expires_in", expires_in);
        redisClient.set("ticketdate", new Date());
        return ticket;
    }


    /**
     * 获取用户信息
     *
     * @param
     * @param
     * @return
     */
    public Map getUserInformationH5(String openid) {
        BufferedReader in = null;
        String url = "";
        boolean flag = redisClient.exists("acctokens");
        if (flag) {//如果有缓存
            Date date = (Date) redisClient.get("acctokendata");
            String dateq = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateq = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Date date1 = new Date(date);
            log.info("缓存token日期-------------------" + dateq);
            if ((new Date().getTime() - date.getTime()) >= (7000 * 1000)) {//过期
                log.info("token过期");
                String acc = getaccesstoken();
                log.info("过期拿到的---------------------------------" + acc);
                url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + acc + "&openid=" + openid + "&lang=zh_CN";
            } else {//没过期
                log.info("token没过期");
                url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + redisClient.get("acctokens") + "&openid=" + openid + "&lang=zh_CN";
                log.info("没过期---------------------------------------" + redisClient.get("acctokens").toString());
            }
        } else {//没有缓存
            String acc = getaccesstoken();
            url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + acc + "&openid=" + openid + "&lang=zh_CN";
            log.info("没有缓存---------------------------------" + acc);
        }
        log.info(url);
        String result = GetHttp(url);
        Map map = new HashMap();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String subscribe = jsonObject.get("subscribe").toString();
        String nickname = jsonObject.get("nickname").toString();
        String sex = jsonObject.get("sex").toString();
        long subscribe_time = Long.valueOf(jsonObject.get("subscribe_time").toString());
        String headimgurl = jsonObject.get("headimgurl").toString();
        String openids = jsonObject.get("openid").toString();
        String city = jsonObject.get("city").toString();
        String unionid = jsonObject.get("unionid").toString();
        redisClient.set("openids", openids);
        map.put("subscribe", subscribe);
        map.put("nickname", nickname);
        map.put("subscribe_time", subscribe_time);
        map.put("headimgurl", headimgurl);
        map.put("openids", openids);
        map.put("city", city);
        map.put("sex", sex);
        map.put("unionid", unionid);
        Fuwuhao fuwuhao = new Fuwuhao();
        fuwuhao.setOpenid(openids);
        fuwuhao.setUnionid(unionid);
        fuwuhaoService.insertSelective(fuwuhao);
        return map;
    }

    /**
     * 获取用户信息
     * 订阅号
     *
     * @param
     * @param
     * @return
     */
    public Map getUserInformationH5DY(String openid) {
        BufferedReader in = null;
        String url = "";
        boolean flag = redisClient.exists("DYacctokens");
        if (flag) {//如果有缓存
            Date date = (Date) redisClient.get("DYacctokendata");
            String dateq = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                dateq = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Date date1 = new Date(date);
            log.info("缓存token日期-------------------" + dateq);
            if ((new Date().getTime() - date.getTime()) >= (7000 * 1000)) {//过期
                log.info("token过期");
                String acc = getaccesstokenDY();
                log.info("过期拿到的---------------------------------" + acc);
                url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + acc + "&openid=" + openid + "&lang=zh_CN";
            } else {//没过期
                log.info("token没过期");
                url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + redisClient.get("DYacctokens") + "&openid=" + openid + "&lang=zh_CN";
                log.info("没过期---------------------------------------" + redisClient.get("DYacctokens").toString());
            }
        } else {//没有缓存
            String acc = getaccesstokenDY();
            url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + acc + "&openid=" + openid + "&lang=zh_CN";
            log.info("没有缓存---------------------------------" + acc);
        }
        log.info(url);
        String result = GetHttp(url);
        Map map = new HashMap();
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(result);
        String subscribe = jsonObject.get("subscribe").toString();
        String nickname = jsonObject.get("nickname").toString();
        String sex = jsonObject.get("sex").toString();
        long subscribe_time = Long.valueOf(jsonObject.get("subscribe_time").toString());
        String headimgurl = jsonObject.get("headimgurl").toString();
        String openids = jsonObject.get("openid").toString();
        String city = jsonObject.get("city").toString();
        String unionid = jsonObject.get("unionid").toString();
        redisClient.set("DYopenids", openids);
        map.put("subscribe", subscribe);
        map.put("nickname", nickname);
        map.put("subscribe_time", subscribe_time);
        map.put("headimgurl", headimgurl);
        map.put("openids", openids);
        map.put("city", city);
        map.put("sex", sex);
        map.put("unionid", unionid);

        Dinyuehao dinyuehao = new Dinyuehao();
        dinyuehao.setOpenid(openids);
        dinyuehao.setUnionid(unionid);
        dinyuehaoService.insertSelective(dinyuehao);
        return map;
    }

    /**public List findAllList(Paging<WeixinList> paging) {
        return weixinListService.findAllList(paging);
     }*/

    public static void main(String[] args) {
        /**DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret));
        Map videoId = VideoUploadUtil.createUploadVideo(aliyunClient, "c://f304196fa9ed1e9de8d4ff9a643042fa.mp4", " ", " ", "多的", 100);
         Map videoIds = VideoUploadUtil.createUploadVideo(aliyunClient, "c://f304196fa9ed1e9de8d4ff9a643042fa.mp4", " ", " ", "多的", 10  0);
        System.out.println("VideoId:" + videoId);
        System.out.println("VideoId:" + videoIds);
        String viod = String.valueOf(videoId.get("videoId"));
         refreshUploadVideo(aliyunClient, viod);*/
        //VideoUploadUtil.deleteVideo("a8cf01c12cf748e2b4e997b24cf3edfd");
        // VideoUploadUtil videoUploadUtil = new VideoUploadUtil();
        //videoUploadUtil.getUserInfoAccessToken("0418ZbBa28AWoS0HRxBa2hfvBa28ZbBE");
        // VideoUploadUtil.getrefulshtoken();
        VideoUploadUtil videoUploadUtil = new VideoUploadUtil();
        //   videoUploadUtil.getticket("bCANmBh3eg9XtfRZ75Wy6ko3sn8KXajggGzxUfpGoyME82ON5umkP-hm8cZbw2JvZYnNUWNyg6VdtQi6r-UyF7Dr10w3a4z5xWyslJVeyvc");
        //videoUploadUtil.getaccesstoken();
     }
}
