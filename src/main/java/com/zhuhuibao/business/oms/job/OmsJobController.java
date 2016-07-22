package com.zhuhuibao.business.oms.job;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
