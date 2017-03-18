package com.movision.mybatis.cart.mapper;

import com.movision.mybatis.cart.entity.Cart;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.rentdate.entity.Rentdate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    void addRentDate(List<Rentdate> prentDateList);

    int queryIsHaveRent(Map<String, Object> parammap);

    int queryIsHave(Map<String, Object> parammap);

    List<Cart> queryCartid(Map<String, Object> parammap);

    List<Rentdate> queryDateList(int cartid);

    List<Rentdate> queryRentDate(Map<String, Object> parammap);

    void addRentSum(Map<String, Object> parammap);

    void addSum(Map<String, Object> parammap);

    List<CartVo> queryCartByUser(int userid);

    String queryShopName(int shopid);

    List<Rentdate> queryRentDateList(int cartid);

    List<GoodsVo> queryGoodsByComboid(int comboid);

    void deleteCartGoods(Map<String, Object> parammap);

    int checkStore(int cartid);

    void updateCartGoodsSum(Map<String, Object> parammap);

    int queryGoodsSum(int cartid);

    void deleteCartGoodsRentDate(Map<String, Object> parammap);

    void updateCartGoodsRentDate(Map<String, Object> parammap);

    List<CartVo> queryCartVoList(int[] cartid);

    void batchDeleteCartGoods(int[] cartid);

    int addGoodsCart(Map<String, Object> parammap);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}