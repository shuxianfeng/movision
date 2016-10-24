package com.zhuhuibao.mobile.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.AdvertisingConstant;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.service.MobileExhibitionService;
import com.zhuhuibao.service.MobileSysAdvertisingService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 
 * @author tongxinglong
 * @date 2016/10/20 0020.
 */
@RestController
@RequestMapping("/rest/m/exhibition/site/")
public class MobileExhibitionController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // 字符串自动trim，且空字符串转为null
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @Autowired
    private MobileExhibitionService mobileExhibitionService;
    @Autowired
    private MobileSysAdvertisingService advertisingService;

    @ApiOperation(value = "会展信息列表", notes = "会展信息列表", response = Response.class)
    @RequestMapping(value = "sel_list", method = RequestMethod.GET)
    public Response selList(@ApiParam(value = "所属栏目:1:筑慧活动;2:行业会议;3:厂商活动") @RequestParam(required = false) String type,
            @ApiParam(value = "筑慧活动子栏目:1:技术研讨会;2:产品发布会;3:行业峰会;4:市场活动会") @RequestParam(required = false) String subType, @ApiParam(value = "省") @RequestParam(required = false) String province,
            @ApiParam(value = "市") @RequestParam(required = false) String city, @ApiParam(value = "发布时间查询的开始时间") @RequestParam(required = false) String publishTimeA,
            @ApiParam(value = "发布时间查询的结束时间") @RequestParam(required = false) String publishTimeB, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String, Object> paramMap = MapUtil.convert2HashMap("subType", subType, "type", type, "province", province, "city", city, "publishTimeA", publishTimeA, "publishTimeB", publishTimeB,
                "type1", 1);

        // 会展信息
        List<Map<String, String>> exhibitionList = mobileExhibitionService.getExhibitionList(pager, paramMap);
        pager.result(exhibitionList);
        // banner
        List<SysAdvertising> banner = advertisingService.queryAdvertising(AdvertisingConstant.AdvertisingPosition.M_Activity_Banner.value);

        return new Response(MapUtil.convert2HashMap("exhibitionList", pager, "banner", banner));
    }

    @ApiOperation(value = "会展详情查看", notes = "会展详情查看", response = Response.class)
    @RequestMapping(value = "sel_detail", method = RequestMethod.GET)
    public Response exhibitionInfo(@ApiParam(value = "信息ID") @RequestParam String exhibitionId) {

        Exhibition exhibition = mobileExhibitionService.getExhibitionById(exhibitionId);

        // 点击率+1
        exhibition.setViews(String.valueOf(Integer.parseInt(exhibition.getViews()) + 1));
        mobileExhibitionService.updateExhibitionViews(exhibition);

        return new Response(exhibition);

    }
}
