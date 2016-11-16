package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ExpertConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.exception.PageNotFoundException;
import com.zhuhuibao.mybatis.expert.entity.Achievement;
import com.zhuhuibao.mybatis.expert.entity.Dynamic;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.service.MemberService;
import com.zhuhuibao.service.MobileExpertService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 筑慧专家controller
 *
 * @author liyang
 * @date 2016年11月16日
 */
@RestController
@RequestMapping("/rest/m/expert/mc")
public class MobileExpertController {

    private static final Logger log = LoggerFactory.getLogger(MobileExpertController.class);

    @Autowired
    private MobileExpertService expertService;

    @Autowired
    private MemberService memberService;


    @RequestMapping(value = "sel_my_looked_mobile_expert_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-筑慧中心-我查看过的专家列表", notes = "触屏端-筑慧中心-我查看过的专家列表", response = Response.class)
    public Response selMyLookedMobileExpertList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo),
                    Integer.valueOf(pageSize));
            Long createId = ShiroUtil.getCreateID();
            Map<String, Object> map = new HashMap<>();
            Member member = memberService.findMemById(String.valueOf(createId));
            if ("100".equals(member.getWorkType())) {
                map.put("companyId", createId);
            } else {
                map.put("viewerId", createId);
            }
            List<Map<String, String>> expertList = expertService.findAllMyLookedMobileExpertList(pager, map);
            pager.result(expertList);
            response.setData(pager);
        } catch (Exception e) {
            log.error("sel_my_looked_expert_list error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
                    .valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }


    @RequestMapping(value = "del_batch_my_looked_expert", method = RequestMethod.POST)
    @ApiOperation(value = "触屏端-筑慧中心-删除我查看过的专家", notes = "触屏端-筑慧中心-删除我查看过的专家", response = Response.class)
    public Response delBatchMyLookedExpert(@ApiParam(value = "要删除专家的id") @RequestParam() String ids) {
        Response response = new Response();
        try {
            String idlist[] = ids.split(",");
            for (String id : idlist) {
                expertService.deleteLookedExpert(id);
            }
        } catch (Exception e) {
            log.error("del_batch_my_looked_expert error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
                    .valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }


    @ApiOperation(value = "触屏端-筑慧中心-商品是否已经购买", notes = "触屏端-筑慧中心-商品是否已经购买", response = Response.class)
    @RequestMapping(value = "sel_payment", method = RequestMethod.GET)
    public Response viewGoodsPayInfo(@ApiParam(value = "商品ID") @RequestParam String goodsID,
                                     @ApiParam(value = "商品类型同筑慧币") @RequestParam String type) throws Exception {
        return expertService.viewGoodsRecord(goodsID, type);
    }


    @RequestMapping(value = "sel_my_looked_achievement_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-筑慧中心-查询我查看过的专家技术成果", notes = "触屏端-筑慧中心-查询我查看过的专家技术成果", response = Response.class)
    public Response selMyLookedAchievementList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo),
                    Integer.valueOf(pageSize));
            Long createId = ShiroUtil.getCreateID();
            Map<String, Object> map = new HashMap<>();
            Member member = memberService.findMemById(String.valueOf(createId));
            if ("100".equals(member.getWorkType())) {
                map.put("companyId", createId);
            } else {
                map.put("viewerId", createId);
            }
            List<Map<String, String>> achievementList = expertService.findAllMyLookedAchievementList(pager, map);
            pager.result(achievementList);
            response.setData(pager);

        } catch (Exception e) {
            log.error("sel_my_looked_expert_list error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
                    .valueOf(MsgCodeConstant.un_login)));

        }
        return response;
    }


    @RequestMapping(value = "del_batch_my_looked_achievement", method = RequestMethod.POST)
    @ApiOperation(value = "触屏端-筑慧中心-批量删除我查看过的专家技术成果", notes = "触屏端-筑慧中心-批量删除我查看过的专家技术成果", response = Response.class)
    public Response delBatchMyLookedAchievement(@RequestParam() String ids) {
        Response response = new Response();
        String idlist[] = ids.split(",");
        for (String id : idlist) {
            expertService.deleteLookedAchievement(id);
        }
        return response;
    }


    @ApiOperation(value = "触屏端-筑慧中心-我的协会动态(后台)", notes = "触屏端-筑慧中心-我的协会动态(后台)", response = Response.class)
    @RequestMapping(value = "dynamic/sel_myDynamicList", method = RequestMethod.GET)
    public Response myDynamicList(@ApiParam(value = "状态") @RequestParam(required = false) String status,
                                  @RequestParam(required = false, defaultValue = "1") String pageNo,
                                  @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try {
            Paging<Dynamic> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            Map<String, Object> map = new HashMap<>();
            //查询传参
            map.put("status", status);
            Long createId = ShiroUtil.getCreateID();
            map.put("createId", String.valueOf(createId));
            List<Dynamic> dynamicList = expertService.findAllDynamicList(pager, map);
            List list = new ArrayList();
            for (Dynamic Dynamic : dynamicList) {
                Map m = new HashMap();
                m.put("id", Dynamic.getId());
                m.put("title", Dynamic.getTitle());
                m.put("updateTime", Dynamic.getUpdateTime());
                m.put("status", Dynamic.getStatus());
                list.add(m);
            }
            pager.result(list);
            response.setData(pager);

        } catch (Exception e) {
            log.error("sel_my_dynamic_list error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;

    }


    @ApiOperation(value = "触屏端-筑慧中心-删除协会动态", notes = "触屏端-筑慧中心-删除协会动态", response = Response.class)
    @RequestMapping(value = "del_dynamic", method = RequestMethod.POST)
    public Response deleteDynamic(@ApiParam(value = "协会动态ids,逗号隔开") @RequestParam String ids) {

        Response response = new Response();
        try {
            String[] idList = ids.split(",");
            for (String id : idList) {
                Dynamic dynamic = new Dynamic();
                dynamic.setIs_deleted(ExpertConstant.EXPERT_DELETE_ONE);
                dynamic.setId(id);
                expertService.updateDynamic(dynamic);
            }
        } catch (Exception e) {
            log.error("del_dynamic error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }


    @ApiOperation(value = "触屏端-筑慧中心-协会动态详情", notes = "触屏端-筑慧中心-协会动态详情", response = Response.class)
    @RequestMapping(value = "dynamic/sel_dynamic", method = RequestMethod.GET)
    public Response queryDynamicById(@ApiParam(value = "协会动态Id") @RequestParam String id) throws Exception {
        Response response = new Response();
        try {
            Dynamic dynamic = expertService.queryDynamicById(id);
            response.setData(dynamic);
        } catch (Exception e) {
            log.error("dynamic/sel_dynamic error! ", e);
            throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
        }
        return response;
    }


    @RequestMapping(value = "sel_my_looked_achievementList", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-筑慧中心-我的技术成果", notes = "触屏端-筑慧中心-我的技术成果", response = Response.class)
    public Response sel_my_looked_achievementList(@ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
                                                  @ApiParam(value = "每页显示的数目") @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        try{

            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo),
                    Integer.valueOf(pageSize));
            Long createId = ShiroUtil.getCreateID();
            Map<String, Object> map = new HashMap<>();
            if (createId != null) {
                Member member = memberService.findMemById(String.valueOf(createId));
                if ("100".equals(member.getWorkType())) {
                    map.put("companyId", createId);
                } else {
                    map.put("viewerId", createId);
                }
                List<Map<String, String>> achievementList = expertService.findAllAchievementList(pager, map);
                pager.result(achievementList);
                response.setData(pager);
            } else {
                throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String
                        .valueOf(MsgCodeConstant.un_login)));
            }
        }catch (Exception e){
            log.error("sel_my_achievement_list error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }




    @ApiOperation(value = "触屏端-筑慧中心-删除技术成果", notes = "触屏端-筑慧中心-删除技术成果", response = Response.class)
    @RequestMapping(value = "ach/del_achievement", method = RequestMethod.POST)
    public Response deleteAchievement(@ApiParam(value = "技术成果ids,逗号隔开") @RequestParam String ids) {
        Response response = new Response();
        try {
            String[] idList = ids.split(",");
            for (String id : idList) {
                Achievement achievement = new Achievement();
                achievement.setIs_deleted(ExpertConstant.EXPERT_DELETE_ONE);
                achievement.setId(id);
                expertService.updateAchievement(achievement);
            }

        } catch (Exception e) {
            log.error("ach/del_achievement error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    @ApiOperation(value = "触屏端-筑慧中心-技术成果详情", notes = "触屏端-筑慧中心-技术成果详情", response = Response.class)
    @RequestMapping(value = "ach/sel_achievement", method = RequestMethod.GET)
    public Response queryAchievementById(@ApiParam(value = "技术成果ID") @RequestParam String id) throws Exception {
        Response response = new Response();
        try {
            Map<String, String> map = expertService.queryAchievementById(id);
            if (map != null) {
                response.setData(map);
            } else {
                throw new PageNotFoundException(MsgCodeConstant.SYSTEM_ERROR, "页面不存在");
            }
        } catch (Exception e) {
            log.error("ach/sel_achievement error! ", e);
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));

        }

        return response;
    }
}
