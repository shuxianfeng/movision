package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.CircleFacade;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.post.entity.PostList;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
    public Response circleByList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<CircleVo> pager = new Paging<CircleVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<CircleVo> list = circleFacade.queryCircleByList(pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }
    /**
     * 后台管理-查询精贴列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询精贴列表", notes = "查询精贴列表", response = Response.class)
    @RequestMapping(value="/Isessence_list",method = RequestMethod.POST)
    public  Response queryPostIsessenceByList(@RequestParam(required = false,defaultValue = "1") String pageNo,
                                              @RequestParam(required = false,defaultValue = "10") String pageSize){
        Response response= new Response();
        Paging<PostList> pager = new Paging<PostList>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<PostList> list = circleFacade.queryPostIsessenceByList(pager);
        if(response.getCode()==200){
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

}
