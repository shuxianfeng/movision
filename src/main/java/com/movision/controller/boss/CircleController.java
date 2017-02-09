package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.CircleFacade;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/2/8 17:55
 */
@RestController
@RequestMapping("/boss/circle")
public class CircleController {
    @Autowired
    CircleFacade circleFacade;

    @ApiOperation(value = "查询圈子列表", notes = "用于查询圈子列表接口", response = Response.class)
    @RequestMapping(value = "/circle_list", method = RequestMethod.POST)
    public Response circleByList(@RequestParam(required = false) String pageNo,
                                 @RequestParam(required = false) String pageSize) {
        Response response = new Response();
        List<CircleVo> list = circleFacade.queryCircleByList(pageNo, pageSize);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(list);
        return response;
    }
}
