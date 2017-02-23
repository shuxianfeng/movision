package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.GoodsListFacade;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/2/23 9:51
 */
@RestController
@RequestMapping("/boss/goods")
public class GoodsController {

    @Autowired
    GoodsListFacade goodsFacade = new GoodsListFacade();


    /**
     * 商品管理*--商品列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品列表（分页）", notes = "商品列表（分页）", response = Response.class)
    @RequestMapping(value = "query_goods_list", method = RequestMethod.POST)
    public Response queryGoodsList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<GoodsVo> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsVo> list = goodsFacade.queryGoodsList(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 商品管理-删除商品
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除商品", notes = "删除商品", response = Response.class)
    @RequestMapping(value = "delete_goods", method = RequestMethod.POST)
    public Response deleteGoods(@ApiParam(value = "商品编号") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = goodsFacade.deleteGoods(id);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(result);
        return response;
    }

}
