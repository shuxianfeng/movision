package com.zhuhuibao.business.oms.job;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * 运营系统 - 人才网
 *
 * @author jianglz
 * @since 16/6/24.
 */
@RestController
@RequestMapping("/rest/job/oms")
@Api(value = "OMS-Job", description = "人才网运营系统")
public class OmsJobController {
    private static final Logger log = LoggerFactory.getLogger(OmsJobController.class);

    private static final String IS_HOT = "1";//热门职位
    private static final String NOT_HOT = "0"; //非热门职位

    @Autowired
    JobPositionService positionService;
    @Autowired
    private ResumeService resumeService;
    @Autowired
    private JobPositionService jobService;
    @Autowired
    private UploadService uploadService;

    @ApiOperation(value = "设置为热门职位", notes = "设置为热门职位", response = Response.class)
    @RequestMapping(value = "upd_setup_hot", method = RequestMethod.POST)
    public Response setupHotPostion(@ApiParam("职位ID") @RequestParam String positionId) {

        positionService.updateHot(positionId, IS_HOT);

        return new Response();
    }

    @ApiOperation(value = "取消热门职位", notes = "取消热门职位", response = Response.class)
    @RequestMapping(value = "upd_cancel_hot", method = RequestMethod.POST)
    public Response cancelHotPostion(@ApiParam("职位ID") @RequestParam String positionId) {

        positionService.updateHot(positionId, NOT_HOT);

        return new Response();
    }


    @ApiOperation(value = "设置是否为热门职位", notes = "设置是否为热门职位", response = Response.class)
    @RequestMapping(value = "upd_hot", method = RequestMethod.POST)
    public Response setHotPostion(@ApiParam("职位ID") @RequestParam String positionId,
                                  @ApiParam("是否热门 true|false") @RequestParam String isHot) {

        positionService.updateHot(positionId, isHot.equals("true") ? IS_HOT : NOT_HOT);

        return new Response();
    }

    @ApiOperation(value = "更新编辑已发布的职位", notes = "更新编辑已发布的职位", response = Response.class)
    @RequestMapping(value = "upd_position", method = RequestMethod.POST)
    public Response updatePosition(@ModelAttribute() Job job) throws IOException {
        return positionService.updatePosition(job);
    }

    @ApiOperation(value = "批量更新编辑已发布的职位", notes = "批量更新编辑已发布的职位", response = Response.class)
    @RequestMapping(value = "batch_upd_position", method = RequestMethod.POST)
    public Response batchUpdatePosition(@ApiParam(value = "要更新的id字符串") @RequestParam(required = false) String ids) throws IOException {
        return jobService.refreshPosition(ids);
    }

    @ApiOperation(value = "更新简历", notes = "更新简历", response = Response.class)
    @RequestMapping(value = "upd_resume", method = RequestMethod.POST)
    public Response updateResume(@ModelAttribute Resume resume) throws IOException {
        return resumeService.updateResume(resume);
    }


    @ApiOperation(value = "解析简历", notes = "解析简历", response = Response.class)
    @RequestMapping(value = "analysis_resume", method = RequestMethod.POST)
    public Response analysisResume(@ApiParam(value = "上传文件") @RequestParam MultipartFile file,
                                   @ApiParam(value = "51job:51job,智联:zhilian,人才:rencai,猎聘:liepin") @RequestParam String chann) throws Exception {
        Response result = new Response();
        try {
            Map<String, String> map = uploadService.upload(file, "zip", chann);
          //  Map<String, Object> map = new HashMap<>();
           /* map.put("url", url);
            map.put("name", FileUtil.getFileNameByUrl(url));
            positionService.selDecompression(url);
            result.setData(map);*/
        } catch (Exception e) {
            log.error("analysis_resume error! ", e);
        }
        return result;
    }

}
