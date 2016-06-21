package com.zhuhuibao.business.common;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.advertising.entity.SysAdvertising;
import com.zhuhuibao.mybatis.advertising.service.SysAdvertisingService;
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
 *
 * 广告
 * @author jianglz
 * @since 16/6/20.
 */
@RestController
@RequestMapping("/rest/common/adv")
public class AdvertisingController {
    private static final Logger log = LoggerFactory.getLogger(AdvertisingController.class);

    @Autowired
    private SysAdvertisingService advService;


    @ApiOperation(value = "首页广告区域信息展示", notes = "首页广告区域信息展示")
    @RequestMapping(value = "sel_advinfo", method = RequestMethod.GET)
    public Response getAdvInfo(@ApiParam(value="频道类型 1:平台主站  2：工程商 ，3：商城，4：项目，5：威客，6：人才，7：会展，8：技术，9：专家")
                                   @RequestParam String chanType,
                                   @ApiParam(value="频道下子页面.index:首页;") @RequestParam String page,
                                   @ApiParam(value="广告所在区域:F1:一楼") @RequestParam String advArea) {
        log.debug("广告区域信息展示->频道类型:{},频道子页面:{},广告所在区域:{}", new Object[]{chanType, page, advArea});

        List<SysAdvertising> advertisings = advService.findListByCondition(chanType,page,advArea);

        List<Map<String,String>> mapList = new ArrayList<>();
        for(SysAdvertising advertising : advertisings){
            Map<String,String> map = new HashMap<>();
            map.put("title",advertising.getTitle());
            map.put("imgUrl",advertising.getImgUrl());
            map.put("linkUrl",advertising.getLinkUrl());
            mapList.add(map);
        }

        return new Response(mapList);
    }

}
