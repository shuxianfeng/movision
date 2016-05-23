package com.zhuhuibao.business.memCenter.ExhibitionManage;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.constant.MsgCodeConstant;
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
    @ApiOperation(value="我的活动",notes="我的活动",response = JsonResult.class)
    @RequestMapping(value = "findAllMyExhibition", method = RequestMethod.GET)
    public JsonResult findAllMyExhibition(@ApiParam(value = "标题")@RequestParam(required = false)String title,
                                        @ApiParam(value = "审核状态")@RequestParam(required = false)String status,
                                        @RequestParam(required = false)String pageNo,@RequestParam(required = false)String pageSize) {
        JsonResult jsonResult = new JsonResult();
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
                jsonResult.setData(pager);
            }else {
                jsonResult.setCode(401);
                jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
                jsonResult.setMsgCode(MsgCodeConstant.un_login);
            }
        }else {
            jsonResult.setCode(401);
            jsonResult.setMessage(MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
            jsonResult.setMsgCode(MsgCodeConstant.un_login);
        }
        return jsonResult;
    }

    /**
     * 会展详情查看
     */
    @ApiOperation(value="会展详情查看",notes="会展详情查看",response = JsonResult.class)
    @RequestMapping(value = "queryExhibitionInfoById", method = RequestMethod.GET)
    public JsonResult queryExhibitionInfoById(@RequestParam String id)  {
        JsonResult jsonResult = new JsonResult();
        Exhibition exhibition = exhibitionService.queryExhibitionInfoById(id);
        jsonResult.setData(exhibition);
        return jsonResult;
    }

    /**
     * 会展信息编辑更新
     */
    @ApiOperation(value="会展信息编辑更新",notes="会展信息编辑更新",response = JsonResult.class)
    @RequestMapping(value = "updateExhibitionInfoById", method = RequestMethod.POST)
    public JsonResult updateExhibitionInfoById(@ModelAttribute()Exhibition exhibition)  {
        JsonResult jsonResult = new JsonResult();
        exhibitionService.updateExhibitionInfoById(exhibition);
        return jsonResult;
    }

    /**
     * 会展信息删除
     */
    @ApiOperation(value="会展信息删除",notes="会展信息删除",response = JsonResult.class)
    @RequestMapping(value = "deleteExhibition", method = RequestMethod.POST)
    public JsonResult deleteExhibition(@ApiParam(value = "ids,逗号隔开") @RequestParam String ids)  {
        JsonResult jsonResult = new JsonResult();
        String[] idList = ids.split(",");
        for (String id : idList) {
            String is_deleted = "1";
            Exhibition exhibition = new Exhibition();
            exhibition.setIs_deleted(is_deleted);
            exhibition.setId(id);
            exhibitionService.updateExhibitionInfoById(exhibition);
        }
        return jsonResult;
    }
}
