package com.zhuhuibao.mobile.web.mc;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.service.MobileEnquiryService;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 询报价controller
 * 
 * @author tongxinglong
 * @date 2016/10/18 0018.
 */
@RestController
@RequestMapping("/rest/m/enquiry/mc/")
public class MobileEnquiryMcController {

    @Autowired
    private MobileEnquiryService mobileEnquiryService;

    /**
     * 根据条件查询发出的询价信息（分页）
     */
    @ApiOperation(value = "根据条件查询发出的询价信息（分页）", notes = "根据条件查询发出的询价信息（分页）", response = Response.class)
    @RequestMapping(value = { "/sel_sent_enquiry_list" }, method = RequestMethod.GET)
    public Response selSentEnquiryList(@ModelAttribute AskPriceSearchBean askPriceSearch, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        askPriceSearch.setCreateid(String.valueOf(ShiroUtil.getCreateID()));

        Paging<AskPriceResultBean> enquiryPager = mobileEnquiryService.getEnquiryList(askPriceSearch, pageNo, pageSize);

        response.setData(enquiryPager);
        return response;
    }

    @ApiOperation(value = "询价需求：我收到的询价需求", notes = "询价需求：我收到的询价需求", response = Response.class)
    @RequestMapping(value = { "/sel_received_enquiry_list" }, method = RequestMethod.GET)
    public Response selReceivedEnquiryList(@ModelAttribute AskPrice price, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        Paging<AskPriceSimpleBean> pager = new Paging<AskPriceSimpleBean>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<AskPriceSimpleBean> enquiryList = mobileEnquiryService.getReceivedEnquiryList(pager, price);
        pager.result(enquiryList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "查看询价详情", notes = "查看询价详情", response = Response.class)
    @RequestMapping(value = { "/sel_enquiry_detail" }, method = RequestMethod.GET)
    public Response selEnquiryDetail(@ApiParam(value = "询价ID") @RequestParam Long askId) throws IOException {
        Response response = new Response();
        AskPriceBean askPrice = mobileEnquiryService.getAskPriceById(askId);
        response.setData(askPrice);
        return response;
    }

    @ApiOperation(value = "根据条件查询收到的报价信息（分页）", notes = "根据条件查询收到的报价信息（分页）", response = Response.class)
    @RequestMapping(value = { "/sel_received_offer_list" }, method = RequestMethod.GET)
    public Response selReceivedOfferList(@ApiParam(value = "询价ID") @RequestParam Long askId, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        Map<String, Object> resultMap = new HashMap<>();
        AskPriceBean askPrice = mobileEnquiryService.getAskPriceByAskidMemId(askId, ShiroUtil.getCreateID());
        Paging<Map<String, Object>> offerPager = null;
        if (null != askPrice) {
            offerPager = mobileEnquiryService.getOfferListByAskId(askId, pageNo, pageSize);
        }

        resultMap.put("offerPager", offerPager);
        resultMap.put("askPrice", askPrice);

        response.setData(resultMap);
        return response;
    }

    @ApiOperation(value = "发出的报价信息（分页）", notes = "发出的报价信息（分页）", response = Response.class)
    @RequestMapping(value = { "/sel_sent_offer_list" }, method = RequestMethod.GET)
    public Response selSentOfferList(@ApiParam(value = "标题") @RequestParam(required = false) String title, @ApiParam(value = "开始时间") @RequestParam(required = false) String startDate,
            @ApiParam(value = "结束时间") @RequestParam(required = false) String endDate, @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {

        Paging<AskPriceSimpleBean> offerPager = mobileEnquiryService.getSentOfferList(ShiroUtil.getCreateID(), title, startDate, endDate, pageNo, pageSize);

        return new Response(offerPager);
    }

    @ApiOperation(value = "查看报价详情", notes = "查看报价详情", response = Response.class)
    @RequestMapping(value = { "/sel_offer_detail" }, method = RequestMethod.GET)
    public Response selOfferDetail(@ApiParam(value = "报价ID") @RequestParam Long offerId) throws IOException {
        Response response = new Response();
        Map<String, Object> resultMap = new HashMap<>();

        OfferPrice offerPrice = mobileEnquiryService.getOfferPriceById(offerId);
        if (null != offerPrice && null != ShiroUtil.getCreateID()) {
            AskPriceBean askPrice = mobileEnquiryService.getAskPriceById(offerPrice.getAskid());
            resultMap.put("offerPrice", offerPrice);
            resultMap.put("askPrice", askPrice);
        }

        response.setData(resultMap);
        return response;
    }

}
