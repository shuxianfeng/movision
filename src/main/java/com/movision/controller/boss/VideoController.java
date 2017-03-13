package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.video.VideoFacade;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作为上传视频测试使用
 * @Author zhuangyuhao
 * @Date 2017/3/3 10:37
 */
@RestController
public class VideoController {

    @Autowired
    private MovisionOssClient movisionOssClient;

    @Autowired
    private VideoFacade videoFacade;

    /**
     * 可以使用，上传时间有点长
     * @param file
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @ApiOperation(value = "作为上传视频测试使用", notes = "作为上传视频测试使用", response = Response.class)
    @RequestMapping(value = "boss/video/upload_video", method = RequestMethod.POST)
    public Response queryApplyVipList(@RequestParam(value = "file", required = false) MultipartFile file) throws ServletException, IOException {
        String url = movisionOssClient.uploadObject(file, "video", "test");
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        return new Response(map);

        /*Map map = videoFacade.newUploadVideo(request, "video", response);
        return new Response(map);*/

    }


}
