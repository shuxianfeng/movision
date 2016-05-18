package com.zhuhuibao.business.memCenter.PriceManage;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.AskPriceResultBean;
import com.zhuhuibao.common.AskPriceSearchBean;
import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.Config;
import com.zhuhuibao.utils.JsonUtils;
import com.zhuhuibao.utils.ResourcePropertiesUtils;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 询价报价管理
 * Created by cxx on 2016/3/29 0029.
 */
@RestController
@RequestMapping("/rest/price/")
@Api(value="Price", description="询价")
public class PriceController {
    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    @Autowired
    private UploadService uploadService;
    /**
     * 询价保存
     */
    @ApiOperation(value="询价保存",notes="询价保存",response = JsonResult.class)
    @RequestMapping(value = "saveAskPrice", method = RequestMethod.POST)
    public JsonResult saveAskPrice(AskPrice askPrice) throws IOException {
        JsonResult result = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        askPrice.setEndTime(askPrice.getEndTime()+" 23:59:59");

        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                askPrice.setCreateid(principal.getId().toString());
                result = priceService.saveAskPrice(askPrice);
            }else{
                result.setCode(401);
                result.setMessage("请先登录");
            }
        }else{
            result.setCode(401);
            result.setMessage("请先登录");
        }
        return result;
    }

    /**
     * 上传询价单（定向，公开），上传报价单
     */
    @ApiOperation(value="上传询价单（定向，公开），上传报价单",notes="上传询价单（定向，公开），上传报价单",response = JsonResult.class)
    @RequestMapping(value = "uploadAskList", method = RequestMethod.POST)
    public JsonResult uploadAskList(HttpServletRequest req) throws IOException {
        JsonResult result = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session){
            String url = uploadService.upload(req,"doc");
            Map map = new HashMap();
            map.put(Constant.name,url);
            result.setData(map);
            result.setCode(200);
        }else{
            result.setCode(401);
            result.setMessage("请先登录");
        }
        return result;
    }

    /**
     * 获得我的联系方式（询报价者联系方式）
     */
    @ApiOperation(value="获得我的联系方式（询报价者联系方式）",notes="获得我的联系方式（询报价者联系方式）",response = JsonResult.class)
    @RequestMapping(value = "getLinkInfo", method = RequestMethod.GET)
    public JsonResult getLinkInfo() throws IOException {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                jsonResult = priceService.getLinkInfo(principal.getId().toString());
            }else{
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }
        return jsonResult;
    }

    /**
     * 查看具体某条询价信息
     */
    @ApiOperation(value="查看具体某条询价信息",notes="查看具体某条询价信息",response = JsonResult.class)
    @RequestMapping(value = "queryAskPriceByID", method = RequestMethod.GET)
    public JsonResult queryAskPriceByID(@RequestParam String id) throws IOException {
        JsonResult result = priceService.queryAskPriceByID(id);
        return result;
    }

    /**
     * 根据条件查询询价信息（分页）
     */
    @ApiOperation(value="根据条件查询询价信息（分页）",notes="根据条件查询询价信息（分页）",response = JsonResult.class)
    @RequestMapping(value = "queryAskPriceInfo", method = RequestMethod.GET)
    public JsonResult queryAskPriceInfo(AskPriceSearchBean askPriceSearch,@RequestParam(required = false) String pageNo,@RequestParam(required = false) String pageSize) throws IOException {
        /*String title = new String(askPriceSearch.getTitle().getBytes("8859_1"), "utf8" );
        askPriceSearch.setTitle(title);*/
        JsonResult jsonResult = new JsonResult();
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
                jsonResult.setCode(401);
                jsonResult.setMessage("请先登录");
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }

        askPriceSearch.setEndTime(askPriceSearch.getEndTime()+" 23:59:59");
        Paging<AskPriceResultBean> pager = new Paging<AskPriceResultBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<AskPriceResultBean> askPriceList = priceService.findAllByPager(pager,askPriceSearch);
        pager.result(askPriceList);
        jsonResult.setData(pager);
        return jsonResult;
    }

    /**
     * 最新公开询价(限六条)
     */
    @ApiOperation(value="最新公开询价(限六条)",notes="最新公开询价(限六条)",response = JsonResult.class)
    @RequestMapping(value = "queryNewPriceInfo", method = RequestMethod.GET)
    public JsonResult queryNewPriceInfo() throws IOException {
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
        JsonResult jsonResult = priceService.queryNewPriceInfo(6,askPriceSearch);
        return jsonResult;
    }

    /**
     * 最新公开询价(分页)
     */
    @ApiOperation(value="最新公开询价(分页)",notes="最新公开询价(分页)",response = JsonResult.class)
    @RequestMapping(value = "queryNewPriceInfoList", method = RequestMethod.GET)
    public JsonResult queryNewPriceInfoList(@RequestParam(required = false) String pageNo,@RequestParam(required = false) String pageSize) throws IOException {
        JsonResult jsonResult = new JsonResult();
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
        jsonResult.setCode(200);
        jsonResult.setData(pager);
        return jsonResult;
    }
}
