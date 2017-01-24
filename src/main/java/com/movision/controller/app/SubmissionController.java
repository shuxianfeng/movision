package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.submission.SubmissionFacade;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author shuxf
 * @Date 2017/1/23 17:19
 */
@RestController
@RequestMapping("/app/submission/")
public class SubmissionController {

    @Autowired
    private SubmissionFacade submissionFacade;

    @ApiOperation(value = "投稿接口", notes = "用于提交原生视频申请投稿", response = Response.class)
    @RequestMapping(value = "commitSubmission", method = RequestMethod.POST)
    public Response commitSubmission(@ApiParam(value = "投稿主题") @RequestParam String title,
                                     @ApiParam(value = "第三方投稿视频地址") @RequestParam String videourl,
                                     @ApiParam(value = "用户id") @RequestParam String userid,
                                     @ApiParam(value = "投稿者邮箱") @RequestParam String email,
                                     @ApiParam(value = "投稿说明") @RequestParam String comment) {
        Response response = new Response();

        int count = submissionFacade.commitSubmission(title, videourl, userid, email, comment);

        if (response.getCode() == 200 && count == 1) {
            response.setMessage("申请提交成功");
        }
        return response;
    }
}
