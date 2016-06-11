package com.zhuhuibao.business.project.oms;/**
 * @author Administrator
 * @version 2016/5/16 0016
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.TenderToned;
import com.zhuhuibao.mybatis.oms.service.TenderTonedService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *招中标公告管理控制层
 *@author pl
 *@create 2016/5/16 0016
 **/
@RestController
@RequestMapping(value = "/rest/project")
@Api(value = "TenderToned",description = "频道页和运营平台调用")
public class TenderTonedOmsController {

    private final static Logger log = LoggerFactory.getLogger(TenderTonedOmsController.class);

    @Autowired
    TenderTonedService ttService;

    @RequestMapping(value = {"addTenderToned","oms/base/add_tenderToned"},method = RequestMethod.POST)
    @ApiOperation(value="运营管理平台增加招中标",notes = "运营管理平台增加招中标",response = Response.class)
    public Response addTenderToned(@ApiParam(value = "招中标信息") @ModelAttribute TenderToned tt) throws Exception {
        Response response = new Response();
        Long createId = ShiroUtil.getOmsCreateID();
        if(createId != null) {
            tt.setCreateid(createId);
            ttService.insertTenderTone(tt);
        }
        return response;
    }

    @RequestMapping(value = {"updateTenderToned","oms/base/upd_tenderToned"},method = RequestMethod.POST)
    @ApiOperation(value="运营管理平台修改招中标",notes = "运营管理平台修改招中标",response = Response.class)
    public Response updateTenderToned(@ApiParam(value = "招中标信息") @ModelAttribute TenderToned tt) throws Exception {
        Response response = new Response();
        ttService.updateTenderTone(tt);
        return response;
    }

    @RequestMapping(value = {"previewTenderTonedCode","oms/base/sel_tenderTonedCode"},method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台预览招中标信息地区code",notes = "运营管理平台预览",response = Response.class)
    public Response previewTenderTonedCode(@ApiParam(value = "招中标信息ID") @RequestParam Long tenderTonedID) throws Exception {
        Response response = new Response();
        TenderToned tenderToned = ttService.queryTenderToneByIDCode(tenderTonedID);
        response.setData(tenderToned);
        return response;
    }

}
