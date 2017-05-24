package com.movision.controller.app;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.AliVideoConstant;
import com.movision.facade.apsaraVideo.AliVideoFacade;

import com.movision.utils.VideoUploadUtil;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/5/19 16:41
 */
@RestController
@RequestMapping("app/video")
public class AppVideoController {

    @Autowired
    private AliVideoFacade aliVideoFacade;

    @Autowired
    private VideoUploadUtil videoUploadUtil;



    @ApiOperation(value = "获取视频播放凭证", notes = "获取视频播放凭证", response = Response.class)
    @RequestMapping(value = "get_video_play_auth", method = RequestMethod.POST)
    public Response getVideoPlayAuth(@ApiParam("vid, ali视频id") @RequestParam String vid) throws Exception {
        Response response = new Response();
        String url = aliVideoFacade.generateRequestUrl(AliVideoConstant.GetVideoPlayAuth, vid);
        Map<String, String> reMap = aliVideoFacade.doGet(url);
        Map result = new HashedMap();
        if (!reMap.isEmpty()) {
            if ("200".equals(reMap.get("status"))) {
                Gson gson = new Gson();
                result = gson.fromJson(reMap.get("result"), Map.class);
            }
        }
        response.setData(result);
        return response;
    }

    @ApiOperation(value = "视频上传接口", notes = "视频上传接口", response = Response.class)
    @RequestMapping(value = "get_video_upload", method = RequestMethod.POST)
    public Response getVideoUpload(@ApiParam("上传文件所在的绝对路径(必须包含扩展名)") @RequestParam String fileName,
                                   @ApiParam("视频标题") @RequestParam String title,
                                   @ApiParam("视频描述") @RequestParam String description,
                                   @ApiParam("封面URL") @RequestParam String coverimg,
                                   @ApiParam("视频标签,多个用逗号分隔") @RequestParam String tatges) {
        Response response = new Response();
        String videoid = videoUploadUtil.videoUpload(fileName, title, description, coverimg, tatges);
        if (response.getCode() == 200) {
            response.setData(videoid);
            response.setMessage("调用成功");
        }
        return response;
    }


    @ApiOperation(value = "获取视频上传凭证和地址", notes = "获取视频上传凭证和地址", response = Response.class)
    @RequestMapping(value = "get_video_address", method = RequestMethod.POST)
    public Response createUploadVideo(
            @ApiParam("必选，视频源文件名称（必须带后缀, 支持 \"3GP\",\"AVI\",\"FLV\",\"MP4\",\"M3U8\",\"MPG\",\"ASF\",\"WMV\",\"MKV\",\"MOV\",\"TS\",    \"WebM\",\"MPEG\",\"RM\",\"RMVB\",\"DAT\",\"ASX\",\"WVX\",\"MPE\",\"MPA\",\"F4V\",\"MTS\",\"VOB\",\"GIF\"）") @RequestParam String fileName,
            @ApiParam("视频标题") @RequestParam String title,
            @ApiParam("视频描述") @RequestParam(required = false) String description,
            @ApiParam("视频标签,多个用逗号分隔") @RequestParam(required = false) String tatges) {
        Response response = new Response();
        DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", videoUploadUtil.accessKeyId, videoUploadUtil.accessKeySecret));

        Map videoid = videoUploadUtil.createUploadVideo(aliyunClient, fileName, description, tatges, title);
        if (response.getCode() == 200) {
            response.setData(videoid);
            response.setMessage("调用成功");
        }
        return response;
    }


    @ApiOperation(value = " 刷新视频上传凭证", notes = " 刷新视频上传凭证", response = Response.class)
    @RequestMapping(value = "get_video_refresh", method = RequestMethod.POST)
    public Response refreshUploadVideo(
            @ApiParam("视频唯一id") @RequestParam String videoid) {
        Response response = new Response();
        DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", videoUploadUtil.accessKeyId, videoUploadUtil.accessKeySecret));
        videoUploadUtil.refreshUploadVideo(aliyunClient, videoid);
        return response;
    }

}
