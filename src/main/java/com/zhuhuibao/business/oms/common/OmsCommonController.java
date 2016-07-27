package com.zhuhuibao.business.oms.common;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.mybatis.common.entity.SysJoinus;
import com.zhuhuibao.mybatis.common.service.SysJoinusService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jianglz
 * @since 16/7/26.
 */
@RestController
@RequestMapping("rest/common/oms")
public class OmsCommonController {

    private static final Logger log = LoggerFactory.getLogger(OmsCommonController.class);

    @Autowired
    SysJoinusService sysJoinusService;


    @ApiOperation(value="地区代理信息列表(运营分页)",notes="地区代理信息列表(运营分页)",response = Response.class)
    @RequestMapping(value = "sel_joinus_page", method = RequestMethod.GET)
    public Response listJoinus(@RequestParam(required = false) String id,
                                   @RequestParam(required = false)String realName,
                                   @RequestParam(required = false)String mobile,
                                   @RequestParam(required = false)String status,
                                   @RequestParam(required = false,defaultValue = "1")String pageNo,
                                   @RequestParam(required = false,defaultValue = "10")String pageSize)  {
        log.debug("find all joinus pager....");
        Paging<SysJoinus> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(id)){
            map.put("id",id);
        }
        if(!StringUtils.isEmpty(realName)){
            map.put("realName",realName);
        }
        if(!StringUtils.isEmpty(mobile)){
            map.put("mobile",mobile);
        }
        if(!StringUtils.isEmpty(status)){
            map.put("status",status);
        }
        List<SysJoinus> joinusList = sysJoinusService.findAllJoinusPager(pager,map);
        pager.result(joinusList);
        return new Response(pager);
    }


    @ApiOperation(value="查看详情",notes="查看详情",response = Response.class)
    @RequestMapping(value = "sel_joinus_detail", method = RequestMethod.GET)
    public Response getJoinusDetail(@ApiParam ("id")@RequestParam String id){

        SysJoinus joinus = sysJoinusService.selectById(id);

        return new Response(joinus);
    }

    @ApiOperation(value="处理申请",notes="处理申请",response = Response.class)
    @RequestMapping(value = "upd_joinus", method = RequestMethod.POST)
    public Response update(@ApiParam ("id")@RequestParam String id,
    		               @ApiParam ("remark")@RequestParam String remark,
                           @ApiParam ("状态 0：待处理，1：已处理")@RequestParam String status){

        SysJoinus joinus = new SysJoinus();
        joinus.setId(Integer.valueOf(id));
        joinus.setStatus(status);
        joinus.setRemark(remark);
        sysJoinusService.update(joinus);
        return new Response();
    }
}
