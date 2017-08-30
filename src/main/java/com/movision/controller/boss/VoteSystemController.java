package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.voteH5.VoteFacade;
import com.movision.mybatis.activeH5.entity.ActiveH5;
import com.movision.utils.pagination.model.Paging;
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
 * @Date 2017/8/30 9:35
 */
@RestController
@RequestMapping("/voteSystem")
public class VoteSystemController {

    @Autowired
    private VoteFacade voteFacade;


    /**
     * 添加活动
     *
     * @param name
     * @param photo
     * @param begintime
     * @param endtime
     * @param
     * @param activitydescription
     * @return
     */
    @ApiOperation(value = "添加活动", notes = "添加活动", response = Response.class)
    @RequestMapping(value = "insertSelective", method = RequestMethod.POST)
    public Response insertSelective(@ApiParam(value = "活动名称") @RequestParam String name,
                                    @ApiParam(value = "活动图片") @RequestParam String photo,
                                    @ApiParam(value = "活动开始时间") @RequestParam String begintime,
                                    @ApiParam(value = "活动结束时间") @RequestParam String endtime,
                                    @ApiParam(value = "活动说明") @RequestParam String activitydescription) {
        Response response = new Response();
        int result = voteFacade.insertSelective(name, photo, begintime, endtime, activitydescription);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 删除活动
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除活动", notes = "删除活动", response = Response.class)
    @RequestMapping(value = "deleteActive", method = RequestMethod.POST)
    public Response deleteActive(@ApiParam(value = "活动id") @RequestParam int id) {
        Response response = new Response();
        int result = voteFacade.deleteActive(id);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 查询全部活动
     *
     * @return
     */
    @ApiOperation(value = "查询全部活动", notes = "查询全部活动", response = Response.class)
    @RequestMapping(value = "findAllActive", method = RequestMethod.POST)
    public Response findAllActive(@ApiParam(value = "活动名称") @RequestParam(required = false) String name,
                                  @ApiParam(value = "活动开始时间") @RequestParam(required = false) String bigintime,
                                  @ApiParam(value = "活动结束时间") @RequestParam(required = false) String endtime,
                                  @ApiParam(value = "第几页") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @ApiParam(value = "每页多少条") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<ActiveH5> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
        List result = voteFacade.findAllActive(name, bigintime, endtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("返回成功");
        }
        pager.result(result);
        response.setData(pager);
        return response;
    }

}
