package com.movision.facade.cart;

import com.movision.mybatis.cart.entity.Cart;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.goodsDiscount.service.DiscountService;
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

    @Autowired
    private DiscountService discountService;

    //商品加入购物车
    public int addGoodsCart(String userid, String goodsid, String comboid, String discountid, String isdebug, String sum, String type, String rentdate) throws ParseException {

        int flag = 0;

        int tag = 0;//用于区分购物车中租用的商品有没有增加过数量

        //首先需要检查用户选择的该套餐中包含的所有商品

        Map<String, Object> parammap = new HashMap<>();
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("goodsid", Integer.parseInt(goodsid));
        if (!StringUtils.isEmpty(comboid)) {
            parammap.put("comboid", Integer.parseInt(comboid));
        }
        if (!StringUtils.isEmpty(discountid)) {
            parammap.put("discountid", Integer.parseInt(discountid));
        }
        parammap.put("isdebug", Integer.parseInt(isdebug));
        parammap.put("sum", Integer.parseInt(sum));
        parammap.put("intime", new Date());
        parammap.put("isdel", 0);
        parammap.put("type", Integer.parseInt(type));

        if (type.equals("0")) {
            //0 租赁
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String[] rentdates = rentdate.split(",");

            int mark = 0;//定义一个标志位，用于记录两组日期的比较结果

            //首先检查购物车中有无该商品
            int count = cartService.queryIsHaveRent(parammap);

            int id = 0;//定义一个购物车id

            if (count > 0) {
                //如果有多个只是租赁日期不同的商品，取出所有购物车id
                List<Cart> cartList = cartService.queryCartid(parammap);
                for (int h = 0; h < cartList.size(); h++) {
                    mark = 0;
                    id = 0;
                    //遍历出所有的购物车id
                    int cartid = cartList.get(h).getId();
                    //根据购物车id查询，该购物车商品对应的所有租赁日期
                    List<Rentdate> rentdateList = cartService.queryDateList(cartid);

                    //有相同商品时验证租赁日期是否相同
//                    List<Rentdate> rentdateList = cartService.queryRentDate(parammap);
                    if (rentdateList.size() == rentdates.length) {

                        for (int i = 0; i < rentdateList.size(); i++) {
                            for (int j = 0; j < rentdates.length; j++) {
                                if (rentdateList.get(i).getRentdate().equals(sdf.parse(rentdates[j]))) {
                                    mark = mark + 1;
                                    System.out.println("打印mark>>>>>>>" + mark);
                                    if (mark == rentdates.length) {
                                        id = cartid;
                                        System.out.println("打印id>>>>>>>" + id);
                                    }
                                }
                            }
                        }

                    }

                    //如果有该商品且租赁日期一致，只累加商品数量
                    if (id != 0) {//mark == rentdates.length

                        //如果日期完全相同就累加商品数量
                        parammap.put("id", id);
                        cartService.addRentSum(parammap);//累加数量
                        tag = 1;
                        flag = 1;

                    }
                }

                if (id == 0 && tag == 0) {//因为多次循环中有不满足该条件的：“rentdateList.size() == rentdates.length”,所以mark为0或小于rentdates.length   mark <= rentdates.length

                    //如果选择的和购物车中已经存在的日期天数不同，直接新增商品
                    cartService.addGoodsCart(parammap);//商品加入购物车，返回主键-购物车id
                    int cartid2 = (int) parammap.get("id");

                    List<Rentdate> prentDateList = new ArrayList<>();
                    for (int i = 0; i < rentdates.length; i++) {
                        Rentdate redate = new Rentdate();
                        redate.setCartid(cartid2);
                        redate.setRentdate(sdf.parse(rentdates[i]));
                        redate.setIntime(new Date());
                        prentDateList.add(redate);
                    }
                    cartService.addRentDate(prentDateList);//批量插入租赁日期

                    if (cartid2 > 0)
                        flag = 1;
                }

            } else {
                //购物车中没有该商品，直接添加
                cartService.addGoodsCart(parammap);//商品加入购物车，返回主键-购物车id
                int cartid2 = (int) parammap.get("id");

                List<Rentdate> prentDateList = new ArrayList<>();
                for (int i = 0; i < rentdates.length; i++) {
                    Rentdate redate = new Rentdate();
                    redate.setCartid(cartid2);
                    redate.setRentdate(sdf.parse(rentdates[i]));
                    redate.setIntime(new Date());
                    prentDateList.add(redate);
                }
                cartService.addRentDate(prentDateList);//批量插入租赁日期

                if (cartid2 > 0)
                    flag = 1;
            }

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
            if (cartList.get(i).getDiscountid() != null) {
                //查询商品参加的活动名称和活动折扣百分比
                CartVo ov = discountService.queryDiscountName(cartList.get(i).getDiscountid());
                cartList.get(i).setDiscountname(ov.getDiscountname());
                cartList.get(i).setDiscount("0." + ov.getDiscount());
                cartList.get(i).setIsenrent(ov.getIsenrent());
                cartList.get(i).setRentday(ov.getRentday());
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
        //先做商品库存校验(检查是否有货)
        int store = cartService.checkStore(Integer.parseInt(cartid));
        if (store > 0) {
            //先修改商品数量
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("cartid", Integer.parseInt(cartid));
            parammap.put("type", Integer.parseInt(type));
            cartService.updateCartGoodsSum(parammap);

            //再返回商品当前数
            int sum = cartService.queryGoodsSum(Integer.parseInt(cartid));
            return sum;
        } else {
            return -1;//无库存
        }
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
