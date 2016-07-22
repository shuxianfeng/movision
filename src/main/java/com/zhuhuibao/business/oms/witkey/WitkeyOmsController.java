package com.zhuhuibao.business.oms.witkey;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 威客运营接口
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
@RequestMapping("/rest/witkey/oms/base")
public class WitkeyOmsController {
    private static final Logger log = LoggerFactory.getLogger(WitkeyOmsController.class);

    @Autowired
    private CooperationService cooperationService;


    /**
     * 查询任务列表
     */
    @ApiOperation(value="查询任务列表",notes="查询任务列表",response = Response.class)
    @RequestMapping(value = "sel_witkeyList", method = RequestMethod.GET)
    public Response findAllMyCooperationByPager(
            @RequestParam(required = false) String pageNo,@RequestParam(required = false) String pageSize,
            @ApiParam(value = "标题")@RequestParam(required = false) String title,
            @ApiParam(value = "威客类型")@RequestParam(required = false) String type,
            @ApiParam(value = "审核状态")@RequestParam(required = false) String status,
            @ApiParam(value = "省") @RequestParam(required = false) String province,
            @ApiParam(value = "市") @RequestParam(required = false) String city
    )  {
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Response response = new Response();
        Paging<Map<String,String>> pager = new Paging<Map<String,String>>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Cooperation cooperation = new Cooperation();
        cooperation.setType(type);
        cooperation.setTitle(title);
        cooperation.setStatus(status);
        cooperation.setProvince(province);
        cooperation.setCity(city);
        List<Map<String,String>> cooperationList = cooperationService.findAllCooperationByPager(pager, cooperation);
        pager.result(cooperationList);
        response.setData(pager);
        return response;
    }

    /**
     * 编辑任务
     */
    @ApiOperation(value="编辑任务",notes="编辑任务",response = Response.class)
    @RequestMapping(value = "upd_witkey", method = RequestMethod.POST)
    public Response updateCooperation(Cooperation cooperation)  {
        Response response = new Response();
        if(cooperation.getEndTime()!=null){
            cooperation.setEndTime(cooperation.getEndTime()+" 23:59:59");
        }
        if(cooperation.getPrice()==null){
            cooperation.setPrice(Double.parseDouble("-1"));
        }
        cooperationService.updateCooperation(cooperation);
        return response;
    }
}
