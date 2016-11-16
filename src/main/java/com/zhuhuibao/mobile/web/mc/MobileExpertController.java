package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.service.MobileExpertListService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private MobileExpertListService expertListService;

    @RequestMapping(value = "sel_my_looked_expert_list", method = RequestMethod.GET)
    @ApiOperation(value = "触屏端-筑慧中心-我查看过的专家列表", notes = "触屏端-筑慧中心-我查看过的专家列表", response = Response.class)
    public Response selGreatExpertList(@ApiParam(value = "会员的Id") @RequestParam(required = true) String id) {
        Response response = new Response();
        try {
            Map<String, Object> map = expertListService.findAllMyLookedExpertListById(id);
            response.setData(map);
        } catch (Exception e) {
            log.error("sel_my_looked_expert_list error! ", e);
            e.printStackTrace();
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

}
