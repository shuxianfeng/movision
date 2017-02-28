package com.movision.facade.discount;

import com.movision.mybatis.goodsDiscount.entity.GoodsDiscountVo;
import com.movision.mybatis.goodsDiscount.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/2/28 13:39
 */
@Service
public class DiscountFacade {

    @Autowired
    private DiscountService discountService;

    public List<GoodsDiscountVo> querygoodsDiscount() {

        //查询当前商品参与的所有活动列表
        List<GoodsDiscountVo> goodsDiscountList = discountService.querygoodsDiscount();

        for (int i = 0; i < goodsDiscountList.size(); i++) {

            Date startTime = goodsDiscountList.get(i).getStartdate();
            Date endTime = goodsDiscountList.get(i).getEnddate();
            Date now = new Date();

            if (now.after(startTime) && now.before(endTime)) {
                goodsDiscountList.get(i).setStatus(1);//进行中
            } else if (now.before(startTime)) {
                goodsDiscountList.get(i).setStatus(0);//未开始
            } else if (now.after(endTime)) {
                goodsDiscountList.get(i).setStatus(2);//已结束
            }
        }

        return goodsDiscountList;
    }
}
