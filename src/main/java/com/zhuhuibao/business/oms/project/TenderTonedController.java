package com.zhuhuibao.business.oms.project;/**
 * @author Administrator
 * @version 2016/5/16 0016
 */

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
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
@Api(value = "招中标公告管理控制层",description = "频道页和运营平台调用")
public class TenderTonedController {

    private final static Logger log = LoggerFactory.getLogger(TenderTonedController.class);

    @Autowired
    TenderTonedService ttService;

    @RequestMapping(value = "addTenderToned",method = RequestMethod.POST)
    @ApiOperation(value="运营管理平台增加招中标",notes = "运营管理平台增加招中标",response = JsonResult.class)
    public JsonResult addTenderToned(@ApiParam(value = "招中标信息") @ModelAttribute TenderToned tt) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Long createId = ShiroUtil.getOmsCreateID();
        if(createId != null) {
            tt.setCreateid(createId);
            ttService.insertTenderTone(tt);
        }
        return jsonResult;
    }

    @RequestMapping(value = "updateTenderToned",method = RequestMethod.POST)
    @ApiOperation(value="运营管理平台修改招中标",notes = "运营管理平台修改招中标",response = JsonResult.class)
    public JsonResult updateTenderToned(@ApiParam(value = "招中标信息") @ModelAttribute TenderToned tt) throws Exception {
        JsonResult jsonResult = new JsonResult();
        ttService.updateTenderTone(tt);
        return jsonResult;
    }

    @RequestMapping(value = "searchTenderTonedPager",method = RequestMethod.GET)
    @ApiOperation(value="查询招中标分页展示",notes = "根据条件查询招中标",response = JsonResult.class)
    public JsonResult searchTenderTonedPager(@ApiParam(value = "招中标公告名称") @RequestParam(required = false) String noticeName,
                                             @ApiParam(value="省代码") @RequestParam(required = false) String province,
                                             @ApiParam(value="市代码") @RequestParam(required = false) String city,
                                             @ApiParam(value="招中标类型：1招标，2中标") @RequestParam(required = false) String type,
                                             @ApiParam(value = "开工日期查询开始日期") @RequestParam(required = false) String startDateA,
                                             @ApiParam(value = "开工日期查询结束日期") @RequestParam(required = false) String startDateB,
                                             @ApiParam(value = "竣工日期查询开始日期") @RequestParam(required = false) String endDateA,
                                             @ApiParam(value = "竣工日期查询结束日期") @RequestParam(required = false) String endDateB,
                                             @ApiParam(value = "页码") @RequestParam(required = false) String pageNo,
                                             @ApiParam(value="每页显示的条数") @RequestParam(required = false) String pageSize) throws Exception {
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("type",type);
        map.put("noticeName",noticeName);
        map.put("province",province);
        map.put("city",city);
        map.put("startDateA",startDateA);
        map.put("startDateB",startDateB);
        map.put("endDateA",endDateA);
        map.put("endDateB",endDateB);
        Paging<TenderToned> pager = new Paging<TenderToned>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<TenderToned> ttList = ttService.findAllTenderTonedPager(map,pager);
        pager.result(ttList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    @RequestMapping(value = "previewTenderToned",method = RequestMethod.GET)
    @ApiOperation(value="运营管理平台预览招中标信息",notes = "运营管理平台预览",response = JsonResult.class)
    public JsonResult previewTenderToned(@ApiParam(value = "招中标信息ID") @RequestParam Long tenderTonedID) throws Exception {
        JsonResult jsonResult = new JsonResult();
        TenderToned tenderToned = ttService.queryTenderToneByID(tenderTonedID);
        jsonResult.setData(tenderToned);
        return jsonResult;
    }

    @RequestMapping(value = "queryLatestTenderToned", method = RequestMethod.GET)
    @ApiOperation(value = "最新招标或中标公告信息或搜索时的推荐，默认10条",notes = "最新招标或中标公告信息，默认10条",response = JsonResult.class)
    public JsonResult queryLatestTenderToned(@ApiParam(value="公告类型 1:招标公告，2：中标公告") @RequestParam() String type) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("type",type);
        map.put("count",10);
        List<Map<String,String>> projectList = ttService.queryLatestTenderToned(map);
        jsonResult.setData(projectList);
        return jsonResult;
    }
}
