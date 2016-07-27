package com.zhuhuibao.business.project.mc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.project.service.ProjectService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

/**
 * 会员中心我查看过的项目信息
 *
 * @author pl
 * @create 2016/6/1 0001
 **/
@RestController
@RequestMapping(value = "/rest/project/mc")
@Api(value = "projectMc", description = "会员中心我查看过的项目信息")
public class ProjectMcController {
    @Autowired
    ProjectService projectService;

    /**
     * 根据条件查询项目分页信息
     */
    @RequestMapping(value = {"sel_project_info", "base/sel_project_info"}, method = RequestMethod.GET)
    @ApiOperation(value = "查询我查看过的项目信息", notes = "查询我查看过的项目信息", response = Response.class)
    public Response searchProjectPage(@ApiParam(value = "项目名称") @RequestParam(required = false) String name,
                                      @ApiParam(value = "城市Code") @RequestParam(required = false) String city,
                                      @ApiParam(value = "省代码") @RequestParam(required = false) String province,
                                      @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
                                      @ApiParam(value = "页码",defaultValue = "1") @RequestParam(required = false) String pageNo,
                                      @ApiParam(value = "每页显示的条数",defaultValue = "10") @RequestParam(required = false) String pageSize) throws Exception {
        // 封装查询参数
        Long createId = ShiroUtil.getCreateID();
        Response response = new Response();
        if (createId != null) {
            Map<String, Object> map = new HashMap<>();
            if (name != null && !"".equals(name)) {
                map.put("name", name.replace("_", "\\_"));
            }
            map.put("city", city);
            map.put("province", province);
            map.put("category", category);
            map.put("viewerId", createId);
            map.put("type", ZhbConstant.ZhbGoodsType.CKXMXX.toString());

            Paging<Map<String, String>> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
            // 调用查询接口
            List<Map<String, String>> projectList = projectService.queryOmsViewProject(map, pager);
            pager.result(projectList);
            response.setData(pager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

}
