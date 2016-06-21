package com.zhuhuibao.business.mall;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 筑慧商城首页
 * @author jianglz
 * @since 16/6/20.
 */
@RestController
@RequestMapping("/rest/mall/site")
@Api(value = "Mall-Index", description = "筑慧商城首页")
public class MallIndexController {
    private static final Logger log = LoggerFactory.getLogger(MallIndexController.class);


    @Autowired
    private SysAdvertisingService advService;

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "首页广告区域信息展示", notes = "首页广告区域信息展示")
    @RequestMapping(value = "indexfloor", method = RequestMethod.GET)
    public Response getIndexAdvertising(@ApiParam(value="频道类型 1:平台主站  2：工程商 ，3：商城，4：项目，5：威客，6：人才，7：会展，8：技术，9：专家")
                                   @RequestParam String chanType,
                                   @ApiParam(value="频道下子页面.index:首页;") @RequestParam String page,
                                   @ApiParam(value="广告所在区域:F1:一楼") @RequestParam String advArea){
        log.debug("广告区域信息展示->频道类型:{},频道子页面:{},广告所在区域:{}",new Object[] {chanType,page,advArea});
        Map<String,Object> result = new HashMap<>();

        List<Map<String,String>> comList = new ArrayList<>();
        List<Map<String,String>> brandList = new ArrayList<>();
        List<Map<String,String>> otherList = new ArrayList<>();

        List<SysAdvertising> advertisings = advService.findListByCondition(chanType,page,advArea);
        for(SysAdvertising advertising : advertisings){
            Map<String,String> comMap = new HashMap<>();
            Map<String,String> brandMap = new HashMap<>();
            Map<String,String> otherMap = new HashMap<>();

            switch (advertising.getAdvType()) {
                case "company":
                    comMap.put("imgUrl", advertising.getImgUrl());
                    comMap.put("linkUrl", advertising.getLinkUrl());
                    comMap.put("title", advertising.getTitle());
                    String id = advertising.getConnectedId();  //公司ID

                    comMap.put("comId", id);
                    Member member = memberService.findMemById(id);
                    comMap.put("comName", member.getEnterpriseName());
                    comList.add(comMap);
                    break;
                case "brand":
                    brandMap.put("imgUrl", advertising.getImgUrl());
                    brandMap.put("linkUrl", advertising.getLinkUrl());
                    brandMap.put("title", advertising.getTitle());
                    brandList.add(brandMap);
                    break;
                case "other":
                    otherMap.put("imgUrl", advertising.getImgUrl());
                    otherMap.put("linkUrl", advertising.getLinkUrl());
                    otherMap.put("title", advertising.getTitle());
                    otherList.add(otherMap);
                    break;
            }
        }

        result.put("company",comList);
        result.put("brand",brandList);
        result.put("other",otherList);

        return  new Response(result);
    }


    @ApiOperation(value = "品牌馆广告区域信息展示", notes = "品牌馆广告区域信息展示")
    @RequestMapping(value = "brandfloor", method = RequestMethod.GET)
    public Response getBrandAdvertising(@ApiParam(value="频道类型 1:平台主站  2：工程商 ，3：商城，4：项目，5：威客，6：人才，7：会展，8：技术，9：专家")
                                   @RequestParam String chanType,
                                   @ApiParam(value="频道下子页面.index:首页;") @RequestParam String page,
                                   @ApiParam(value="广告所在区域:F1:一楼") @RequestParam String advArea){
        log.debug("广告区域信息展示->频道类型:{},频道子页面:{},广告所在区域:{}",new Object[] {chanType,page,advArea});
        Map<String,Object> result = new HashMap<>();

        List<Map<String,String>> brandList = new ArrayList<>();
        List<Map<String,String>> otherList = new ArrayList<>();

        List<SysAdvertising> advertisings = advService.findListByCondition(chanType,page,advArea);
        for(SysAdvertising advertising : advertisings){
            Map<String,String> brandMap = new HashMap<>();
            Map<String,String> otherMap = new HashMap<>();

            switch (advertising.getAdvType()) {

                case "brand":
                    brandMap.put("imgUrl", advertising.getImgUrl());
                    brandMap.put("linkUrl", advertising.getLinkUrl());
                    brandMap.put("title", advertising.getTitle());
                    brandList.add(brandMap);
                    break;
                case "other":
                    otherMap.put("imgUrl", advertising.getImgUrl());
                    otherMap.put("linkUrl", advertising.getLinkUrl());
                    otherMap.put("title", advertising.getTitle());
                    otherList.add(otherMap);
                    break;
            }
        }

        result.put("brand",brandList);
        result.put("other",otherList);

        return  new Response(result);
    }
}
