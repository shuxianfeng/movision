package com.zhuhuibao.business.system.agent.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.pojo.*;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.AgentService;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.security.EncodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
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
public class AgentController {
    private static final Logger log = LoggerFactory.getLogger(AgentController.class);

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

    /**
     * 根据品牌查询它所属的大系统，子系统
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/category","/rest/system/mc/agent/sel_category"}, method = RequestMethod.GET)
    public Response category(String id)  {
        Response result = new Response();
        List list = new ArrayList();
        List<ResultBean> resultBeanList = categoryService.findSystemByBrand(id);
        List<SysBean> sysBeanList = categoryService.findCategoryByBrand(id);
        for (ResultBean resultBean : resultBeanList) {
            Map map = new HashMap();
            List list1 = new ArrayList();
            map.put("id", resultBean.getCode());
            map.put("name", resultBean.getName());
            for (int y = 0; y < sysBeanList.size(); y++) {
                SysBean sysBean = sysBeanList.get(y);
                Map map1 = new HashMap();
                if (resultBean.getCode().equals(sysBean.getId())) {
                    map1.put("id", sysBean.getCode());
                    map1.put("name", sysBean.getSubSystemName());
                    list1.add(map1);
                }
            }
            map.put("subSys", list1);
            list.add(map);
        }
        result.setData(list);
        return result;
    }

    /**
     * 根据会员id查询其品牌
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/brand","/rest/system/mc/agent/sel_brand_by_memId"}, method = RequestMethod.GET)
    public Response brand(Brand brand)  {
        Response result = new Response();
        List list = new ArrayList();
        List<Brand> brands = brandService.searchBrandByStatus(brand);
        for (Brand brand1 : brands) {
            Map map = new HashMap();
            map.put("id", brand1.getId());
            map.put("name", brand1.getCNName());
            list.add(map);
        }
        result.setData(list);
        return result;
    }

    /**
     * 根据账号，公司名称查询代理商会员
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/searchAgent","/rest/system/mc/agent/sel_agent_by_account"}, method = RequestMethod.GET)
    public Response searchAgent(HttpServletRequest req)  {
        Response result = new Response();
        String account = req.getParameter("account");
        if(account.contains("_")){
            account = account.replace("_","\\_");
        }
        String type = req.getParameter("type");
        List<AccountBean> memList = memberService.findAgentMember(account,type);
        result.setData(memList);
        return result;
    }

    /**
     * 关联代理商保存
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/agentSave","/rest/system/mc/agent/add_agent"}, method = RequestMethod.POST)
    public Response agentSave(Agent agent)  {
        Response result = new Response();
        Agent agent1 = agentService.find(agent);
        if(agent1==null){
            agentService.agentSave(agent);
        }else{
            result.setCode(400);
            result.setMessage("该品牌该代理商已设置");
        }
        return result;
    }

    /**
     * 关联代理商编辑更新
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/agentUpdate","/rest/system/mc/agent/upd_agent"}, method = RequestMethod.POST)
    public Response agentUpdate(Agent agent)  {
        Response result = new Response();
        agentService.agentUpdate(agent);
        return result;
    }

    /**
     * 取消关联代理商
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/cancelAgent","/rest/system/mc/agent/cancel_agent"}, method = RequestMethod.POST)
    public Response cancelAgent(Agent agent)  {
        Response result = new Response();
        agent.setStatus("1");
        agentService.agentUpdate(agent);
        return result;
    }

    /**
     * 区域按首拼分类
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/province","/rest/system/mc/agent/sel_province_by_pinyin"}, method = RequestMethod.GET)
    public Response province()  {
        Response result = new Response();
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        List list4 = new ArrayList();
        Map map = new HashMap();
        List<CommonBean> ResultList = agentService.searchProvinceByPinYin();
        for (CommonBean aResultList : ResultList) {
            Map map1 = new HashMap();
            CommonBean resultBean = aResultList;
            map1.put("id", resultBean.getId());
            map1.put("code", resultBean.getCode());
            map1.put("name", resultBean.getName());
            if ("A-G".equals(resultBean.getOther())) {
                list1.add(map1);
            } else if ("H-K".equals(resultBean.getOther())) {
                list2.add(map1);
            } else if ("L-S".equals(resultBean.getOther())) {
                list3.add(map1);
            } else {
                list4.add(map1);
            }
        }
        map.put("AG",list1);
        map.put("HK",list2);
        map.put("LS",list3);
        map.put("TZ",list4);
        result.setCode(200);
        result.setData(map);
        return result;
    }

    /**
     * 邀请代理商入驻
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/inviteAgent","/rest/system/mc/agent/invite_agent"}, method = RequestMethod.POST)
    public Response inviteAgent(HttpServletRequest req, String id) {
        Response result = new Response();
        Member member = memberService.findMemById(id);
        String email = req.getParameter("email");
        Member member1 = new Member();
        member1.setEmail(email);
        Member member2 = memberService.findMemer(member1);
        try{
            if(member2 == null){
                accountService.sendInviteEmail(member,email);
            }
            result.setCode(200);
        }catch (Exception e){
            log.error("send inviteEmail error!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询我的代理商
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/myAgent","/rest/system/mc/agent/sel_my_agent"}, method = RequestMethod.GET)
    public Response myAgent(String id) {
        Response result = new Response();
        List<AgentBean> list = agentService.findAgentByMemId(id);
        result.setData(list);
        result.setCode(200);
        return result;
    }

    /**
     * 代理商邀请邮件点击注册
     * @param req
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/agentRegister","/rest/system/mc/agent/agent_register"}, method = RequestMethod.GET)
    public ModelAndView agentRegister(HttpServletRequest req) throws IOException {
        log.debug("email agentRegister start.....");
        Response result = new Response();
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
     * @return
     * @throws IOException
     */
    @RequestMapping(value = {"/rest/agent/updateAgentById","/rest/system/mc/agent/sel_agent_by_id"}, method = RequestMethod.GET)
    public Response updateAgentById(String id)  {
        Response response = new Response();
        Map map = agentService.updateAgentById(id);
        response.setData(map);
        return response;
    }
}
