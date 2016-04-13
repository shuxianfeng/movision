package com.zhuhuibao.business.memCenter.PriceManage;

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
        JsonResult result = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);

        if(null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                askPrice.setCreateid(principal.getId().toString());
                askPrice.setBillurl(ResourcePropertiesUtils.getValue("host.ip")+ "/uploadDoc/price/" + askPrice.getBillurl());
                result = priceService.saveAskPrice(askPrice);
            }
        }else{
            result.setCode(401);
            result.setMessage("请先登录");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据品牌id查询代理商跟厂商（区域分组）
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/getAgentByBrandid", method = RequestMethod.GET)
    public void getAgentByBrandid(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = priceService.getAgentByBrandid(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 上传询价单（定向，公开），上传报价单
     */
    @RequestMapping(value = "/rest/price/uploadAskList", method = RequestMethod.POST)
    public void uploadAskList(HttpServletRequest req, HttpServletResponse response) throws IOException {
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

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 获得我的联系方式（询报价者联系方式）
     */
    @RequestMapping(value = "/rest/price/getLinkInfo", method = RequestMethod.GET)
    public void getLinkInfo(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult jsonResult = new JsonResult();
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if(null != session)
        {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser)session.getAttribute("member");
            if(null != principal){
                jsonResult = priceService.getLinkInfo(principal.getId().toString());
            }
        }else{
            jsonResult.setCode(401);
            jsonResult.setMessage("请先登录");
        }

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
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
    public void queryAskPriceInfo(HttpServletRequest req, HttpServletResponse response, AskPriceSearchBean askPriceSearch,String pageNo,String pageSize) throws IOException {
        /*String title = new String(askPriceSearch.getTitle().getBytes("8859_1"), "utf8" );
        askPriceSearch.setTitle(title);*/
        JsonResult jsonResult = new JsonResult();
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<AskPriceResultBean> pager = new Paging<AskPriceResultBean>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        List<AskPriceResultBean> askPriceList = priceService.findAllByPager(pager,askPriceSearch);
        pager.result(askPriceList);
        jsonResult.setData(pager);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(jsonResult));
    }
}
