package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.service.MobileExhibitionService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 会展controller
 * 
 * @author tongxinglong
 * @date 2016/10/19 0019.
 */
@RestController
@RequestMapping("/rest/m/exhibition/mc/")
public class MobileExhibitionMcController {

    @Autowired
    private MobileExhibitionService mobileExhibitionService;

    /**
     * 我的活动
     */
    @ApiOperation(value = "我的活动", notes = "我的活动", response = Response.class)
    @RequestMapping(value = "sel_my_exhibition_list", method = RequestMethod.GET)
    public Response selMyExhibitionList(@ApiParam(value = "标题") @RequestParam(required = false) String title, @ApiParam(value = "审核状态") @RequestParam(required = false) String status,
            @ApiParam(value = "点击率排序，值为asc/desc") @RequestParam String viewsOrder, @ApiParam(value = "发布时间排序，值为asc/desc") @RequestParam String publishTimeOrder,
            @RequestParam(required = false) String pageNo, @RequestParam(required = false) String pageSize) {
        Map<String, Object> map = MapUtil.convert2HashMap("memberId", ShiroUtil.getCreateID(), "title", title, "status", status, "viewsOrder", viewsOrder, "publishTimeOrder", publishTimeOrder);
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        List<Map<String, String>> exhibitionList = mobileExhibitionService.getMyExhibitionList(pager, map);
        pager.result(exhibitionList);

        return new Response(pager);
    }

    @ApiOperation(value = "我的活动", notes = "我的活动", response = Response.class)
    @RequestMapping(value = "sel_my_exhibition_detail", method = RequestMethod.GET)
    public Response selMyExhibitionList(@ApiParam(value = "标题") @RequestParam String exhibitionId) {
        Exhibition exhibition = mobileExhibitionService.getExhibitionById(exhibitionId);

        return new Response(exhibition);
    }
}
