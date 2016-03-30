package com.zhuhuibao.business.memCenter.PriceManage;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.service.AgentService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
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
import java.util.ArrayList;
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
    private MemberService memberService;

    @Autowired
    private AgentService agentService;

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
    @RequestMapping(value = "/rest/price/getProductProxyInfo", method = RequestMethod.GET)
    public void getProductProxyInfo(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        List<ResultBean> provinceList = memberService.findProvince();
        List<ResultBean> agentList = agentService.findAgent();
        List list = new ArrayList();
        for(int i=0;i<provinceList.size();i++){
            ResultBean province = provinceList.get(i);
            Map map1 = new HashMap();
            map1.put("id",province.getCode());
            map1.put("name",province.getName());
            List list1 = new ArrayList();
            for(int j=0;j<agentList.size();j++){
                ResultBean agent = agentList.get(j);
                Map map2 = new HashMap();
                if(agent.getSmallIcon().contains(province.getSmallIcon())){
                    map2.put("id",agent.getCode());
                    map2.put("name",agent.getName());
                    list1.add(map2);
                }
            }
            map1.put("agentList",list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);
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
}
