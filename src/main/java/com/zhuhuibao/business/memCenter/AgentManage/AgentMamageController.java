package com.zhuhuibao.business.memCenter.AgentManage;

import com.zhuhuibao.common.AccountBean;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SysBean;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.AgentService;
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
 * 代理商管理
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

    @Autowired
    private AgentService agentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private DictionaryService ds;
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

    /**
     * 关联代理商保存
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/agentSave", method = RequestMethod.POST)
    public void agentSave(HttpServletRequest req, HttpServletResponse response, Agent agent) throws IOException {
        JsonResult result = new JsonResult();
        try{
            int isSave = agentService.agentSave(agent);
            if(isSave==0){
                result.setCode(400);
                result.setMessage("关联代理商保存成功");
            }else{
                result.setCode(200);
            }
        }catch (Exception e){
            log.error("save agent error!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 关联代理商编辑更新
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/agentUpdate", method = RequestMethod.POST)
    public void agentUpdate(HttpServletRequest req, HttpServletResponse response, Agent agent) throws IOException {
        JsonResult result = new JsonResult();
        try{
            int isUpdate = agentService.agentUpdate(agent);
            if(isUpdate==0){
                result.setCode(400);
                result.setMessage("关联代理商编辑更新成功");
            }else{
                result.setCode(200);
            }
        }catch (Exception e){
            log.error("update agent error!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 取消关联代理商
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/cancelAgent", method = RequestMethod.POST)
    public void cancelAgent(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        Agent agent = new Agent();
        agent.setStatus("1");
        try{
            int isUpdate = agentService.agentUpdate(agent);
            if(isUpdate==0){
                result.setCode(400);
                result.setMessage("取消关联代理商成功");
            }else{
                result.setCode(200);
            }
        }catch (Exception e){
            log.error("cancel agent error!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 区域按首拼分类
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/province", method = RequestMethod.GET)
    public void province(HttpServletRequest req, HttpServletResponse response) throws IOException {
        JsonResult result = new JsonResult();
        Map map = new HashMap();
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        try{
            List<ResultBean> list = agentService.searchProvinceByPinYin();
            for(int i=0;i<8;i++){
                list1.add(list.get(i));
            }
            list1.add(list.get(33));
            for(int j=8;j<17;j++){
                list2.add(list.get(j));
            }
            for(int k=17;k<26;k++){
                list3.add(list.get(k));
            }
            for(int l=27;l<33;l++){
                list4.add(list.get(l));
            }
            map.put("A_G",list1);
            map.put("H-K",list2);
            map.put("L-S",list3);
            map.put("T-Z",list4);
            result.setCode(200);
            result.setData(map);
        }catch (Exception e){
            log.error("searchProvinceByPinYin error!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 邀请代理商入驻
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/inviteAgent", method = RequestMethod.POST)
    public void inviteAgent(HttpServletRequest req, HttpServletResponse response, String id) throws IOException {
        JsonResult result = new JsonResult();
        Member member = memberService.findMemById(id);
        String emails[] = req.getParameterValues("emails");
        try{
            for(int i = 0; i < emails.length; i++){
                String email = emails[i];
                String mail = ds.findMailAddress(email);
                if(mail == null || mail.equals("")){
                    result.setCode(400);
                    result.setData(email);
                    result.setMessage("邮箱格式不正确！");
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
                }
            }

            if(result.getCode()!=400){
                accountService.sendInviteEmail(member,emails);
                result.setCode(200);
            }
        }catch (Exception e){
            log.error("send inviteEmail error!");
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}
