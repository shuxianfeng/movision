package com.movision.controller.app;

import com.movision.common.Response;
import com.movision.facade.mall.MallIndexFacade;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @RequestMapping(value = "index", method = RequestMethod.POST)
    public Response queryMonthHot() {
        Response response = new Response();

        List<GoodsVo> monthHotList = mallIndexFacade.queryMonthHot();

        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(monthHotList);
        return response;
    }
}
