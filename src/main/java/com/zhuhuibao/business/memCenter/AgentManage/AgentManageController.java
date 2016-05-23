package com.zhuhuibao.business.memCenter.AgentManage;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.*;
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
@RequestMapping(value = "/rest/agent")
public class AgentManageController {
    private static final Logger log = LoggerFactory.getLogger(AgentManageController.class);

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
    @RequestMapping(value = "category", method = RequestMethod.GET)
    public JsonResult category(String id) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "brand", method = RequestMethod.GET)
    public JsonResult brand(Brand brand) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "searchAgent", method = RequestMethod.GET)
    public JsonResult searchAgent(HttpServletRequest req) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "agentSave", method = RequestMethod.POST)
    public JsonResult agentSave(Agent agent) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "agentUpdate", method = RequestMethod.POST)
    public JsonResult agentUpdate(Agent agent) throws Exception {
        JsonResult result = new JsonResult();
        agentService.agentUpdate(agent);
        return result;
    }

    /**
     * 取消关联代理商
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "cancelAgent", method = RequestMethod.POST)
    public JsonResult cancelAgent(Agent agent) throws Exception {
        JsonResult result = new JsonResult();
        agent.setStatus("1");
        agentService.agentUpdate(agent);
        return result;
    }

    /**
     * 区域按首拼分类
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "province", method = RequestMethod.GET)
    public JsonResult province() throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "inviteAgent", method = RequestMethod.POST)
    public JsonResult inviteAgent(HttpServletRequest req, String id) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "myAgent", method = RequestMethod.GET)
    public JsonResult myAgent(String id) throws Exception {
        JsonResult result = new JsonResult();
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
    @RequestMapping(value = "agentRegister", method = RequestMethod.GET)
    public ModelAndView agentRegister(HttpServletRequest req) throws IOException {
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
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "updateAgentById", method = RequestMethod.GET)
    public JsonResult updateAgentById(String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Map map = agentService.updateAgentById(id);
        jsonResult.setData(map);
        return jsonResult;
    }

    /**
     * 根据产品id查询代理商跟厂商（区域分组）
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getAgentByProId", method = RequestMethod.GET)
    public JsonResult getAgentByProId(String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Map map = agentService.getAgentByProId(id);
        jsonResult.setData(map);
        return jsonResult;
    }

    /**
     * 根据子系统id查优秀代理商
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/rest/agent/getGreatAgentByScateid", method = RequestMethod.GET)
    public JsonResult getGreatAgentByScateid(String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        List<ResultBean> resultBeen =  agentService.getGreatAgentByScateid(id);
        jsonResult.setData(resultBeen);
        return jsonResult;
    }

    /**
     * 根据品牌id查优秀代理商
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "getGreatAgentByBrandId", method = RequestMethod.GET)
    public JsonResult getGreatAgentByBrandId(String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        List<ResultBean> resultBeen =  agentService.getGreatAgentByBrandId(id);
        jsonResult.setData(resultBeen);
        return jsonResult;
    }

    /**
     * 根据品牌id查询代理商跟厂商（区域分组）
     * @return
     * @throws IOException
     */
    @ApiOperation(value="根据品牌id查询代理商跟厂商（区域分组）",notes="根据品牌id查询代理商跟厂商（区域分组）",response = JsonResult.class)
    @RequestMapping(value = "getAgentByBrandid", method = RequestMethod.GET)
    public JsonResult getAgentByBrandid(String id) throws Exception {
        JsonResult jsonResult = new JsonResult();
        Map map = agentService.getAgentByBrandid(id);
        jsonResult.setData(map);
        return jsonResult;
    }
}
