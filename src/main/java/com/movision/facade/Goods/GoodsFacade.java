package com.movision.facade.Goods;

import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/15 19:04
 */
@Service
public class GoodsFacade {

    @Autowired
    private GoodsService goodsService;

    /**
     * 根据商品id查询商品详情
     *
     * @param goodsid
     * @return
     */
    public GoodsDetail queryGoodDetail(String goodsid) {

        //首先查询用户基本信息
        GoodsDetail goodsDetail = goodsService.queryGoodDetail(Integer.parseInt(goodsid));

        //再查询用户商品实物图列表集合
        List<GoodsImg> goodsImgList = goodsService.queryGoodsImgList(Integer.parseInt(goodsid));
        goodsDetail.setGoodsImgList(goodsImgList);

        return goodsDetail;
    }
}
