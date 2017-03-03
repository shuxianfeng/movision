package com.movision.mybatis.cart.service;

import com.movision.mybatis.cart.entity.Cart;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.cart.mapper.CartMapper;
import com.movision.mybatis.combo.mapper.ComboMapper;
import com.movision.mybatis.goods.entity.GoodsVo;
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

    @Autowired
    private ComboMapper comboMapper;

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

    public int queryIsHaveRent(Map<String, Object> parammap) {
        try {
            log.info("查询该用户购物车中有没有该租赁的商品");
            return cartMapper.queryIsHaveRent(parammap);
        } catch (Exception e) {
            log.error("查询该用户购物车中有没有该租赁的商品失败");
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

    public List<Cart> queryCartid(Map<String, Object> parammap) {
        try {
            log.info("如果购物车中有多个只有租赁日期不同的商品，取出所有的购物车id");
            return cartMapper.queryCartid(parammap);
        } catch (Exception e) {
            log.error("如果购物车中有多个只有租赁日期不同的商品，取出所有的购物车id失败");
            throw e;
        }
    }

    public List<Rentdate> queryDateList(int cartid) {
        try {
            log.info("根据购物车id查询租赁日期列表");
            return cartMapper.queryDateList(cartid);
        } catch (Exception e) {
            log.error("根据购物车id查询租赁日期列表失败");
            throw e;
        }
    }

    public List<Rentdate> queryRentDate(Map<String, Object> parammap) {
        try {
            log.info("查询当前添加的商品租赁日期和购物中的商品租赁日期是否相同");
            return cartMapper.queryRentDate(parammap);
        } catch (Exception e) {
            log.error("查询当前添加的商品租赁日期和购物中的商品租赁日期是否相同失败");
            throw e;
        }
    }

    public void addRentSum(Map<String, Object> parammap) {
        try {
            log.info("添加购物车中租赁的商品的数量");
            cartMapper.addRentSum(parammap);
        } catch (Exception e) {
            log.error("添加购物车中租赁的商品的数量失败");
            throw e;
        }
    }

    public void addSum(Map<String, Object> parammap) {
        try {
            log.info("添加购物车中购买的商品的数量");
            cartMapper.addSum(parammap);
        } catch (Exception e) {
            log.error("添加购物车中购买的商品的数量失败");
            throw e;
        }
    }

    public List<CartVo> queryCartByUser(int userid) {
        try {
            log.info("查询该用户购物车中所有商品");
            return cartMapper.queryCartByUser(userid);
        } catch (Exception e) {
            log.error("查询该用户购物车中所有商品失败");
            throw e;
        }
    }

    public CartVo queryNamePrice(int comboid) {
        try {
            log.info("查询套餐名称和套餐折后价");
            return comboMapper.queryNamePrice(comboid);
        } catch (Exception e) {
            log.error("查询套餐名称和套餐折后价失败");
            throw e;
        }
    }

    public List<Rentdate> queryRentDateList(int cartid) {
        try {
            log.info("根据购物车id查询租赁商品的租赁日期");
            return cartMapper.queryRentDateList(cartid);
        } catch (Exception e) {
            log.error("根据购物车id查询租赁商品的租赁日期失败");
            throw e;
        }
    }

    public List<GoodsVo> queryGoodsByComboid(int comboid) {
        try {
            log.info("根据套餐id查询套餐中所有的商品库存");
            return cartMapper.queryGoodsByComboid(comboid);
        } catch (Exception e) {
            log.error("根据套餐id查询套餐中所有的商品库存失败");
            throw e;
        }
    }

    public void deleteCartGoods(Map<String, Object> parammap) {
        try {
            log.info("删除购物车中的商品");
            cartMapper.deleteCartGoods(parammap);
        } catch (Exception e) {
            log.error("删除购物车中的商品失败");
            throw e;
        }
    }

    public int checkStore(int cartid) {
        try {
            log.info("校验购物车中该商品的库存");
            return cartMapper.checkStore(cartid);
        } catch (Exception e) {
            log.error("校验购物车中该商品的库存失败");
            throw e;
        }
    }

    public void updateCartGoodsSum(Map<String, Object> parammap) {
        try {
            log.info("修改购物车中商品的数量");
            cartMapper.updateCartGoodsSum(parammap);
        } catch (Exception e) {
            log.error("修改购物车中商品的数量失败");
            throw e;
        }
    }

    public int queryGoodsSum(int cartid) {
        try {
            log.info("查询该商品在购物车中的数量");
            return cartMapper.queryGoodsSum(cartid);
        } catch (Exception e) {
            log.error("查询该商品在购物车中的数量失败");
            throw e;
        }
    }

    public void deleteCartGoodsRentDate(Map<String, Object> parammap) {
        try {
            log.info("删除该租用商品的历史租用日期");
            cartMapper.deleteCartGoodsRentDate(parammap);
        } catch (Exception e) {
            log.error("删除该租用商品的历史租用日期失败");
            throw e;
        }
    }

    public void updateCartGoodsRentDate(Map<String, Object> parammap) {
        try {
            log.info("插入修改后的商品租用日期");
            cartMapper.updateCartGoodsRentDate(parammap);
        } catch (Exception e) {
            log.error("插入修改后的商品租用日期失败");
            throw e;
        }
    }

    public List<CartVo> queryCartVoList(int[] cartid) {
        try {
            log.info("查询需要结算的购物车所有商品");
            return cartMapper.queryCartVoList(cartid);
        } catch (Exception e) {
            log.error("查询需要结算的购物车所有商品失败");
            throw e;
        }
    }
}
