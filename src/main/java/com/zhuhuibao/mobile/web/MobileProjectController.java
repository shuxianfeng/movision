package com.zhuhuibao.mobile.web;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.service.MobileProjectService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目信息controller
 * 
 * @author tongxinglong
 * @date 2016/10/17 0017.
 */
@RestController
@RequestMapping("/rest/m/project")
public class MobileProjectController {

    @Autowired
    private MobileProjectService mobileProjectService;

    @RequestMapping(value = { "/mc/sel_my_project_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "查询我查看过的项目信息", notes = "查询我查看过的项目信息", response = Response.class)
    public Response selMyProjectList(@ApiParam(value = "项目名称") @RequestParam(required = false) String name, @ApiParam(value = "城市Code") @RequestParam(required = false) String city,
            @ApiParam(value = "省代码") @RequestParam(required = false) String province, @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
            @ApiParam(value = "页码", defaultValue = "1") @RequestParam(required = false) String pageNo,
            @ApiParam(value = "每页显示的条数", defaultValue = "10") @RequestParam(required = false) String pageSize) throws Exception {

        // 封装查询参数
        Long createId = ShiroUtil.getCreateID();
        Response response = new Response();
        if (createId != null) {
            Paging<Map<String, String>> projectPager = mobileProjectService.getMyProjectList(createId, name, city, province, category, pageNo, pageSize);
            response.setData(projectPager);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }

    /**
     * 前台项目信息列表页
     *
     * @param name
     *            项目信息
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @RequestMapping(value = { "/site/sel_project_list" }, method = RequestMethod.GET)
    @ApiOperation(value = "根据条件查询项目分页信息", notes = "根据条件查询分页", response = Response.class)
    public Response selProjectList(@ApiParam(value = "项目名称") @RequestParam(required = false) String name, @ApiParam(value = "城市Code") @RequestParam(required = false) String city,
            @ApiParam(value = "省代码") @RequestParam(required = false) String province, @ApiParam(value = "项目类别") @RequestParam(required = false) String category,
            @ApiParam(value = "开工日期查询开始日期") @RequestParam(required = false) String startDateA, @ApiParam(value = "开工日期查询结束日期") @RequestParam(required = false) String startDateB,
            @ApiParam(value = "竣工日期查询开始日期") @RequestParam(required = false) String endDateA, @ApiParam(value = "竣工日期查询结束日期") @RequestParam(required = false) String endDateB,
            @ApiParam(value = "页码") @RequestParam(required = false, defaultValue = "1") String pageNo,
            @ApiParam(value = "每页显示的条数") @RequestParam(required = false, defaultValue = "10") String pageSize) throws Exception {
        // 查询项目信息
        Paging<Map<String, String>> projectPager = mobileProjectService.getProjectListByConditions(name, city, province, category, startDateA, startDateB, endDateA, endDateB, pageNo, pageSize);

        // TODO
        // 封装广告数据

        Map<String, Object> result = new HashMap<>();
        result.put("projectPager", projectPager);

        return new Response(result);
    }
}