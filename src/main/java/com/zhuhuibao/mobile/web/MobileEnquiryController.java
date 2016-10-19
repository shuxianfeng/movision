package com.zhuhuibao.mobile.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.pojo.AskPriceBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.service.MobileEnquiryService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 询报价controller
 * 
 * @author tongxinglong
 * @date 2016/10/18 0018.
 */
@RestController
@RequestMapping("/rest/m/enquiry/")
public class MobileEnquiryController {

    @Autowired
    private MobileEnquiryService mobileEnquiryService;

    /**
     * 根据条件查询询价信息（分页）
     */
    @LoginAccess
    @ApiOperation(value = "根据条件查询询价信息（分页）", notes = "根据条件查询询价信息（分页）", response = Response.class)
    @RequestMapping(value = { "/mc/sel_enquiry_list" }, method = RequestMethod.GET)
    public Response selEnquiryList(AskPriceSearchBean askPriceSearch, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        askPriceSearch.setCreateid(String.valueOf(ShiroUtil.getCreateID()));

        Paging<AskPriceResultBean> enquiryPager = mobileEnquiryService.getEnquiryList(askPriceSearch, pageNo, pageSize);

        response.setData(enquiryPager);
        return response;
    }

    @LoginAccess
    @ApiOperation(value = "根据条件查询指定询价的报价信息（分页）", notes = "根据条件查询指定询价的报价信息（分页）", response = Response.class)
    @RequestMapping(value = { "/mc/sel_offer_list" }, method = RequestMethod.GET)
    public Response selEnquiryList(@ApiParam(value = "询价ID") @RequestParam Long askId, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        Map<String, Object> resultMap = new HashMap<>();
        Paging<Map<String, Object>> offerPager = mobileEnquiryService.getOfferListByAskId(askId, pageNo, pageSize);
        AskPriceBean askPrice = mobileEnquiryService.getAskPriceById(askId, ShiroUtil.getCreateID());

        resultMap.put("offerPager", offerPager);
        resultMap.put("askPrice", askPrice);

        response.setData(resultMap);
        return response;
    }

}
