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
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    //static String APPID = "wx1a8d32888a41fcb2";
    //static String APPSECRET = "58f2162e7c0253e8486b4d8679e787dd";

    //美番（MOFO）公众号
    //订阅号
    //static String DYAPPID = "wxd5b48a6ca0c168fa";
    //static String DYAPPSECRET = "f89ff5a40b7e440a87e9fff74327e52c";

    //三元佳美公众号
    //订阅号
    static String DYAPPID = "wx02f87b0f2283d306";
    static String DYAPPSECRET = "ce7d888e707af276e21464d114995281";


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



    /**
     * wex
     * @param request
     * @param code
     * @return
     */
    public Map doPost(HttpServletRequest request, String code) {
        String openid = "";
        Map map = new HashMap();
        try {
            String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
            //第一次请求 获取access_token 和 openid
            String oppid = doGet(requestUrl);
            net.sf.json.JSONObject oppidObj = net.sf.json.JSONObject.fromObject(oppid);
            openid = (String) oppidObj.get("openid");
            getUserInformationH5(openid);
            //根据openid查询对应的unionid（服务号表）
            String unionid = fuwuhaoService.openidByUnionid(openid);
            //根据unionid去订阅表中查有没有该用户的openid
            int have = dinyuehaoService.unionidByOpenid(unionid);
            String DYopenid="";
            int count = 0;
            if (have == 0) {
                count = 0;
                map.put("DYopenid", DYopenid);
            } else {
                DYopenid= dinyuehaoService.unionidByOpenids(unionid);
                Map map1=getUserInformationH5DY(DYopenid);
                count=Integer.parseInt(map1.get("subscribe").toString());
                if(count==0){
                    dinyuehaoService.unionidByD(unionid);
                }
                map.put("DYopenid", DYopenid);
            }
             map.put("count", count);
            //把用户的信息存入表中code  count
            Fuwuhao fuwuhao = new Fuwuhao();
            fuwuhao.setCode(code);
            fuwuhao.setCount(count);
            fuwuhao.setOpenid(openid);
            int update=dinyuehaoService.updateFU(fuwuhao);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map selectOc(String code){
        Map map = new HashMap();
         Fuwuhao fuwuhao=dinyuehaoService.selectOc(code);
        int count=fuwuhao.getCount();
        String unionid=fuwuhao.getUnionid();
        String DYopenid = dinyuehaoService.unionidByOpenids(unionid);
        map.put("DYopenid",DYopenid);
        map.put("count",count);
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
     * 检验微信tokken
     * @param request
     * @param response
     */
     public String get(HttpServletRequest request,HttpServletResponse response) {
        log.info("请求进来了...");
        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature =request.getParameter("signature");
        // 时间戳
        String timestamp =request.getParameter("timestamp");
        // 随机数
        String nonce =request.getParameter("nonce");
        // 随机字符串
        String echostr =request.getParameter("echostr");

        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
            if (MessageUtil.checkSignature(signature,timestamp, nonce)) {
                out.print(echostr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
            out = null;
        }
        return echostr;
    }

    /**
     * 处理微信服务器发来的消息
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        get(request,response);
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
        int subscribe = Integer.parseInt(jsonObject.get("subscribe").toString());
        String nickname="";
        String sex="";
        long subscribe_time=0;
        String headimgurl="";
        String openids="";
        String city="";
        if(subscribe==1) {
              nickname = jsonObject.get("nickname").toString();
              sex = jsonObject.get("sex").toString();
              subscribe_time = Long.valueOf(jsonObject.get("subscribe_time").toString());
              headimgurl = jsonObject.get("headimgurl").toString();
              openids = jsonObject.get("openid").toString();
              city = jsonObject.get("city").toString();
        }
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
        //查询表中服务号有没有
        int have=fuwuhaoService.haveFuwu(openid);
        if(have==0) {
            fuwuhao.setOpenid(openids);
            fuwuhao.setUnionid(unionid);
            fuwuhaoService.insertSelective(fuwuhao);
        }
        return map;
    }

    public String sssss() {
        String a = "oWYQJwDGCW3n6H62YFP9XDauEaYk,oWYQJwORibbTsUE824N3xeKgeqOI,oWYQJwLFpZIT9ELp4CmluiXFkTjg,oWYQJwDWYKk60wF8BWwfP2k3gMiU,oWYQJwJ_49i4zRUB7vFbItJaVtZA,oWYQJwLuktM2IMuiRt_NrKAq5nxQ,oWYQJwF3IuYt733QvOeScb8n45BM,oWYQJwFnUiqGzesbFOhT554YzsQg,oWYQJwL97X8UprMZjGrAJ1ji-Eok,oWYQJwGmYrMtQ0vUOeK67zKz3BEE,oWYQJwGIkhwz7novQ9NXERyYTKmo,oWYQJwCZRSMWLfusp21as-WKErok,oWYQJwEqHbIEa8r8DDRq2td6huFE,oWYQJwFRqkQphNi0mk0nCmDWEZUI,oWYQJwOhPDA_nlxQI6v3F0Bb8w5k,oWYQJwLJbpPQsPZPP8GBHrtQuvA8,oWYQJwA5vBqn_RwA6frqfd3yz3oY,oWYQJwFwIHiS9YfLF78EwIgVGYx0,oWYQJwGJL6-xDvPVBWbhUyBZRno0,oWYQJwJ3viN5_oMjNh8X7PXnXKdM,oWYQJwMDsM4X7eYV-5b9_YJmsliQ,oWYQJwGrkQr-rm7_pE4RT9pStDhg,oWYQJwEvR272u4NragHvBH3VjwG0,oWYQJwGxhYokHUtu-VVbzq4HYjGs,oWYQJwCztI0wZxlKOiseqsHTqsrI,oWYQJwFLCF7cYSxoF2noFiQkdEpE,oWYQJwDxS3zzRukFzDVbM2Sbaz4s,oWYQJwPUy2TDs74Y-Pwm3JSzGtms,oWYQJwOj0NoB5tQShHpGUyoJEmBk,oWYQJwLrRCmW8O-gNbombZaMUA_A,oWYQJwNdYOLjjny8BgYXuY77ayu0,oWYQJwBmmxD5_eYrRNnLMSdXfb3w,oWYQJwGjjB9bjtB8QGxVd7ZTvhSM,oWYQJwMb7uL9VQPn1erW0Do46IOI,oWYQJwH2cOqlnWkwDaWBatXt7RiQ,oWYQJwPSggrHgA8EGo9rvOCQbDCY,oWYQJwAsaniPI3gn0oFd0_IPCaso,oWYQJwFXGPN9ge5NLpY0rmKQp_S4,oWYQJwFF2pOTkj0ppMCLQ29iwYm0,oWYQJwImdOFVpcp6lrFGy_qGJXI4,oWYQJwLejrc5rbUafinHiD2bPsr8,oWYQJwFsQUGXirsZHBILGA5_L-C0,oWYQJwMhDGmXPRZmCE6vay3jDyys,oWYQJwMmVYK2YK_ElzHqxramhv1s,oWYQJwEAJ7JvH99BaVOX-FkecFLA,oWYQJwDbE771x1m7-V_rCE8wOFrY,oWYQJwIcRKAt7owWX0IluTUwv0jg,oWYQJwCgAY20fksHOBY1P5cJGB_c,oWYQJwOtL0uesz4AdS11LtDiwkMo,oWYQJwB1CrAghcvmQo6VGqhwOuzE,oWYQJwGPJwwQ3Uej1uufku8WQVPE,oWYQJwBvH7zV4m0j-Dh_Y4-SbnaA,oWYQJwLLf7DHWz6NDnB7Yt6BQOIg,oWYQJwJKlkwNjGJVY4sRfb4FvBB8,oWYQJwCuIEEWXIk7hNkIcJsbKK6Y,oWYQJwOkprI01DlwC6q0FGTECQkE,oWYQJwMT1PH_4-wYxFEaJWDtEaiM,oWYQJwOx9jOQ_hVNCfBRFLfkHx-g,oWYQJwHxsKfohaKFI7VFr3cVspx0,oWYQJwB4b9Xm9EH81k_KRwvLVUp4,oWYQJwCARmfZhj3LMnlKAeTITcr4,oWYQJwKPWAOeMyBNtImu3oGd19h0,oWYQJwHUvXJ6s79JYnBrq8a0o5CU,oWYQJwAptuJTu2oat0RKvV529ocY,oWYQJwPM6BTfcIEYOZT_T61YaSXY,oWYQJwMTqMTjjqFK8GddQwJj3fgI,oWYQJwFQoTPdyRCv30ZWHveyvklg,oWYQJwLd6xdxXuUVXgu_ueMosvaQ,oWYQJwEYMpWuAPr78FtGfZ_WcQ94,oWYQJwPmTVGqx8zDwM8a92MSS3MU,oWYQJwKdqw47eoQhW7A846TRyuRw,oWYQJwIc4pfUBr-O2iyH6l3NdjBA,oWYQJwM7y8powI3ZHcuthSSZ7l8M,oWYQJwJ4En6PN7A3P_OucfcVp4ug,oWYQJwD0Jf5Y7pn11VccZSzOuw3I,oWYQJwAOZoyTPA0I1TqDe4rWNd-4,oWYQJwKmdwSWpwZNzOX0z_Wq34yw,oWYQJwDd3adrNshE_yy66EwnKfO0,oWYQJwGJQ-tNM6ifl24ymkQYDdLA,oWYQJwOFdUhMCAsj1dveuzymUqRE,oWYQJwBnjbyuNnjtYjHbsYL1rUL0,oWYQJwJhe7rG73PO2ttVrUjnLWjY,oWYQJwAeLbzw8aV8vflcGtkJEbSA,oWYQJwKFI0qJUw4114YFL1-133t4,oWYQJwGBp19815gaChzFThp7sMAE,oWYQJwCyYxLugBO90ysLVVJdUJps,oWYQJwLdCe9D2d_7M3I5jA2dem1E,oWYQJwLmTXPu9eOax0tW49CcMEIo,oWYQJwPyuE6_Xx7fnD_quivnUgS4,oWYQJwOyE1VNcWs-Je7YdBDP7mGw,oWYQJwCammTJWG48jm4Q81BpuQYo,oWYQJwMvy3kepA-286Zv1qncIHh0,oWYQJwKstCicoc3-r0vZIfm_vVbg,oWYQJwO9cdB9GxCHVmQE1v3sP8vM,oWYQJwKP1qYlGixT8iS0S_OFbb8w,oWYQJwDyY01IZfFV1I6neAFZTnww,oWYQJwHSdMMRtpVU3Rj4Ze2_7LCo,oWYQJwNm0QJHtYSrVjShj6RzkcBM,oWYQJwFBrCzIGfL5JKsLXbLd8mlQ,oWYQJwJERXBaFUnqMI9XNVdqONWA,oWYQJwNVQhkaPkVvrGfYXNH3Of3M,oWYQJwOiJO9HNHMBNSxCHvVld2fI,oWYQJwE7js3wBPTfcNl6biEeGByo,oWYQJwFMRLoQgiWa7rPH191SEh3c,oWYQJwMoRrEG8JRF71kf3KZpdx38,oWYQJwGndLlsp_auzYkBge2F_xOM,oWYQJwCXAiBsMh5ZwsmUJAWR-0o4,oWYQJwDmjl4Mm7kUBzaVYQf0sTXI,oWYQJwJJ8PR2kJIpCF58NHFVNyt0,oWYQJwIgfLrn1tGi_KPgG6IFFE5w,oWYQJwJk-FrgvtnvYD1EEw6EK-yU,oWYQJwH2ZgA4W2Ufiy2x28xudm30,oWYQJwLqV2S-pdo4xO5ITxAyQ9Ws,oWYQJwEp_uWRay5GKvq9bN4AGcPI,oWYQJwLcmCNpO6hGIQtPix_w0sA4,oWYQJwK6uIcV8lQr8WX_BvXuqNi0,oWYQJwHW1yoBy9I-_7eSx-SSdhb8,oWYQJwClvIY_T-uAMR4ZuKZ1vVkY,oWYQJwFdK4d90mBJsmiTf6-2pYPI,oWYQJwOwKOdWLdF9re4RjTVCfSbg,oWYQJwC0REokQdWr7tsgkkeEl-qc,oWYQJwK5Fk4KzXCGxYTyg1aJEY-E,oWYQJwB0yQaIxtXLQ-Ro25btlJ8w,oWYQJwB-THXdL2xYAyqVp13PBA4U,oWYQJwJcMAGYfC40kZXl3NSDkhCg,oWYQJwHJPVX0mBzN33i9j4tobnrE,oWYQJwFlP1rBqCRrcfRtZCo5ehuA,oWYQJwFAEERO0pX-lIRrrI_b5W7s,oWYQJwHu-NoD-lFhCbW8zeWLr-sA,oWYQJwCxR4Ro6DFwI5uDHMGE2yq4,oWYQJwP2zkEa3dTTMdYIZ6NQ7g2A,oWYQJwEV7hb-XyqUyRZYFW9BgDWY,oWYQJwIXzODpZjg9aBr72Wx1EYpM,oWYQJwJfnkEti4b9jUUOt4IWw5ws,oWYQJwG5_r0d2tlf3uVVF7MIfrKQ,oWYQJwMN0EBaDbiE7vlY9RFQ7YDg,oWYQJwF-OJ92BFpIxdUdZA5w_SD0,oWYQJwGBalfEb5xSh42saaEhYgS0,oWYQJwIEVcJEYn-U66TPj7xhSjQs,oWYQJwGP_we5T-Rx_3wpEFGbZf-M,oWYQJwFQ7WOJ3J_goopE9V_JoRhs,oWYQJwHRie8o2-HSdT1t1VDr2U48,oWYQJwGzP4OIXH-E-2MnEU0cHK10,oWYQJwBzllfgIwGZjomTufKhSd2U,oWYQJwF0YT4hkE6L49JrM_38wAVY,oWYQJwHJpfBWeUiuuBYy1UUb4ErU,oWYQJwCGyPSqLjyFS0VFovMgvrCc,oWYQJwPQJ92RLNmB1JOB4LJnJghQ,oWYQJwHdHXglUErsLXKTc0EuY2Ic,oWYQJwIljx3vomnmOb-_xCbghDp8,oWYQJwKIV2nooUAAUKbBJH229D88,oWYQJwOf1N7CuHEAi-vL3vvGEcRQ,oWYQJwKtx9GLkYUqRIeNcRoI4xH4,oWYQJwLNctBT2ybv_dG_3nYYPel8,oWYQJwHz-cjgNeajMsY7W15wTRsI,oWYQJwCDk6ob7swAb3_qAEhJl6yM,oWYQJwOLh-mNzyeW3GywUDapCeLY,oWYQJwMHIbNA9PDg6kGbLZnhWOrs,oWYQJwJJzOkfag3KSm9HQS95zwGo,oWYQJwFdHnickFhkQa8PU2Dudoh4,oWYQJwH1kFadFID_wcRQXfJVbAhg,oWYQJwEukSrmCldR3htMANVZsbJA,oWYQJwOrJVtt-aGbdL260j8VotL8,oWYQJwFmaslcmYR-mUNp97bVJOlo,oWYQJwNwAVJxT9b0gW0b7ZAcpJ1U,oWYQJwBIN6XLKaI1P91HwiNSRA-A,oWYQJwP0ObOz9IACv5Mgh7qi6xwE,oWYQJwGlueXJfgHhOzYO-_XdOYY4,oWYQJwJ4NG3isDhs_FVKAOnDrPKo,oWYQJwL0YPHO08kYHcGp8yOPJqjI,oWYQJwNlo66YO9MliV0TSBES3UFs,oWYQJwFUPOWHEekbPh2I1-Dk_LXA,oWYQJwMUBpkamsrxJutF3LGrEUlM,oWYQJwEQHgVflzjcejg8pR8Y5tV4,oWYQJwAuT-Fq9nzvwpLywcFeDzIg,oWYQJwEr6IJ2Y-Ec2czFbZupEz4s,oWYQJwEuRIBrQb_3D-63wHfuhVFA,oWYQJwNERe46D7aA0oojWzCqqyDE,oWYQJwF9Tpt2s65aWxTJYP_RIeaY,oWYQJwPmPNh_MIUQhrsf4wWQ69s8,oWYQJwHhJJFWlXFiTzmzUZTo-sMk,oWYQJwJuc3wFHMUGEhoRvbRgweJs,oWYQJwOAWjF2Kr8svAFtml0U3GwU,oWYQJwAJlxPmu4KALRgSEwV0VHv4,oWYQJwC1pnyENsX0W9IJn0M7auhU,oWYQJwNgN67a-g--SeGX12ozhTEI,oWYQJwB4vzRkx2xwV_bJSRdnnpcE,oWYQJwHARaChmE-55ThS_sEzap9g,oWYQJwCQAVJMF73Z0kiQ5zk16Z70,oWYQJwFc3auOoy_J0_PFV1_o7oo4,oWYQJwARDpFqM-TTFp7iUUiDNaFk,oWYQJwIcNNzt5GiBplaj9z1hW2Uo,oWYQJwCiPd6UcJ5c5jbdO4TJmIOI,oWYQJwE8oyx2D_jYQ7LdULMq2-Bw,oWYQJwELEmALDXau7j2iQDzcGawY,oWYQJwGXOUL7IJNl0idEThZvdvqM,oWYQJwDcFhT1tQ_m87SL2MryWG_0,oWYQJwPVvLYntCjolBskXfB4RV9g,oWYQJwBQv47Px5pp2k55XIB3PHiE,oWYQJwH_yMYjmjliDg5_w5Ab602I,oWYQJwLuuwKwlWiJHlyUQcWxhjkw,oWYQJwES9xZsraC6cznsm0j8nhtM,oWYQJwPFYPhEZ02QlGP_9020M4pc,oWYQJwIsie5av6Rdg3Acxu9o_sj8,oWYQJwC0Kw6SKPu0gPbd-53PnhcY,oWYQJwJJXegovgCvbwBO-Il7FT64,oWYQJwGkhSz9mYv6dQ13splbcPx8,oWYQJwBco6z8lGRBK-RZeslncmbE,oWYQJwDEp7pBYNNEY9gUykIcJCrw,oWYQJwMFjHOgedxxIIsRgAyizQns,oWYQJwAldbkPxMirWdmw39DT4x5A,oWYQJwK4n9qf-wFcYNWGnUwV0S-E,oWYQJwLjSIjLR63hInU0yMUmLnRU,oWYQJwH-ffGIBacNjcqf7zysvUaY,oWYQJwI858ChDeFHcOXzz9W9OWIQ,oWYQJwKoEuQhRDuGmR9JegOikj1w,oWYQJwOCotmU5R5HT2sQuhZXHtfE,oWYQJwDIh3Z9w2Wa0fFaWNQNd_NY,oWYQJwK94axYTEjjk_haEHrRRfkI,oWYQJwET0SsDXM9AcnX4zAj3tmRw,oWYQJwBqNAbuG_LlVeTfMSvw8XcE,oWYQJwGDTYxXZw43bWMAaTxMj2zs,oWYQJwNyIjus4wC_vSUPIrWtI6Fw,oWYQJwG6cKMIsn4k_m4JyW9Gv-Tg,oWYQJwJVShK9FXczc3VijTSEMQ0Y,oWYQJwCoWN50VkuESysUAjJYij3Y,oWYQJwDdJCzmDZjsIrn2BKR8srKA,oWYQJwL5nt4CzYdddUB3lGSYEvHA,oWYQJwN7gl-su4C_e6xVyV4knzno,oWYQJwEQSPtdJo7GvHLlhZ8tsPXM,oWYQJwFxzZ73hgWfrKRMZLhNJ7hc,oWYQJwOpo3ga2xNtYkcXyr8ab9Kk,oWYQJwOh33XximwbzfKFNoi6qhwE,oWYQJwIZfKtH67jpYrOr-sQVHW80,oWYQJwCeeZJ8cinopRvHMIDEiekQ,oWYQJwC1XZ8VU307vk4zylk5r1Vk,oWYQJwPX02otTT11_J2ycVbI0h0M,oWYQJwMJ3-LEsWILaHt75fpcxRJk,oWYQJwDkq7d2t_NPkeRrpoZmSxqI,oWYQJwMxFimKWl8fMPxHk7dktk8E,oWYQJwI90fwMMpHO93rKJ012jArc,oWYQJwNXtVaMNIEesuyT39YLGbu8,oWYQJwGq5lJog-FhsiCZxRVExNqY,oWYQJwGGWRHWKc-vuSgzvS-TW8iI,oWYQJwFPnoi5eeGPPqDJD4E6vyyM,oWYQJwHdFyu_CMUd_aR6iCjxWa5M,oWYQJwNqy7Rw9bdY1uryE8AQxwB4,oWYQJwNI0ID7X-7bb8x-IinllotI,oWYQJwKN61DqoUMGQ3mMiBi1pQK0,oWYQJwDCWiWFWHg4M-EJY4Jw8n34,oWYQJwFMG8FiByHBnb0SNIxjE6GA,oWYQJwOynE_hG5X9QrPs-8L_i_g4,oWYQJwNGpX4ImHdJLuCYbcwbV6jk,oWYQJwK00_EQkxwXB-W0L_GaHcF0,oWYQJwCAz0xPivhen2MmxYcqqYXo,oWYQJwNl7k1DkuqWa3n9mYs4odug,oWYQJwLCtQfZchRzuhsVuCUrW7bE,oWYQJwNiKCkxCdUKl3s8ckOaIhdA,oWYQJwDaPRNoKCTyneDlcAsEJc9k,oWYQJwMiivAyITs2gz-6KAbxt-EE,oWYQJwHKbyXJwy6yAKF9-NDHlSHU,oWYQJwLGqBfRfQ9gS_8uvDPbz0zs,oWYQJwO7K-5i20WRANNnwKXh_GpU,oWYQJwE38ve2z5zT_CrVvAtWkmoI,oWYQJwOYtgQ7TP4G_y0gPHcqwm6k,oWYQJwPaHR3E4vMq0mMfJhi6rFDI,oWYQJwCRxEK-BVaXDkq3LMjiaXnE,oWYQJwAaJKRhVQB9k9SUJVZe5duE,oWYQJwPpE2Cc9kp1KZgMFV0QxIdE,oWYQJwLRtlwPs3Ro-0q6izrwm8JE,oWYQJwIgjoFVJA8naFD4SLzdKfqE,oWYQJwKWospLIm6E2XREty-iFHzI,oWYQJwGYBXMybBDj1GaGDxQTZ0BM,oWYQJwOeZ5qepURSuo68LK-SYl1s,oWYQJwLi10f3HylIIhliIysUpn04,oWYQJwE-sH2ZF_dSojSOKRQ1mlFg,oWYQJwLGHrcDJ4MRrnhszTln0Vdc,oWYQJwICSvtNeQWohK_DcMBSuh0Q,oWYQJwE-l4pxR6yCBlbFZuwKXWrM,oWYQJwAjG4I50ay_vSIk76sooFVM,oWYQJwJHRMzJiClZGfzC2gewbCok,oWYQJwFKo7QWzDXGhrpFvCtzSAMU,oWYQJwInwirpie9__0_m0eNUFAh4,oWYQJwK85oWqDn3ODwSmG_uHPuXc,oWYQJwFOZn2wVwtwir-CjtDJnDmM,oWYQJwDLlQjjHT2yurTjefjRRdOw,oWYQJwAbaVpItF1e-jhyght487jE,oWYQJwLzePfnlfmn2-d2Fc00s2kg,oWYQJwN4-XERul1aELSCN3YI5vVI,oWYQJwFV91XJvx4u7J5XpDYUwt7I," +
                "oWYQJwKA4iN9iMx5530sQDmN89R4,oWYQJwIWAW-eCsiUz22fVRZEU25A,oWYQJwDz_NikazQzNNQoHpPjAFDY,oWYQJwLK6ezxOcdzn3SDFPAq_N34,oWYQJwGs53d97UjZZS-AxJplBprg";
        String cc = "";
        String[] c = a.split(",");
        for (int i = 0; i < c.length; i++) {
            cc = c[i];
            getUserInformationH5DY(c[i]);
        }
        return cc;
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
        int subscribe = Integer.parseInt(jsonObject.get("subscribe").toString());
        String nickname="";
        String sex="";
        long subscribe_time=0;
        String headimgurl="";
        String openids="";
        String city="";
        if(subscribe==1) {
              nickname = jsonObject.get("nickname").toString();
              sex = jsonObject.get("sex").toString();
              subscribe_time = Long.valueOf(jsonObject.get("subscribe_time").toString());
              headimgurl = jsonObject.get("headimgurl").toString();
              openids = jsonObject.get("openid").toString();
              city = jsonObject.get("city").toString();
        }
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
        //查询有没有
        int c=dinyuehaoService.selectO(openid);
        if(c==0) {
            Dinyuehao dinyuehao = new Dinyuehao();
            dinyuehao.setOpenid(openids);
            dinyuehao.setUnionid(unionid);
            dinyuehaoService.insertSelective(dinyuehao);
        }
        return map;
    }

    /**public List findAllList(Paging<WeixinList> paging) {
        return weixinListService.findAllList(paging);
     }*/

    /**
     * 创建菜单
     * @return
     */
    public String createMenu() {
        String menu = "{\"button\":[{\"type\":\"click\",\"name\":\"投票系统555\",\"key\":\"1\"}]}";

        //此处改为自己想要的结构体，替换即可
        String access_token=getaccesstokenDY();
        String action = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+access_token;
        try {
            URL url = new URL(action);
            HttpURLConnection http =   (HttpURLConnection) url.openConnection();

            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒
            http.connect();
            OutputStream os= http.getOutputStream();
            os.write(menu.getBytes("UTF-8"));//传入参数
            os.flush();
            os.close();

            InputStream is =http.getInputStream();
            int size =is.available();
            byte[] jsonBytes =new byte[size];
            is.read(jsonBytes);
            String message=new String(jsonBytes,"UTF-8");
            return "返回信息"+message;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "createMenu 失败";
    }



    public Map<String, String> parseXml(HttpServletRequest request){
        // 将解析结果存储在HashMap中
        Map<String, String> map = new HashMap<String, String>();
        try {
            // 从request中取得输入流
            InputStream inputStream = request.getInputStream();
            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            // document.selectSingleNode("//")
            // 得到xml根元素
            Element root = document.getRootElement();
            // 得到根元素的所有子节点
            List<Element> elementList = root.elements();
            // 遍历所有子节点
            for (Element e : elementList)
                map.put(e.getName(), e.getText());
            // 释放资源
            inputStream.close();
            inputStream = null;
        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }
    /**
     * 微信的返回xml
     * @param request
     * @return
     */
    public  String processRequest(HttpServletRequest request) {
        String fromUserName="";
        String respContent="";
         try {
             // xml请求解析
            Map<String, String> requestMap =parseXml(request);
            // 发送方帐号（open_id）
            fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 事件类型
            String eventType = requestMap.get("Event");

            String eventKey = requestMap.get("EventKey");
             if(eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
                 if(eventKey.equals(1)){
                     respContent = "聊天唠嗑菜单项被点击！";
                 }
             }
        }catch (Exception e){
            e.printStackTrace();
        }
        return respContent;
     }

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
       // videoUploadUtil.createMenu();
     }
}
