package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.witkey.entity.Cooperation;
import com.zhuhuibao.mybatis.witkey.service.CooperationService;
import com.zhuhuibao.service.MobileWitkeyService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * The type Mobile witkey controller.
 *
 * @author zhuangyuhao
 * @date 2016 -11-21 17:06:25
 */
@RestController
@RequestMapping("/rest/m/witkey/site/")
public class MobileWitkeySiteController {

    @Autowired
    private MobileWitkeyService mWitkeySV;

    @ApiOperation(value = "查询任务列表（前台分页）", notes = "查询任务列表（前台分页）", response = Response.class)
    @RequestMapping(value = "sel_witkeyList", method = RequestMethod.GET)
    public Response findAllCooperationByPager
            (@RequestParam(required = false, defaultValue = "1") String pageNo,
             @RequestParam(required = false, defaultValue = "10") String pageSize,
             @ApiParam(value = "合作类型") @RequestParam(required = false) String type,
             @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
             @ApiParam(value = "系统分类") @RequestParam(required = false) String systemType,
             @ApiParam(value = "省") @RequestParam(required = false) String province,
             @ApiParam(value = "关键字") @RequestParam(required = false) String smart,
             @ApiParam(value = "发布类型，1：接任务，2：接服务，3：资质合作") @RequestParam String parentId) {

        Response Response = new Response();
        Response.setData(mWitkeySV.getPager(pageNo, pageSize, type, category, systemType, province, smart, parentId));
        return Response;
    }



}
