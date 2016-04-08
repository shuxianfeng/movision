package com.zhuhuibao.business.memCenter.AgentManage;

import com.zhuhuibao.common.*;
import com.zhuhuibao.mybatis.dictionary.service.DictionaryService;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.AgentService;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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
            e.printStackTrace();
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
            e.printStackTrace();
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
        String type = req.getParameter("type");
        try{
            List<AccountBean> memList = memberService.findAgentMember(account,type);
            result.setCode(200);
            result.setData(memList);
        }catch (Exception e){
            log.error("query Agent error!");
            e.printStackTrace();
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
            Agent agent1 = agentService.find(agent);
            if(agent1==null){
                int isSave = agentService.agentSave(agent);
                if(isSave==0){
                    result.setCode(400);
                    result.setMessage("关联代理商保存成功");
                }else{
                    result.setCode(200);
                }
            }else{
                result.setCode(400);
                result.setMessage("该品牌该代理商已设置");
            }
        }catch (Exception e){
            log.error("save agent error!");
            e.printStackTrace();
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
            Agent agent1 = agentService.find(agent);
            if(agent1==null) {
                int isUpdate = agentService.agentUpdate(agent);
                if (isUpdate == 0) {
                    result.setCode(400);
                    result.setMessage("关联代理商编辑更新成功");
                } else {
                    result.setCode(200);
                }
            }else{
                result.setCode(400);
                result.setMessage("该品牌该代理商已设置");
            }
        }catch (Exception e){
            log.error("update agent error!");
            e.printStackTrace();
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
    public void cancelAgent(HttpServletRequest req, HttpServletResponse response,Agent agent) throws IOException {
        JsonResult result = new JsonResult();
        agent.setStatus("1");
        try{
            int isUpdate = agentService.agentUpdate(agent);
            if(isUpdate==0){
                result.setCode(400);
                result.setMessage("取消关联代理商失败");
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
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        Map map = new HashMap();
        try{
            List<CommonBean> ResultList = agentService.searchProvinceByPinYin();
            for(int i=0;i<ResultList.size();i++){
                Map map1 = new HashMap();
                CommonBean resultBean = ResultList.get(i);
                map1.put("id",resultBean.getId());
                map1.put("code",resultBean.getCode());
                map1.put("name",resultBean.getName());
                if("A-G".equals(resultBean.getOther())){
                    list1.add(map1);
                }else if("H-K".equals(resultBean.getOther())){
                    list2.add(map1);
                }else if("L-S".equals(resultBean.getOther())){
                    list3.add(map1);
                }else{
                    list4.add(map1);
                }
            }
            map.put("AG",list1);
            map.put("HK",list2);
            map.put("LS",list3);
            map.put("TZ",list4);
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
        String email = req.getParameter("email");
        Member member1 = new Member();
        member1.setEmail(email);
        Member member2 = memberService.findMemer(member1);
        try{
                String mail = ds.findMailAddress(email);
                if(mail == null || mail.equals("")){
                    result.setCode(400);
                    result.setData(email);
                    result.setMessage("系统暂不支持此邮箱！");
                }else{
                    if(member2 == null){
                        accountService.sendInviteEmail(member,email);
                    }
                    result.setCode(200);
                }
        }catch (Exception e){
            log.error("send inviteEmail error!");
            e.printStackTrace();
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 查询我的代理商
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/myAgent", method = RequestMethod.GET)
    public void myAgent(HttpServletRequest req, HttpServletResponse response, String id) throws IOException {
        JsonResult result = new JsonResult();
        try{
            List<AgentBean> list = agentService.findAgentByMemId(id);
            result.setData(list);
            result.setCode(200);
        }catch (Exception e){
            log.error("findAgentByMemId error!");
            e.printStackTrace();
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 代理商邀请邮件点击注册
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/agentRegister", method = RequestMethod.GET)
    public ModelAndView agentRegister(HttpServletRequest req, HttpServletResponse response) throws IOException {
        log.debug("email agentRegister start.....");
        JsonResult result = new JsonResult();
        ModelAndView modelAndView = new ModelAndView();
        try
        {
            String vm = req.getParameter("vm");//获取email
            if(vm != null & !vm.equals(""))
            {
                String decodeVM = new String (EncodeUtil.decodeBase64(vm));
                result = accountService.agentRegister(decodeVM);
                RedirectView rv = new RedirectView(accountService.getRedirectUrl(result));
                modelAndView.setView(rv);
            }
        }
        catch(Exception e)
        {
            log.error("email agentRegister error!",e);
            e.printStackTrace();
        }

        return modelAndView;

    }

    /**
     * 代理商编辑，参数，id
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/updateAgentById", method = RequestMethod.GET)
    public void updateAgentById(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = agentService.updateAgentById(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据产品id查询代理商跟厂商（区域分组）
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/getAgentByProId", method = RequestMethod.GET)
    public void getAgentByProId(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = agentService.getAgentByProId(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据子系统id查优秀代理商
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/getGreatAgentByScateid", method = RequestMethod.GET)
    public void getGreatAgentByScateid(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = agentService.getGreatAgentByScateid(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }

    /**
     * 根据品牌id查优秀代理商
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/getGreatAgentByBrandId", method = RequestMethod.GET)
    public void getGreatAgentByBrandId(HttpServletRequest req, HttpServletResponse response,String id) throws IOException {
        JsonResult result = agentService.getGreatAgentByBrandId(id);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JsonUtils.getJsonStringFromObj(result));
    }
}
