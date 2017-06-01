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
import com.movision.fsearch.utils.HttpClient;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.springframework.stereotype.Service;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.crypto.dsig.SignatureMethod;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;


/**
 * @Author zhanglei
 * @Date 2017/5/22 11:44
 */
@Service
public class VideoUploadUtil {

    public static String accessKeyId = PropertiesLoader.getValue("access.key.id");
    public static String accessKeySecret = PropertiesLoader.getValue("access.key.secret");


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
                System.out.println("RequestId:" + response.getRequestId());
                System.out.println("UploadAuth:" + response.getUploadAuth());
                System.out.println("UploadAddress:" + response.getUploadAddress());
            } else {
                map.put("error", 0);
            }
        } catch (ServerException e) {
            System.out.println("CreateUploadVideoRequest Server Exception:");
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("CreateUploadVideoRequest Client Exception:");
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
        VideoUploadUtil.deleteVideo("a8cf01c12cf748e2b4e997b24cf3edfd");
    }
}
