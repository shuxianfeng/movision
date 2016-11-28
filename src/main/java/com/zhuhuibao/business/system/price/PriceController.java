package com.zhuhuibao.business.system.price;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询价报价管理
 * Created by cxx on 2016/3/29 0029.
 */
@RestController
@Api(value = "Price", description = "询价")
public class PriceController {

    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    @Autowired
    ZhbOssClient zhbOssClient;

    @LoginAccess
    @ApiOperation(value = "询价保存", notes = "询价保存", response = Response.class)
    @RequestMapping(value = {"/rest/price/saveAskPrice", "/rest/system/mc/enquiry/add_enquiry"}, method = RequestMethod.POST)
    public Response saveAskPrice(@ModelAttribute AskPrice askPrice) throws Exception {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        askPrice.setEndTime(askPrice.getEndTime() + " 23:59:59");

        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        askPrice.setCreateid(principal.getId().toString());

        return priceService.saveAskPrice(askPrice);
    }

    @LoginAccess
    @ApiOperation(value = "上传询价单（定向，公开），上传报价单", notes = "上传询价单（定向，公开），上传报价单", response = Response.class)
    @RequestMapping(value = {"/rest/price/uploadAskList", "/rest/system/mc/enquiry/upload_enquiryList"}, method = RequestMethod.POST)
    public Response uploadAskList(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String url = zhbOssClient.uploadObject(file, "doc", "price");
        Map<String, String> map = new HashMap<>();
        map.put(Constants.name, url);

        return new Response(map);
    }

    @LoginAccess
    @ApiOperation(value = "获得我的联系方式（询报价者联系方式）", notes = "获得我的联系方式（询报价者联系方式）", response = Response.class)
    @RequestMapping(value = {"/rest/price/getLinkInfo", "/rest/system/mc/enquiry/sel_linkMan_info"}, method = RequestMethod.GET)
    public Response getLinkInfo() throws IOException {
        Map<String, String> map;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);

        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        map = priceService.getLinkInfo(principal.getId().toString());

        return new Response(map);
    }

    @ApiOperation(value = "查看具体某条询价信息", notes = "查看具体某条询价信息", response = Response.class)
    @RequestMapping(value = {"/rest/price/queryAskPriceByID", "/rest/system/mc/enquiry/sel_enquiry"}, method = RequestMethod.GET)
    public Response queryAskPriceByID(@RequestParam String id) {
        Response response = new Response();
        AskPriceBean bean = priceService.queryAskPriceByID(id);
        Long memberid = ShiroUtil.getCompanyID();
        if (!String.valueOf(memberid).equals(bean.getCreateid()) && "结束".equals(bean.getStatusName())) {
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        } else {
            response.setData(bean);
        }
        return response;
    }

    @LoginAccess
    @ApiOperation(value = "根据条件查询询价信息（分页）", notes = "根据条件查询询价信息（分页）", response = Response.class)
    @RequestMapping(value = {"/rest/price/queryAskPriceInfo", "/rest/system/mc/enquiry/sel_enquiryList"}, method = RequestMethod.GET)
    public Response queryAskPriceInfo(AskPriceSearchBean askPriceSearch,
                                      @RequestParam(required = false, defaultValue = "1") String pageNo,
                                      @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();

        if (askPriceSearch.getTitle() != null) {
            if (askPriceSearch.getTitle().contains("_")) {
                askPriceSearch.setTitle(askPriceSearch.getTitle().replace("_", "\\_"));
            }
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        askPriceSearch.setCreateid(principal.getId().toString());

        askPriceSearch.setEndTime(askPriceSearch.getEndTime() + " 23:59:59");
        Paging<AskPriceResultBean> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<AskPriceResultBean> askPriceList = priceService.findAllByPager(pager, askPriceSearch);
        pager.result(askPriceList);
        response.setData(pager);
        return response;
    }

    @ApiOperation(value = "最新公开询价(限六条)", notes = "最新公开询价(限六条)", response = Response.class)
    @RequestMapping(value = {"/rest/price/queryNewPriceInfo", "/rest/system/site/enquiry/sel_new_open_enquiryList_count"}, method = RequestMethod.GET)
    public Response queryNewPriceInfo() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        String createid = "";
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                createid = principal.getId().toString();
            }
        }
        return priceService.queryNewPriceInfo(6, createid);
    }

    @ApiOperation(value = "最新公开询价(分页)", notes = "最新公开询价(分页)", response = Response.class)
    @RequestMapping(value = {"/rest/price/queryNewPriceInfoList", "/rest/system/mc/enquiry/sel_new_open_enquiryList"}, method = RequestMethod.GET)
    public Response queryNewPriceInfoList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                          @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {

        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        AskPriceSearchBean askPriceSearch = new AskPriceSearchBean();
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                askPriceSearch.setCreateid(principal.getId().toString());
            }
        }
        Paging<AskPrice> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<AskPrice> askPriceList = priceService.queryNewPriceInfoList(pager, askPriceSearch);
        pager.result(askPriceList);
        return new Response(pager);
    }
}
