package com.zhuhuibao.business.memCenter.PriceManage;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.mybatis.memCenter.service.UploadService;
import com.zhuhuibao.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 询价报价管理
 * Created by cxx on 2016/3/29 0029.
 */
@RestController
public class PriceController {
    private static final Logger log = LoggerFactory.getLogger(PriceController.class);

    @Autowired
    private PriceService priceService;

    @Autowired
    private UploadService uploadService;
    /**
     * 询价保存
     */
    @RequestMapping(value = "/rest/price/saveAskPrice", method = RequestMethod.POST)
    public void saveAskPrice(HttpServletRequest req, HttpServletResponse response, AskPrice askPrice) throws IOException {
        JsonResult result = priceService.saveAskPrice(askPrice);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 获得代理商信息
     */
    @RequestMapping(value = "/rest/price/getProxyInfoByProvince", method = RequestMethod.GET)
    public void getProxyInfoByProvince(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = priceService.getProxyInfoByProvince();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 上传询价单（定向，公开），上传报价单
     */
    @RequestMapping(value = "/rest/price/uploadAskList", method = RequestMethod.POST)
    public void uploadAskList(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        String url = uploadService.upload(req,"doc");
        result.setData(url);
        result.setCode(200);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 获得我的联系方式（询报价者联系方式）
     */
    @RequestMapping(value = "/rest/price/getLinkInfo", method = RequestMethod.GET)
    public void getLinkInfo(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = priceService.getLinkInfo(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查看具体某条询价信息
     */
    @RequestMapping(value = "/rest/price/queryAskPriceByID", method = RequestMethod.GET)
    public void queryAskPriceByID(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = priceService.queryAskPriceByID(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据条件查询询价信息（分页）
     */
    @RequestMapping(value = "/rest/price/queryAskPriceInfo", method = RequestMethod.GET)
    public void queryAskPriceInfo(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = priceService.queryAskPriceInfo();
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}
