package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.CheckBrand;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zhuhuibao.service.MobileBrandService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 品牌controller
 * 
 * @author tongxinglong
 * @date 2016/10/20 0020.
 */
@RestController
@RequestMapping("/rest/m/brand/mc/")
public class MobileBrandMcController {

    @Autowired
    private MobileBrandService mobileBrandService;

    @ApiOperation(value = "我的品牌列表", notes = "我的品牌列表", response = Response.class)
    @RequestMapping(value = "/sel_my_brand_list", method = RequestMethod.GET)
    public Response selMyBrandList(@ApiParam(value = "审核状态") @RequestParam(required = false) String status,
            @ApiParam(value = "发布时间排序，值为asc/desc") @RequestParam(required = false) String publishTimeOrder) {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            List<Map<String, Object>> brandList = mobileBrandService.getMyBrandChkList(memberId, status, publishTimeOrder);
            result.setData(brandList);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    @ApiOperation(value = "我的品牌详情", notes = "我的品牌详情", response = Response.class)
    @RequestMapping(value = "/sel_my_brand_detail", method = RequestMethod.GET)
    public Response selMyBrandDetail(@ApiParam(value = "品牌ID") @RequestParam(required = false) String brandId) {
        Response result = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            Map<String, Object> brandInfo = new HashMap<>();
            CheckBrand brand = mobileBrandService.getBrandChkById(brandId);
            List<Map<String, Object>> brandSysList = mobileBrandService.getBrandSysChkById(brandId);

            brandInfo.put("brand", brand);
            brandInfo.put("brandSysList", brandSysList);

            result.setData(brandInfo);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

}
