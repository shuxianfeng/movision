package com.zhuhuibao.mobile.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.oms.entity.TenderToned;
import com.zhuhuibao.mybatis.vip.service.VipInfoService;
import com.zhuhuibao.mybatis.zhb.entity.DictionaryZhbgoods;
import com.zhuhuibao.mybatis.zhb.entity.ZhbAccount;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.service.MobileProjectService;
import com.zhuhuibao.service.MobileSysAdvertisingService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 项目信息controller
 * 
 * @author tongxinglong
 * @date 2016/10/17 0017.
 */
@RestController
@RequestMapping("/rest/m/project/site")
public class MobileProjectController {

    @Autowired
    private MobileProjectService mobileProjectService;
    @Autowired
    private ZhbService zhbService;
    @Autowired
    private VipInfoService vipInfoService;
    @Autowired
    private MobileSysAdvertisingService advertisingService;

    /**
     * 前台项目信息列表页
     *
     * @param name
     *            项目信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = { "/sel_project_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "根据条件查询项目分页信息", notes = "根据条件查询分页", response = Response.class)
    public Response selProjectList(@ApiParam(value = "项目名称") @RequestParam(required = false) String name, @ApiParam(value = "城市Code") @RequestParam(required = false) String city,
            @ApiParam(value = "省代码") @RequestParam(required = false) String province, @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
            @ApiParam(value = "开工日期查询开始日期") @RequestParam(required = false) String startDateA, @ApiParam(value = "开工日期查询结束日期") @RequestParam(required = false) String startDateB,
            @ApiParam(value = "竣工日期查询开始日期") @RequestParam(required = false) String endDateA, @ApiParam(value = "竣工日期查询结束日期") @RequestParam(required = false) String endDateB,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        Map<String, Object> result = new HashMap<>();

        // 查询项目信息
        Paging<Map<String, String>> projectPager = mobileProjectService.getProjectListByConditions(name, city, province, category, startDateA, startDateB, endDateA, endDateB, pageNo, pageSize);
        // banner
        List<SysAdvertising> banner = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Project_Banner.value);

        result.put("projectPager", projectPager);
        result.put("banner", banner);
        return new Response(result);
    }

    @ApiOperation(value = "项目信息详情", notes = "项目信息详情", response = Response.class)
    @RequestMapping(value = "/sel_project_detail", method = RequestMethod.GET)
    public Response selProjectDetail(@ApiParam(value = "项目信息ID") @RequestParam Long projectId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        // 判断是否登录
        if (null != ShiroUtil.getCreateID()) {
            // 剩余特权数量
            long privilegeNum = vipInfoService.getExtraPrivilegeNum(ShiroUtil.getCompanyID(), ZhbConstant.ZhbGoodsType.CKXMXX.toString());
            resultMap.put("privilegeNum", String.valueOf(privilegeNum));
            // 筑慧币余额
            ZhbAccount account = zhbService.getZhbAccount(ShiroUtil.getCompanyID());
            resultMap.put("zhbAmount", null != account ? account.getAmount().toString() : "0");
        } else {
            resultMap.put("privilegeNum", "0");
            resultMap.put("zhbAmount", "0");
        }
        // 筑慧币单价
        DictionaryZhbgoods goodsConfig = zhbService.getZhbGoodsByPinyin(ZhbConstant.ZhbGoodsType.CKXMXX.toString());
        resultMap.put("zhbPrice", null != goodsConfig ? String.valueOf(goodsConfig.getPriceDoubleValue()) : "999");

        // 获取项目信息详情
        Map<String, Object> projectDetail = mobileProjectService.getProjectDetail(projectId);
        resultMap.putAll(projectDetail);

        return new Response(resultMap);
    }

    @RequestMapping(value = { "/sel_tender_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "查询招中标分页展示", notes = "根据条件查询招中标", response = Response.class)
    public Response selTenderList(@ApiParam(value = "招中标公告名称") @RequestParam(required = false) String noticeName, @ApiParam(value = "省代码") @RequestParam(required = false) String province,
            @ApiParam(value = "市代码") @RequestParam(required = false) String city, @ApiParam(value = "招中标类型：1招标，2中标") @RequestParam(required = false) String type,
            @ApiParam(value = "开工日期查询开始日期") @RequestParam(required = false) String startDateA, @ApiParam(value = "开工日期查询结束日期") @RequestParam(required = false) String startDateB,
            @ApiParam(value = "竣工日期查询开始日期") @RequestParam(required = false) String endDateA, @ApiParam(value = "竣工日期查询结束日期") @RequestParam(required = false) String endDateB,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {

        Map<String, Object> paramMap = MapUtil.convert2HashMap("type", type, "province", province, "city", city, "startDateA", startDateA, "startDateB", startDateB, "endDateA", endDateA, "endDateB",
                endDateB);
        if (noticeName != null && !"".equals(noticeName)) {
            paramMap.put("noticeName", noticeName.replace("_", "\\_"));
        }
        Paging<Map<String, Object>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        // 招中标数据
        List<Map<String, Object>> tenderList = mobileProjectService.getTenderList(paramMap, pager);

        // banner
        List<SysAdvertising> banner = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Project_Banner.value);

        return new Response(MapUtil.convert2HashMap("tenderList", tenderList, "banner", banner));
    }

    @RequestMapping(value = { "/sel_tender_detail" }, method = RequestMethod.GET)
    @ApiOperation(value = "频道预览招中标信息", notes = "频道预览招中标信息", response = Response.class)
    public Response selTenderDetail(@ApiParam(value = "招中标信息ID") @RequestParam Long tenderID) throws Exception {
        TenderToned tenderToned = mobileProjectService.getTenderDetail(tenderID);
        return new Response(tenderToned);
    }

}