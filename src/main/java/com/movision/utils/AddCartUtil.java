package com.movision.utils;

import com.movision.mybatis.cart.entity.Cart;
import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.rentdate.entity.Rentdate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/3/16 20:42
 */
@Service
public class AddCartUtil {

    @Autowired
    private CartService cartService;

    /**
     * 立即租用，立即购买前加入购物车
     *
     * @param userid
     * @param goodsid
     * @param combotype
     * @param discountid
     * @param isdebug
     * @param num
     * @param type
     * @param rentdate
     * @return
     */
    @Transactional
    public int addGoodsCart(String userid, String goodsid, String combotype, String discountid, String isdebug, String num, int type, String rentdate) throws ParseException {

        int id = 0;//定义一个购物车id

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("goodsid", Integer.parseInt(goodsid));
        if (!StringUtils.isEmpty(combotype)) {
            parammap.put("comboid", Integer.parseInt(combotype));
        }
        if (!StringUtils.isEmpty(discountid)) {
            parammap.put("discountid", Integer.parseInt(discountid));
        }
        if (!isdebug.equals("")) {
            parammap.put("isdebug", Integer.parseInt(isdebug));
        }
        parammap.put("sum", Integer.parseInt(num));
        parammap.put("intime", new Date());
        parammap.put("isdel", 0);
        parammap.put("type", type);

        if (type == 0) {
            //0 租赁
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] rentdates = rentdate.split(",");

            //购物车中没有该商品，直接添加
            cartService.addGoodsCart(parammap);//商品加入购物车，返回主键-购物车id
            id = (int) parammap.get("id");

            List<Rentdate> prentDateList = new ArrayList<>();
            for (int i = 0; i < rentdates.length; i++) {
                Rentdate redate = new Rentdate();
                redate.setCartid(id);
                redate.setRentdate(sdf.parse(rentdates[i]));
                redate.setIntime(new Date());
                prentDateList.add(redate);
            }
            cartService.addRentDate(prentDateList);//批量插入租赁日期

        } else if (type == 1) {
            cartService.addGoodsCart(parammap);//新增一条
            id = (int) parammap.get("id");
        }
        return id;
    }
}
