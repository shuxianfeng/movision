package com.zhuhuibao.business.cooperation;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Cooperation;
import com.zhuhuibao.mybatis.memCenter.service.CooperationService;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by cxx on 2016/5/4 0004.
 */
@RestController
@RequestMapping("/rest/cooperation")
@Api(value="Cooperation", description="前台频道-合作")
public class CooperationSiteController {
    private static final Logger log = LoggerFactory .getLogger(CooperationSiteController.class);

    @Autowired
    private CooperationService cooperationService;


    /**
     * 查询任务列表（分页）
     */
    @ApiOperation(value="查询任务列表（分页）",notes="查询任务列表（分页）",response = JsonResult.class)
    @RequestMapping(value = "findAllCooperationByPager", method = RequestMethod.GET)
    public JsonResult findAllCooperationByPager(@RequestParam(required = false) String pageNo,@RequestParam(required = false) String pageSize,
        @ApiParam(value = "合作类型")@RequestParam(required = false) String type,
        @ApiParam(value = "项目类别")@RequestParam(required = false) String category,
        @ApiParam(value = "省")@RequestParam(required = false) String province,
        @ApiParam(value = "关键字")@RequestParam(required = false) String smart,
        @ApiParam(value = "会员类型，1：企业，2：个人")@RequestParam(required = false) String memberType,
        @ApiParam(value = "发布类型，1：接任务，2：接服务，3：资质合作")@RequestParam String parentId) throws IOException
    {
            if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<Cooperation> pager = new Paging<Cooperation>(Integer.valueOf(pageNo),Integer.valueOf(pageSize));
        Cooperation cooperation = new Cooperation();
        cooperation.setSmart(smart);
        cooperation.setType(type);
        cooperation.setCategory(category);
        cooperation.setMemberType(memberType);
        cooperation.setProvince(province);
        cooperation.setParentId(parentId);
        return cooperationService.findAllCooperationByPager(pager, cooperation);
    }
}
