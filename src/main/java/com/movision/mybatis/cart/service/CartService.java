package com.movision.mybatis.cart.service;

import com.movision.mybatis.cart.mapper.CartMapper;
import com.movision.mybatis.rentdate.entity.Rentdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/18 17:39
 */
@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    private Logger log = LoggerFactory.getLogger(CartService.class);

    public int addGoodsCart(Map<String, Object> parammap) {
        try {
            log.info("商品加入购物车");
            return cartMapper.addGoodsCart(parammap);
        } catch (Exception e) {
            log.error("商品加入购物车失败");
            throw e;
        }
    }

    public void addRentDate(List<Rentdate> prentDateList) {
        try {
            log.info("租赁商品加入购物车同时插入租赁日期");
            cartMapper.addRentDate(prentDateList);
        } catch (Exception e) {
            log.error("租赁商品加入购物车同时插入租赁日期失败");
            throw e;
        }
    }

    public int queryIsHave(Map<String, Object> parammap) {
        try {
            log.info("查询该用户购物车中有没有该商品");
            return cartMapper.queryIsHave(parammap);
        } catch (Exception e) {
            log.error("查询该用户购物车中有没有该商品失败");
            throw e;
        }
    }

    public void addSum(Map<String, Object> parammap) {
        try {
            log.info("添加购物车中商品的数量");
            cartMapper.addSum(parammap);
        } catch (Exception e) {
            log.error("添加购物车中商品的数量失败");
            throw e;
        }
    }
}
