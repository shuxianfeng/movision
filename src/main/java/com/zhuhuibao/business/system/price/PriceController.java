package com.zhuhuibao.business.system.price;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询价报价管理
 * Created by cxx on 2016/3/29 0029.
 */
@RestController
@Api(value="Price", description="询价")
public class PriceController {
    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    @Autowired
    private UploadService uploadService;

    @Autowired
    ZhbOssClient zhbOssClient;
    /**
     * 询价保存
     */
    @ApiOperation(value="询价保存",notes="询价保存",response = Response.class)
    @RequestMapping(value = {"/rest/price/saveAskPrice","/rest/system/mc/enquiry/add_enquiry"}, method = RequestMethod.POST)
    public Response saveAskPrice(AskPrice askPrice) throws IOException {
        Response result = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        askPrice.setEndTime(askPrice.getEndTime()+" 23:59:59");

        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                askPrice.setCreateid(principal.getId().toString());
                result = priceService.saveAskPrice(askPrice);
            }else{
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    /**
     * 上传询价单（定向，公开），上传报价单
     */
    @ApiOperation(value="上传询价单（定向，公开），上传报价单",notes="上传询价单（定向，公开），上传报价单",response = Response.class)
    @RequestMapping(value = {"/rest/price/uploadAskList","/rest/system/mc/enquiry/upload_enquiryList"}, method = RequestMethod.POST)
    public Response uploadAskList(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        Response result = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
            String url = zhbOssClient.uploadObject(file,"doc","price");
            Map map = new HashMap();
            map.put(Constants.name,url);
            result.setData(map);
            result.setCode(200);
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return result;
    }

    /**
     * 获得我的联系方式（询报价者联系方式）
     */
    @ApiOperation(value="获得我的联系方式（询报价者联系方式）",notes="获得我的联系方式（询报价者联系方式）",response = Response.class)
    @RequestMapping(value = {"/rest/price/getLinkInfo","/rest/system/mc/enquiry/sel_linkMan_info"}, method = RequestMethod.GET)
    public Response getLinkInfo() throws IOException {
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                response = priceService.getLinkInfo(principal.getId().toString());
            }else{
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    /**
     * 查看具体某条询价信息
     */
    @ApiOperation(value="查看具体某条询价信息",notes="查看具体某条询价信息",response = Response.class)
    @RequestMapping(value = {"/rest/price/queryAskPriceByID","/rest/system/mc/enquiry/sel_enquiry"}, method = RequestMethod.GET)
    public Response queryAskPriceByID(@RequestParam String id) throws IOException {
        Response result = priceService.queryAskPriceByID(id);
        return result;
    }

    /**
     * 根据条件查询询价信息（分页）
     */
    @ApiOperation(value="根据条件查询询价信息（分页）",notes="根据条件查询询价信息（分页）",response = Response.class)
    @RequestMapping(value = {"/rest/price/queryAskPriceInfo","/rest/system/mc/enquiry/sel_enquiryList"}, method = RequestMethod.GET)
    public Response queryAskPriceInfo(AskPriceSearchBean askPriceSearch, @RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        /*String title = new String(askPriceSearch.getTitle().getBytes("8859_1"), "utf8" );
        askPriceSearch.setTitle(title);*/
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        if(askPriceSearch.getTitle()!=null){
            if(askPriceSearch.getTitle().contains("_")){
                askPriceSearch.setTitle(askPriceSearch.getTitle().replace("_","\\_"));
            }
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                askPriceSearch.setCreateid(principal.getId().toString());
            }else{
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        askPriceSearch.setEndTime(askPriceSearch.getEndTime()+" 23:59:59");
        Paging<AskPriceResultBean> pager = new Paging<AskPriceResultBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<AskPriceResultBean> askPriceList = priceService.findAllByPager(pager,askPriceSearch);
        pager.result(askPriceList);
        response.setData(pager);
        return response;
    }

    /**
     * 最新公开询价(限六条)
     */
    @ApiOperation(value="最新公开询价(限六条)",notes="最新公开询价(限六条)",response = Response.class)
    @RequestMapping(value = {"/rest/price/queryNewPriceInfo","/rest/system/site/enquiry/sel_new_open_enquiryList_count"}, method = RequestMethod.GET)
    public Response queryNewPriceInfo() throws IOException {
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        AskPriceSearchBean askPriceSearch = new AskPriceSearchBean();
        String createid = "";
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                createid = principal.getId().toString();
            }
        }
        Response response = priceService.queryNewPriceInfo(6,createid);
        return response;
    }

    /**
     * 最新公开询价(分页)
     */
    @ApiOperation(value="最新公开询价(分页)",notes="最新公开询价(分页)",response = Response.class)
    @RequestMapping(value = {"/rest/price/queryNewPriceInfoList","/rest/system/mc/enquiry/sel_new_open_enquiryList"}, method = RequestMethod.GET)
    public Response queryNewPriceInfoList(@RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) throws IOException {
        Response response = new Response();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        AskPriceSearchBean askPriceSearch = new AskPriceSearchBean();
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                askPriceSearch.setCreateid(principal.getId().toString());
            }
        }
        Paging<AskPrice> pager = new Paging<AskPrice>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<AskPrice> askPriceList = priceService.queryNewPriceInfoList(pager,askPriceSearch);
        pager.result(askPriceList);
        response.setCode(200);
        response.setData(pager);
        return response;
    }
}
