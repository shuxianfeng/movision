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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 废弃不用
     * 原因：上传文件所在的绝对路径，获取不到
     *
     * @param fileName
     * @param title
     * @param description
     * @param coverimg
     * @param tatges
     * @return
     */
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
            @ApiParam("视频标签,多个用逗号分隔") @RequestParam(required = false) String tatges,
            @ApiParam("必选，视频源文件字节数") @RequestParam long filesize) {
        Response response = new Response();
        DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", VideoUploadUtil.accessKeyId, VideoUploadUtil.accessKeySecret));

        Map videoid = VideoUploadUtil.createUploadVideo(aliyunClient, fileName, description, tatges, title, filesize);
        if (response.getCode() == 200) {
            response.setMessage("调用成功");
        }
        response.setData(videoid);
        return response;
    }


    @ApiOperation(value = " 刷新视频上传凭证", notes = " 刷新视频上传凭证", response = Response.class)
    @RequestMapping(value = "get_video_refresh", method = RequestMethod.POST)
    public Response refreshUploadVideo(
            @ApiParam("视频唯一id") @RequestParam String videoid) {
        Response response = new Response();
        DefaultAcsClient aliyunClient;
        aliyunClient = new DefaultAcsClient(
                DefaultProfile.getProfile("cn-shanghai", VideoUploadUtil.accessKeyId, VideoUploadUtil.accessKeySecret));
        VideoUploadUtil.refreshUploadVideo(aliyunClient, videoid);
        return response;
    }


    /**
     * 删除视频
     *
     * @param videoid
     * @return
     */
    @ApiOperation(value = " 删除视频", notes = " 删除视频", response = Response.class)
    @RequestMapping(value = "get_delete_video", method = RequestMethod.POST)
    public Response deleteVideo(@ApiParam("视频唯一id") @RequestParam String videoid) {
        Response response = new Response();
        String result = VideoUploadUtil.deleteVideo(videoid);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 获取用户信息
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 获取用户信息", notes = " 获取用户信息", response = Response.class)
    @RequestMapping(value = "get_user_information", method = RequestMethod.POST)
    public Response getUserInformation(@ApiParam("openid") @RequestParam String openid) {
        Response response = new Response();
        Map result = videoUploadUtil.getUserInformation(openid);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }


    /**
     * 获取用户信息H5
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 获取用户信息H5", notes = " 获取用户信息H5", response = Response.class)
    @RequestMapping(value = "getUserInformationH5", method = RequestMethod.GET)
    public Response getUserInformationH5(@ApiParam("openid") @RequestParam String openid) {
        Response response = new Response();
        Map result = videoUploadUtil.getUserInformationH5(openid);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }


    /**
     * 获取用户信息H5
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 订阅号获取用户信息H5", notes = " 订阅号获取用户信息H5", response = Response.class)
    @RequestMapping(value = "getUserInformationH5DY", method = RequestMethod.GET)
    public Response getUserInformationH5DY(@ApiParam("openid") @RequestParam String openid) {
        Response response = new Response();
        Map result = videoUploadUtil.getUserInformationH5DY(openid);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }

    /**
     * 点击抽奖
     *
     * @param
     * @return
     */
    /**@ApiOperation(value = " 点击抽奖", notes = " 点击抽奖", response = Response.class)
    @RequestMapping(value = "choujiang", method = RequestMethod.POST)
    public Response choujiang(@ApiParam("type") @RequestParam int type) {
        Response response = new Response();
        Map result = videoUploadUtil.choujiang(type);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }*/

    /**
     * 获取fuflshtoken
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 获取fuflshtoken", notes = " 获取fuflshtoken", response = Response.class)
    @RequestMapping(value = "get_fulshtoken", method = RequestMethod.POST)
    public Response getrefulshtoken(@ApiParam("fuflshtoken") @RequestParam String fuflshtoken) {
        Response response = new Response();
        Map result = VideoUploadUtil.getrefulshtoken(fuflshtoken);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }

    /**
     * 获取getSignature
     * @param url
     * @return
     */
    @ApiOperation(value = " 获取getSignature", notes = " 获取getSignature", response = Response.class)
    @RequestMapping(value = "getSignature", method = RequestMethod.POST)
    public Response getSignature(@ApiParam("url") @RequestParam String url) {
        Response response = new Response();
        Map result = videoUploadUtil.getSignature(url);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }


    /**
     * 查询获奖列表
     *
     * @param
     * @return
     */
    /** @ApiOperation(value = " 查询获奖列表", notes = " 查询获奖列表", response = Response.class)
    @RequestMapping(value = "findAllList", method = RequestMethod.POST)
    public Response findAllList(@ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<WeixinList> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = videoUploadUtil.findAllList(pager);
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
                                }*/

    /**
     * 获取getSignature
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 获取全局", notes = " 获取全局", response = Response.class)
    @RequestMapping(value = "getaccesstoken", method = RequestMethod.POST)
    public Response getaccesstoken() {
        Response response = new Response();
        String result = videoUploadUtil.getaccesstoken();
        if (response.getCode() == 200) {
            response.setMessage("获取成功");
            response.setData(result);
        }
        return response;
    }

    /**
     * 获取临时acctoken
     *
     * @param
     * @return
     */
    @ApiOperation(value = " 获取临时acctoken", notes = " 获取临时acctoken", response = Response.class)
    @RequestMapping(value = "get_weiacctoken", method = RequestMethod.POST)
    public Response getUserInfoAccessToken(@ApiParam("code") @RequestParam String code) {
        Response response = new Response();
        Map result = videoUploadUtil.getUserInfoAccessToken(code);
        if (result.get("code").toString() == "200") {
            response.setMessage("获取成功");
            response.setData(result);
        } else if (result.get("code").toString() == "300") {
            response.setCode(300);
        }
        return response;
    }


    @ApiOperation(value = " dy_openid", notes = " dy_openid", response = Response.class)
    @RequestMapping(value = "dy_openid", method = RequestMethod.GET)
    public Response DYOpenid(HttpServletRequest request,
                             @ApiParam("code") @RequestParam String code) {
        Response response = new Response();
        Map openid = videoUploadUtil.doPost(request, code);
        if (response.getCode() == 200) {
            response.setData(openid);
        }
        return response;
    }




}
