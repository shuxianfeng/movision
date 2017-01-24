package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.rewarded.FacadeRewarded;
import com.movision.mybatis.comment.entity.CommentVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/1/23 14:45
 */
@RestController
@RequestMapping("/app/rewarded")
public class RewardedController {
    @Autowired
    FacadeRewarded facadeRewarded;

    @ApiOperation(value = "积分操作", notes = "返回是否成功", response = Response.class)
    @RequestMapping(value = "rewarded", method = RequestMethod.POST)
    public Response queryCircleIndex1(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @ApiParam(value = "打赏积分") @RequestParam String integral,
                                      @ApiParam(value = "打赏用户id") @RequestParam String userid) {
        Response response = new Response();
        facadeRewarded.updateRewarded(postid, integral, userid);
        if (response.getCode() == 200) {
            response.setMessage("操作成功");
        }
        return response;
    }
}
