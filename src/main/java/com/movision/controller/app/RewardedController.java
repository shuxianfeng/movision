package com.movision.controller.app;

import com.movision.common.Response;
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


    /*@ApiOperation(value = "评论列表", notes = "返回帖子评论列表", response = Response.class)
    @RequestMapping(value = "commentLsit", method = RequestMethod.POST)
    public Response queryCircleIndex1(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @RequestParam(value = "打赏积分") String integral,
                                      @RequestParam(value = "打赏用户id") String userid) {
        Response response = new Response();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData();
        return response;
    }*/
}
