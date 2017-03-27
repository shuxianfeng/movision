package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.facade.rewarded.FacadeRewarded;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.constant.entity.Constant;
import com.movision.utils.L;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/1/23 14:45
 */
@RestController
@RequestMapping("/app/rewarded")
public class RewardedController {
    @Autowired
    FacadeRewarded facadeRewarded;
    @Autowired
    PointRecordFacade pointRecordFacade;

    /**
     * 帖子打赏
     *
     * @param postid
     * @param type
     * @param userid
     * @return
     */
    @ApiOperation(value = "积分操作", notes = "返回是否成功", response = Response.class)
    @RequestMapping(value = "rewarded", method = RequestMethod.POST)
    public Response queryCircleIndex1(@ApiParam(value = "帖子id") @RequestParam String postid,
                                      @ApiParam(value = "打赏积分类型") @RequestParam String type,
                                      @ApiParam(value = "打赏用户id") @RequestParam String userid) {
        Response response = new Response();
        Map flag = facadeRewarded.updateRewarded(postid, type, userid);

        if (flag.get("code") == 200 && response.getCode() == 200) {
            response.setMessage("操作成功");
            response.setData(flag);
        }
        if (flag.get("code") == 300) {
            response.setMessage("积分不足");
            response.setData(flag);
        }
        return response;
    }

    /**
     * 查询打赏积分数值列表
     *
     * @return
     */
    @ApiOperation(value = "查询积分列表", notes = "用于查询打赏的积分数值列表接口", response = Response.class)
    @RequestMapping(value = "query_reword_list", method = RequestMethod.POST)
    public Response queryRewordList() {
        Response response = new Response();
        List<Constant> list = facadeRewarded.queryRewordList();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }
}
