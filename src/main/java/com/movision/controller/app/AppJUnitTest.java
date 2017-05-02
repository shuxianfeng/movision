package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.utils.JsoupCompressImg;
import com.movision.utils.videotransfer.VideoTranscoder;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/20 10:42
 * 本机单元测试控制器
 */
@RestController
@RequestMapping("/app/junittest/")
public class AppJUnitTest {

    @Autowired
    private JsoupCompressImg jsoupCompressImg;

    @Autowired
    private VideoTranscoder videoTranscoder;

    /**
     * 测试图片压缩
     *
     * @return
     */
    @ApiOperation(value = "测试图片压缩", notes = "测试图片压缩", response = Response.class)
    @RequestMapping(value = "testCompressImg", method = RequestMethod.POST)
    public Response testCompressImg(HttpServletRequest request,
                                    @ApiParam(value = "帖子内容") @RequestParam String content) {
        Response response = new Response();

        Map<String, Object> resultmap = jsoupCompressImg.compressImg(request, content);

        if (response.getCode() == 200) {
            response.setMessage("生成成功");
            response.setData(resultmap);
        } else {
            response.setCode(300);
            response.setMessage("生成失败");
        }
        return response;
    }

    /**
     * 测试视频转码
     *
     * @rerutn
     */
    @ApiOperation(value = "测试视频转码", notes = "测试视频转码", response = Response.class)
    @RequestMapping(value = "videoTranscoder", method = RequestMethod.POST)
    public Response videoTranscoder(HttpServletRequest request,
                                    @ApiParam(value = "原生视频地址") @RequestParam String videourl) throws IOException {
        Response response = new Response();

        Map<String, Object> resultmap = videoTranscoder.transfer(videourl);

        if (response.getCode() == 200) {
            response.setMessage("转码成功");
            response.setData(resultmap);
        } else {
            response.setCode(300);
            response.setMessage("转码失败");
        }
        return response;
    }
}
