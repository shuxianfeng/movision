package com.movision.utils;

import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/3/17 15:27
 * 工具类：用于提供 购物车结算接口 和 提交订单接口 中的判断商品库存和套餐库存
 */
@Service
public class CheckStock {

    @Autowired
    private GoodsService goodsService;

    public Map<String, Object> checkGoodsStock(List<CartVo> cartVoList, Map<String, Object> map) {

        List<Integer> goodsidList = new ArrayList<>();//定义整数list存储所有商品的商品id

        Map<Integer, Integer> stockmap = new HashMap<>();//定义stockmap用于存放商品和库存的归类映射

        //遍历购物车中所有的商品,归类计算
        for (int i = 0; i < cartVoList.size(); i++) {

            if (goodsidList.size() == 0) {
                //第一件商品，直接加入
                goodsidList.add(cartVoList.get(i).getGoodsid());//存商品id
                stockmap.put(cartVoList.get(i).getGoodsid(), cartVoList.get(i).getNum());//存商品id和库存映射
            } else {
                //已存过商品，先判断存不存在

                int mark = 0;//定义标志位，检查是否存在该商品
                for (int j = 0; j < goodsidList.size(); j++) {
                    if (goodsidList.get(j) == cartVoList.get(i).getGoodsid()) {
                        //如果已存在该商品
                        mark = 1;//设为1
                    }
                }

                if (mark == 0) {
                    //不存在直接放入
                    goodsidList.add(cartVoList.get(i).getGoodsid());//存商品id
                    stockmap.put(cartVoList.get(i).getGoodsid(), cartVoList.get(i).getNum());//存商品id和库存映射
                } else if (mark == 1) {
                    //存在直接取出累加，更新数值
                    int newnum = stockmap.get(cartVoList.get(i).getGoodsid()) + cartVoList.get(i).getNum();
                    stockmap.put(cartVoList.get(i).getGoodsid(), newnum);
                }
            }

        }

        for (int k = 0; k < goodsidList.size(); k++) {
            int sum = stockmap.get(goodsidList.get(k));//购买的该商品件数
            int stock = goodsService.queryStore(goodsidList.get(k));//根据商品id查询商品库存
            if (sum > stock) {
                map.put("stockcode", -2);
                map.put("stockgoodsid", goodsidList.get(k));
                map.put("stockmsg", "购买商品的总数量大于商品总库存");
            } else {
                map.put("stockcode", 200);
                map.put("stockmsg", "商品库存充足");
            }
        }

        return map;
    }
}
