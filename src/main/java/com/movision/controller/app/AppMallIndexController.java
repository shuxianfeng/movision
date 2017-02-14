package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.mall.MallIndexFacade;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/13 14:43
 * 用于提供APP商城首页所有接口
 */
@RestController
@RequestMapping("/app/mall/")
public class AppMallIndexController {
    @Autowired
    private MallIndexFacade mallIndexFacade;

    /**
     * 查询商城首页--月度热销榜单
     *
     * @return
     */
    @ApiOperation(value = "商城首页月度热销榜单返回接口", notes = "用户返回首页月度热销榜单数据", response = Response.class)
    @RequestMapping(value = "monthHot", method = RequestMethod.POST)
    public Response queryMonthHot() {
        Response response = new Response();

        Map<String, Object> map = mallIndexFacade.queryMonthHot();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 查询商城首页--月度热销榜单“查看全部”
     *
     * @return
     */
    @ApiOperation(value = "商城首页月度热销榜单--点击“查看全部”接口", notes = "点击查看全部跳转到全部月度热销榜单分页列表", response = Response.class)
    @RequestMapping(value = "allMonthHot", method = RequestMethod.POST)
    public Response queryAllMonthHot(@ApiParam(value = "第几页") @RequestParam(required = false) String pageNo,
                                     @ApiParam(value = "每页多少条") @RequestParam(required = false) String pageSize) {
        Response response = new Response();

        List<GoodsVo> allMonthHotList = mallIndexFacade.queryAllMonthHot(pageNo, pageSize);

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(allMonthHotList);
        return response;
    }
}
