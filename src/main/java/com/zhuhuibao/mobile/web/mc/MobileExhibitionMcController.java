package com.zhuhuibao.mobile.web.mc;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.expo.entity.DistributedOrder;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.service.MobileExhibitionService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "我的活动", notes = "我的活动", response = Response.class)
    @RequestMapping(value = "sel_my_exhibition_list", method = RequestMethod.GET)
    public Response selMyExhibitionList(@ApiParam(value = "标题") @RequestParam(required = false) String title, @ApiParam(value = "审核状态") @RequestParam(required = false) String status,
            @ApiParam(value = "点击率排序，值为asc/desc") @RequestParam(required = false) String viewsOrder, @ApiParam(value = "发布时间排序，值为asc/desc") @RequestParam String publishTimeOrder,
            @RequestParam(required = false, defaultValue = "1") String pageNo, @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Map<String, Object> map = MapUtil.convert2HashMap("memberId", ShiroUtil.getCreateID(), "title", title, "status", status, "viewsOrder", viewsOrder, "publishTimeOrder", publishTimeOrder);
        Paging<Map<String, String>> pager = new Paging<Map<String, String>>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));

        List<Map<String, String>> exhibitionList = mobileExhibitionService.getMyExhibitionList(pager, map);
        pager.result(exhibitionList);

        return new Response(pager);
    }

    @ApiOperation(value = "我的活动", notes = "我的活动", response = Response.class)
    @RequestMapping(value = "sel_my_exhibition_detail", method = RequestMethod.GET)
    public Response selMyExhibitionList(@ApiParam(value = "信息ID") @RequestParam String exhibitionId) {
        Exhibition exhibition = mobileExhibitionService.getExhibitionById(exhibitionId);

        return new Response(exhibition);
    }

    @ApiOperation(value = "发布分布式会展定制", notes = "发布分布式会展定制", response = Response.class)
    @RequestMapping(value = "add_distributedOrder", method = RequestMethod.POST)
    public Response addDistributedOrder(@ModelAttribute DistributedOrder distributedOrder) {
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        if (createId != null) {
            distributedOrder.setCreateid(String.valueOf(createId));
            distributedOrder.setSource(Constants.DataSource.MOBILE.toString());
            mobileExhibitionService.addDistributedOrder(distributedOrder);
        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }
}
