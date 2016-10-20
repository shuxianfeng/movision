package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.ApiOperation;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.service.MobileAgentService;

/**
 * 代理商
 * 
 * @author tongxinglong
 * @date 2016/10/20 0020.
 */
@RestController
@RequestMapping("/rest/m/agent/mc/")
public class MobileAgentMcController {

    @Autowired
    private MobileAgentService mobileAgentService;

    @ApiOperation(value = "查询我的代理商列表", notes = "查询我的代理商列表", response = Response.class)
    @RequestMapping(value = { "/sel_my_agent_list" }, method = RequestMethod.GET)
    public Response selMyAgentList() {

        return new Response(mobileAgentService.getMyAgentListByMemId(ShiroUtil.getCreateID()));
    }

    @ApiOperation(value = "查询我的代理商详情", notes = "查询我的代理商详情", response = Response.class)
    @RequestMapping(value = { "/sel_my_agent_detail" }, method = RequestMethod.GET)
    public Response selMyAgentDetail(@ApiParam(value = "代理商ID") @RequestParam(required = false) String agentId) {

        return new Response(mobileAgentService.getAgentByProId(agentId));
    }

}
