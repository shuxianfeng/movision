package com.movision.facade.cart;

import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.rentdate.entity.Rentdate;
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

    public int addGoodsCart(String userid, String goodsid, String comboid, String isdebug, String sum, String type, String rentdate) throws ParseException {

        int flag = 0;
        //首先需要检查用户选择的该套餐中包含的所有商品

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("goodsid", Integer.parseInt(goodsid));
        parammap.put("comboid", Integer.parseInt(comboid));
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
}
