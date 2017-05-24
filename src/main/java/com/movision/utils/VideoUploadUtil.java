package com.movision.utils;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadVideoResponse;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoRequest;
import com.aliyuncs.vod.model.v20170321.RefreshUploadVideoResponse;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


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
    public static Map createUploadVideo(DefaultAcsClient client, String filename, String description, String targes, String title) {
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
                request.setFileSize(0L);
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

    public static void main(String[] args) {
        DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret));
        Map videoId = VideoUploadUtil.createUploadVideo(aliyunClient, "c://f304196fa9ed1e9de8d4ff9a643042fa.mp4", " ", " ", "多的");
        Map videoIds = VideoUploadUtil.createUploadVideo(aliyunClient, "c://f304196fa9ed1e9de8d4ff9a643042fa.mp4", " ", " ", "多的");
        System.out.println("VideoId:" + videoId);
        System.out.println("VideoId:" + videoIds);
        String viod = String.valueOf(videoId.get("videoId"));
        refreshUploadVideo(aliyunClient, viod);
    }

}
