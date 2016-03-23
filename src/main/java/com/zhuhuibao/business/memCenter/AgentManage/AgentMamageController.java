package com.zhuhuibao.business.memCenter.AgentManage;

import com.zhuhuibao.common.AccountBean;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SysBean;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
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
 * Created by cxx on 2016/3/22 0022.
 */
@RestController
public class AgentMamageController {
    private static final Logger log = LoggerFactory
            .getLogger(AgentMamageController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private MemberService memberService;

    /**
     * 根据品牌查询它所属的大系统，子系统
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/category", method = RequestMethod.GET)
    public void category(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = new JsonResult();
        List list = new ArrayList();
        try{
            List<ResultBean> resultBeanList = categoryService.findSystemByBrand(id);
            List<SysBean> sysBeanList = categoryService.findCategoryByBrand(id);
            for(int i=0;i<resultBeanList.size();i++){
                ResultBean resultBean = resultBeanList.get(i);
                Map map = new HashMap();
                List list1 = new ArrayList();
                map.put("id",resultBean.getCode());
                map.put("name",resultBean.getName());
                for(int y=0;y<sysBeanList.size();y++){
                    SysBean sysBean = sysBeanList.get(y);
                    Map map1 = new HashMap();
                    if(resultBean.getCode().equals(sysBean.getId())){
                        map1.put("id",sysBean.getCode());
                        map1.put("name",sysBean.getSubSystemName());
                        list1.add(map1);
                    }
                }
                map.put("subSys",list1);
                list.add(map);
            }
        }catch (Exception e){
            log.error("query error!");
        }
        result.setCode(200);
        result.setData(list);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据会员id查询其品牌
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/brand", method = RequestMethod.GET)
    public void brand(HttpServletRequest req, HttpServletResponse response,Brand brand) throws IOException {
        JsonResult result = new JsonResult();
        List list = new ArrayList();
        try{
            List<Brand> brands = brandService.searchBrandByStatus(brand);
            for(int i=0;i<brands.size();i++){
                Brand brand1 = brands.get(i);
                Map map = new HashMap();
                map.put("id",brand1.getId());
                map.put("name",brand1.getCNName());
                list.add(map);
            }
        }catch (Exception e){
            log.error("search brand by memId error!");
        }
        result.setCode(200);
        result.setData(list);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据账号，公司名称查询代理商会员
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/searchAgent", method = RequestMethod.GET)
    public void searchAgent(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        String account = req.getParameter("account");
        try{
            List<AccountBean> memList = memberService.findAgentMember(account);
            result.setCode(200);
            result.setData(memList);
        }catch (Exception e){
            log.error("query Agent error!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}
