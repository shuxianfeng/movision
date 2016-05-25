package com.zhuhuibao.business.memCenter.ExhibitionManage;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Exhibition;
import com.zhuhuibao.mybatis.memCenter.service.ExhibitionService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会展接口类
 * Created by cxx on 2016/5/11 0011.
 */
@RestController
@RequestMapping("/rest/exhibition")
public class ExhibitionController {
    private static final Logger log = LoggerFactory.getLogger(ExhibitionController.class);

    @Autowired
    private ExhibitionService exhibitionService;

    /**
     * 我的活动
     */
    @ApiOperation(value="我的活动",notes="我的活动",response = Response.class)
    @RequestMapping(value = "findAllMyExhibition", method = RequestMethod.GET)
    public Response findAllMyExhibition(@ApiParam(value = "标题")@RequestParam(required = false)String title,
                                        @ApiParam(value = "审核状态")@RequestParam(required = false)String status,
                                        @RequestParam(required = false)String pageNo, @RequestParam(required = false)String pageSize) {
        Response response = new Response();
        //设定默认分页pageSize
        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Exhibition> pager = new Paging<Exhibition>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        Map<String,Object> map = new HashMap<>();
        //查询传参
        map.put("title",title);
        map.put("status",status);
        map.put("type",3);
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        if (null != session) {
            ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
            if (null != principal) {
                map.put("createId", principal.getId());
                //查询
                List<Exhibition> exhibitionList = exhibitionService.findAllExhibition(pager,map);
                pager.result(exhibitionList);
                response.setData(pager);
            }else {
                throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            }
        }else {
            throw new AuthException(MsgCodeConstant.un_login,MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    /**
     * 根據id查詢会展详情
     */
    @ApiOperation(value="根據id查詢会展详情",notes="根據id查詢会展详情",response = Response.class)
    @RequestMapping(value = "queryExhibitionInfoById", method = RequestMethod.GET)
    public Response queryExhibitionInfoById(@RequestParam String id)  {
        Response response = new Response();
        Exhibition exhibition = exhibitionService.queryExhibitionInfoById(id);
        response.setData(exhibition);
        return response;
    }

    /**
     * 会展信息编辑更新
     */
    @ApiOperation(value="会展信息编辑更新",notes="会展信息编辑更新",response = Response.class)
    @RequestMapping(value = "updateExhibitionInfoById", method = RequestMethod.POST)
    public Response updateExhibitionInfoById(@ModelAttribute()Exhibition exhibition)  {
        Response response = new Response();
        exhibitionService.updateExhibitionInfoById(exhibition);
        return response;
    }

    /**
     * 会展信息删除
     */
    @ApiOperation(value="会展信息删除",notes="会展信息删除",response = Response.class)
    @RequestMapping(value = "deleteExhibition", method = RequestMethod.POST)
    public Response deleteExhibition(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids)  {
        Response response = new Response();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Exhibition exhibition = new Exhibition();
            exhibition.setIs_deleted(is_deleted);
            exhibition.setId(id);
            exhibitionService.updateExhibitionInfoById(exhibition);
        }
        return response;
    }
}
