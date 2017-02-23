package com.movision.facade.cart;

import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.rentdate.entity.Rentdate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/2/18 17:15
 */
@Service
public class CartFacade {

    @Autowired
    private CartService cartService;

    //商品加入购物车
    public int addGoodsCart(String userid, String goodsid, String comboid, String isdebug, String sum, String type, String rentdate) throws ParseException {

        int flag = 0;
        //首先需要检查用户选择的该套餐中包含的所有商品

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("goodsid", Integer.parseInt(goodsid));
        if (!StringUtils.isEmpty(comboid)) {
            parammap.put("comboid", Integer.parseInt(comboid));
        }
        parammap.put("isdebug", Integer.parseInt(isdebug));
        parammap.put("sum", Integer.parseInt(sum));
        parammap.put("intime", new Date());
        parammap.put("isdel", 0);
        parammap.put("type", Integer.parseInt(type));

        if (type.equals("0")) {
            //0 租赁
            //租用的商品存在租用日期的选择，所以加入购物车的重复商品不进行合并
            cartService.addGoodsCart(parammap);//商品加入购物车，返回主键-购物车id
            int cartid = (int) parammap.get("id");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] rentdates = rentdate.split(",");
            List<Rentdate> prentDateList = new ArrayList<>();
            for (int i = 0; i < rentdates.length; i++) {
                Rentdate redate = new Rentdate();
                redate.setCartid(cartid);
                redate.setRentdate(sdf.parse(rentdates[i]));
                redate.setIntime(new Date());
                prentDateList.add(redate);
            }
            cartService.addRentDate(prentDateList);//批量插入租赁日期

            if (cartid > 0)
                flag = 1;

        } else if (type.equals("1")) {
            //1 出售
            //首先检查购物车中有无该商品，如果有该商品合并，只累加商品数量
            int count = cartService.queryIsHave(parammap);

            if (count == 0) {
                cartService.addGoodsCart(parammap);//新增一条
            } else if (count >= 1) {
                cartService.addSum(parammap);//累加数量
            }

            flag = 1;
        }


        return flag;
    }

    //查询用户的购物车中所有商品
    public List<CartVo> queryCartByUser(String userid) {

        //一次查询所有商品（不分页且全部为自营商品）
        List<CartVo> cartList = cartService.queryCartByUser(Integer.parseInt(userid));

        //遍历购物车所有商品，当套餐id不为空时，需要查询套餐名称和套餐折后价，set到list中的对象里
        for (int i = 0; i < cartList.size(); i++) {
            if (cartList.get(i).getCombotype() != null) {
                //查询套餐名称和套餐折后价
                CartVo vo = cartService.queryNamePrice(cartList.get(i).getCombotype());
                cartList.get(i).setComboname(vo.getComboname());
                cartList.get(i).setComboprice(vo.getComboprice());
            }
            if (cartList.get(i).getType() == 0) {
                //如果是租赁的商品，需要将商品的租赁日期列表取出
                List<Rentdate> rentdateList = cartService.queryRentDateList(cartList.get(i).getId());
                cartList.get(i).setRentDateList(rentdateList);
            }
        }

        return cartList;
    }

    //用户删除购物车中的商品
    public void deleteCartGoods(String userid, String cartids) {
        //分割购物车id
        String[] cartidstr = cartids.split(",");
        int[] cartid = new int[cartidstr.length];
        for (int i = 0; i < cartidstr.length; i++) {
            cartid[i] = Integer.parseInt(cartidstr[i]);
        }
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("cartid", cartid);
        cartService.deleteCartGoods(parammap);
    }

    //用户修改购物车中的商品
    public int updateCartGoodsSum(String cartid, String type) {
        //先修改商品数量
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("cartid", Integer.parseInt(cartid));
        parammap.put("type", Integer.parseInt(type));
        cartService.updateCartGoodsSum(parammap);

        //再返回商品当前数
        int sum = cartService.queryGoodsSum(Integer.parseInt(cartid));
        return sum;
    }

    //修改购物车中单个商品的租赁日期
    public void updateCartGoodsRentDate(String cartid, String rentdate) throws ParseException {

        String[] rentdateArr = rentdate.split(",");
        Date[] rds = new Date[rentdateArr.length];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < rentdateArr.length; i++) {
            rds[i] = sdf.parse(rentdateArr[i]);
        }

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("cartid", Integer.parseInt(cartid));
        parammap.put("rds", rds);
        parammap.put("intime", new Date());
        //先删除
        cartService.deleteCartGoodsRentDate(parammap);
        //再插入
        cartService.updateCartGoodsRentDate(parammap);
    }
}
