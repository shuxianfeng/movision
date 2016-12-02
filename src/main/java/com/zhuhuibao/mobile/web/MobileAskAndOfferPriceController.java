package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.aop.LoginAccess;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PriceConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.service.AgentService;
import com.zhuhuibao.mybatis.memCenter.service.OfferPriceService;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.service.MobileAskAndOfferPriceService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.file.FileUtil;
import com.zhuhuibao.utils.oss.ZhbOssClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IDEA User: zhuangyuhao Date: 2016/11/24 Time: 13:48
 */
@RestController
@Api(value = "Price", description = "询报价")
public class MobileAskAndOfferPriceController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MobileAskAndOfferPriceController.class);

    @Autowired
    private MobileAskAndOfferPriceService mAskPriceSV;

    @Autowired
    private AgentService agentService;

    @Autowired
    ZhbOssClient zhbOssClient;

    @Autowired
    private PriceService priceService;

    @Resource
    private OfferPriceService offerService;

    @Autowired
    FileUtil fileUtil;

    @LoginAccess
    @ApiOperation(value = "询价保存", notes = "询价保存", response = Response.class)
    @RequestMapping(value = "/rest/m/askprice/site/saveAskPrice", method = RequestMethod.POST)
    public Response saveAskPrice(@ApiParam @ModelAttribute AskPrice askPrice) throws Exception {

        mAskPriceSV.saveAskPrice(mAskPriceSV.prepareAskPrice(askPrice));
        return new Response();
    }

    @ApiOperation(value = "根据产品id查询代理商跟厂商（区域分组）", notes = "根据产品id查询代理商跟厂商（区域分组）", response = Response.class)
    @RequestMapping(value = "/rest/m/askprice/site/getAgentByProId", method = RequestMethod.GET)
    public Response getAgentByProId(@ApiParam(value = "产品的ID") @RequestParam String id) {
        Response response = new Response();
        response.setData(agentService.getAgentByProId(id));
        return response;
    }

    @ApiOperation(value = "向他询价（询价前的准备）", notes = "向他询价（询价前的准备）", response = Response.class)
    @RequestMapping(value = "/rest/m/askprice/site/prepareAskPrice", method = RequestMethod.GET)
    public Response isExistSupplierOrManuf() throws Exception {
        Response response = new Response();
        Map<String, Object> result = new HashMap();

        // result.put("isExistSupOrManu",mAskPriceSV.isExistSupOrManu(suppliers,manuf,type));
        getPrivilegeGoodsDetails(result, null, ZhbConstant.ZhbGoodsType.XJFB);
        response.setData(result);

        return response;
    }

    @LoginAccess
    @ApiOperation(value = "上传询价单，上传报价单", notes = "上传询价单，上传报价单", response = Response.class)
    @RequestMapping(value = { "/rest/m/askprice/site/uploadAskList", "/rest/m/askprice/mc/uploadAskList" }, method = RequestMethod.POST)
    public Response uploadAskList(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        String url = zhbOssClient.uploadObject(file, "img", "price");
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        return new Response(map);
    }

    @LoginAccess
    @ApiOperation(value = "获得我的联系方式（询报价者联系方式）", notes = "获得我的联系方式（询报价者联系方式）", response = Response.class)
    @RequestMapping(value = { "/rest/m/askprice/getLinkInfo" }, method = RequestMethod.GET)
    public Response getLinkInfo() throws IOException {

        return new Response(mAskPriceSV.getMyLink());
    }

    @ApiOperation(value = "查看具体某条询价信息", notes = "查看具体某条询价信息", response = Response.class)
    @RequestMapping(value = "/rest/m/askprice/mc/queryAskPriceByID", method = RequestMethod.GET)
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

    @ApiOperation(value = "下载报价单，询价单", notes = "下载报价单，询价单", response = Response.class)
    @RequestMapping(value = "/rest/m/askprice/mc/downloadBill", method = RequestMethod.GET)
    public Response downloadBill(HttpServletResponse response, @RequestParam Long id, @ApiParam(value = "类型 1:询价单，2：报价单") @RequestParam String type) throws IOException {
        Response res = new Response();
        log.debug("query offer priece info by id ");
        try {
            String fileurl = offerService.downloadBill(id, type);
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Content-disposition", "attachment;filename=" + fileurl);
            response.setContentType("application/octet-stream");
            res = fileUtil.downloadObject(response, fileurl, "doc", "price");
        } catch (Exception e) {
            log.error("download bill error! ", e);
        }
        return res;
    }

    @ApiOperation(value = "提交报价", notes = "提交报价", response = Response.class)
    @RequestMapping(value = { "/rest/m/askprice/mc/addOfferPrice" }, method = RequestMethod.POST)
    public Response addOfferPrice(@ApiParam @ModelAttribute OfferPrice price) throws IOException {
        log.info("add offer price");
        Response response = new Response();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (session != null) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            price.setCreateid(new Long(principal.getId()));
            response = offerService.addOfferPrice(price);
        }
        return response;
    }

    @ApiOperation(value = "公开，定向，单一产品报价查询", notes = "公开，定向，单一产品报价查询", response = Response.class)
    @RequestMapping(value = { "/rest/m/price/mc/sel_offerPriceInfoByID" }, method = RequestMethod.GET)
    public Response queryOfferPriceInfoByID(@RequestParam Long id) throws IOException {
        log.debug("query offer priece info by id ");
        Response response = offerService.queryOfferPriceInfoByID(id);
        return response;
    }

    /**
     * 查看我的询价单中的别人回复的报价 列表
     */
    @LoginAccess
    @ApiOperation(value = "查看我的询价单中的别人回复的报价列表（分页）", notes = "查看我的询价单中的别人回复的报价列表（分页）", response = Response.class)
    @RequestMapping(value = { "/rest/m/price/mc/queryAskPriceInfoList" }, method = RequestMethod.GET)
    public Response queryAskPriceInfo(AskPriceSearchBean askPriceSearch, @RequestParam(required = false, defaultValue = "1") String pageNo,
            @RequestParam(required = false, defaultValue = "10") String pageSize) throws IOException {
        Response response = new Response();
        response.setData(mAskPriceSV.getPager4ViewOtherOfferPrice(askPriceSearch, pageNo, pageSize));
        return response;
    }

    @ApiOperation(value = "查看我的询价单中的别人回复的报价详情", notes = "查看我的询价单中的别人回复的报价详情", response = Response.class)
    @RequestMapping(value = { "/rest/m/price/mc/queryOfferPriceByID" }, method = RequestMethod.GET)
    public Response queryOfferPriceByID(@RequestParam Long id) throws IOException {
        log.debug("query offer priece info by id ");
        return offerService.queryOfferPriceByID(id);
    }

}
