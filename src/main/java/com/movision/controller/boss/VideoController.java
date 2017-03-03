package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/3 10:37
 */
@RestController
public class VideoController {

    @Autowired
    private MovisionOssClient movisionOssClient;

    @ApiOperation(value = "上传视频", notes = "上传视频", response = Response.class)
    @RequestMapping(value = "boss/video/upload_video", method = RequestMethod.POST)
    public Response queryApplyVipList(@RequestParam(value = "file", required = false) MultipartFile file) {
        String url = movisionOssClient.uploadObject(file, "video", "test");
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        return new Response(map);
    }
}
