package com.movision.mybatis.cart.mapper;

import com.movision.mybatis.cart.entity.Cart;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.rentdate.entity.Rentdate;

import java.util.List;
import java.util.Map;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    void addRentDate(List<Rentdate> prentDateList);

    int queryIsHave(Map<String, Object> parammap);

    void addSum(Map<String, Object> parammap);

    List<CartVo> queryCartByUser(int userid);

    List<Rentdate> queryRentDateList(int cartid);

    void deleteCartGoods(Map<String, Object> parammap);

    void updateCartGoodsSum(Map<String, Object> parammap);

    int queryGoodsSum(int cartid);

    void deleteCartGoodsRentDate(Map<String, Object> parammap);

    void updateCartGoodsRentDate(Map<String, Object> parammap);

    int addGoodsCart(Map<String, Object> parammap);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}