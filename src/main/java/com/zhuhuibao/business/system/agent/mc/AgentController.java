package com.zhuhuibao.business.system.agent.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.*;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.Agent;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.AccountService;
import com.zhuhuibao.mybatis.memCenter.service.AgentService;
import com.zhuhuibao.mybatis.memCenter.service.BrandService;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.mybatis.memberReg.entity.LoginMember;
import com.zhuhuibao.mybatis.memberReg.service.MemberRegService;
import com.zhuhuibao.mybatis.oms.service.CategoryService;
import com.zhuhuibao.security.EncodeUtil;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @Autowired
    MemberRegService memberRegService;

    @ApiOperation(value = "根据品牌查询它所属的大系统，子系统",
            notes = "根据品牌查询它所属的大系统，子系统", response = Response.class)
    @RequestMapping(value = {"/rest/agent/category", "/rest/system/mc/agent/sel_category"}, method = RequestMethod.GET)
    public Response category(@RequestParam String id) {
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

    @ApiOperation(value = "根据会员id查询其品牌",
            notes = "根据会员id查询其品牌", response = Response.class)
    @RequestMapping(value = {"/rest/agent/brand", "/rest/system/mc/agent/sel_brand_by_memId"}, method = RequestMethod.GET)
    public Response brand(@ModelAttribute Brand brand) {
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

    @ApiOperation(value = "根据账号，公司名称查询代理商会员",
            notes = "根据账号，公司名称查询代理商会员", response = Response.class)
    @RequestMapping(value = {"/rest/agent/searchAgent", "/rest/system/mc/agent/sel_agent_by_account"}, method = RequestMethod.GET)
    public Response searchAgent(@RequestParam String account, @ApiParam(value = "1:账号;2:公司名称") @RequestParam String type) {
        Response result = new Response();
        if (account.contains("_")) {
            account = account.replace("_", "\\_");
        }
        List<AccountBean> memList = memberService.findAgentMember(account, type);
        result.setData(memList);
        return result;
    }

    @ApiOperation(value = "关联代理商保存",
            notes = "关联代理商保存", response = Response.class)
    @RequestMapping(value = {"/rest/agent/agentSave", "/rest/system/mc/agent/add_agent"}, method = RequestMethod.POST)
    public Response agentSave(@ModelAttribute Agent agent) {
        Response result = new Response();
        Agent agent1 = agentService.find(agent);
        if (agent1 == null) {
            agentService.agentSave(agent);
        } else {
            result.setCode(400);
            result.setMessage("该品牌该代理商已设置");
        }
        return result;
    }

    @ApiOperation(value = "关联代理商编辑更新",
            notes = "关联代理商编辑更新", response = Response.class)
    @RequestMapping(value = {"/rest/agent/agentUpdate", "/rest/system/mc/agent/upd_agent"}, method = RequestMethod.POST)
    public Response agentUpdate(@ModelAttribute Agent agent) {
        Response result = new Response();
        agentService.agentUpdate(agent);
        return result;
    }

    @ApiOperation(value = "取消关联代理商",
            notes = "取消关联代理商", response = Response.class)
    @RequestMapping(value = {"/rest/agent/cancelAgent", "/rest/system/mc/agent/cancel_agent"}, method = RequestMethod.POST)
    public Response cancelAgent(@ModelAttribute Agent agent) {
        Response result = new Response();
        agent.setStatus("1");
        agentService.agentUpdate(agent);
        return result;
    }

    @ApiOperation(value = "区域按首拼分类", notes = "区域按首拼分类", response = Response.class)
    @RequestMapping(value = {"/rest/agent/province", "/rest/system/mc/agent/sel_province_by_pinyin"}, method = RequestMethod.GET)
    public Response province() {
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
        map.put("AG", list1);
        map.put("HK", list2);
        map.put("LS", list3);
        map.put("TZ", list4);
        result.setCode(200);
        result.setData(map);
        return result;
    }

    @ApiOperation(value = "邀请代理商入驻", notes = "邀请代理商入驻", response = Response.class)
    @RequestMapping(value = {"/rest/agent/inviteAgent", "/rest/system/mc/agent/invite_agent"}, method = RequestMethod.POST)
    public Response inviteAgent(@RequestParam String email) {
        Response result = new Response();

        Long createId = ShiroUtil.getCreateID();
        if (createId == null) {
            throw new AuthException(MsgCodeConstant.un_login, "请登录");
        }

        Member member = memberService.findMemById(String.valueOf(createId));
        Member member1 = new Member();
        member1.setEmail(email);
        Member member2 = memberService.findMember(member1);
        try {
            if (member2 == null) {
                accountService.sendInviteEmail(member, email);
            }
            result.setCode(200);
        } catch (Exception e) {
            log.error("send inviteEmail error!");
            e.printStackTrace();
        }
        return result;
    }

    @ApiOperation(value = "查询我的代理商", notes = "查询我的代理商", response = Response.class)
    @RequestMapping(value = {"/rest/agent/myAgent", "/rest/system/mc/agent/sel_my_agent"}, method = RequestMethod.GET)
    public Response myAgent(@RequestParam String id) {
        Response result = new Response();
        List<AgentBean> list = agentService.findAgentByMemId(id);
        result.setData(list);
        result.setCode(200);
        return result;
    }

    @ApiOperation(value = "代理商邀请邮件点击注册", notes = "代理商邀请邮件点击注册", response = Response.class)
    @RequestMapping(value = {"/rest/agent/agentRegister", "/rest/system/mc/agent/agent_register"}, method = RequestMethod.GET)
    public ModelAndView agentRegister(@RequestParam String vm) throws IOException {
        log.debug("email agentRegister start.....");
        Response result;
        ModelAndView modelAndView = new ModelAndView();
        try {
            if (!StringUtils.isEmpty(vm)) {
                String decodeVM = new String(EncodeUtil.decodeBase64(vm));
                result = accountService.agentRegister(decodeVM);
                LoginMember loginMember = memberRegService.getLoginMemberByAccount(decodeVM.split(",")[1]);
                ShiroRealm.ShiroUser shrioUser = new ShiroRealm.ShiroUser(loginMember.getId(), loginMember.getAccount(),
                        loginMember.getStatus(), loginMember.getIdentify(),loginMember.getRole(), "0",
                        loginMember.getCompanyId(), loginMember.getRegisterTime(), loginMember.getWorkType(),
                        loginMember.getHeadShot(), loginMember.getNickname(), loginMember.getCompanyName(), loginMember.getVipLevel());
                Subject currentUser = SecurityUtils.getSubject();
                Session session = currentUser.getSession();
                session.setAttribute("member", shrioUser);
                RedirectView rv = new RedirectView(accountService.getRedirectUrl(result));
                modelAndView.setView(rv);
            }
        } catch (Exception e) {
            log.error("email agentRegister error!", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"操作失败");
        }

        return modelAndView;

    }

    @ApiOperation(value = "根据id查询代理商信息", notes = "根据id查询代理商信息", response = Response.class)
    @RequestMapping(value = {"/rest/agent/updateAgentById", "/rest/system/mc/agent/sel_agent_by_id"}, method = RequestMethod.GET)
    public Response updateAgentById(@RequestParam String id) {
        Response response = new Response();
        Map map = agentService.updateAgentById(id);
        response.setData(map);
        return response;
    }
}
