package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.service.MobileProjectService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author tongxinglong
 * @date 2016/10/19 0019.
 */
@RestController
@RequestMapping("/rest/m/project/mc/")
public class MobileProjectMcController {
    @Autowired
    private MobileProjectService mobileProjectService;

    @RequestMapping(value = { "/sel_my_project_list" }, method = RequestMethod.GET)
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
}
