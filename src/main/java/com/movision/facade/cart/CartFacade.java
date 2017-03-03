package com.movision.facade.cart;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.service.AddressService;
import com.movision.mybatis.cart.entity.Cart;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.combo.service.ComboService;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.service.CouponService;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
import com.movision.mybatis.goodsDiscount.service.DiscountService;
import com.movision.mybatis.rentdate.entity.Rentdate;
import com.movision.mybatis.user.service.UserService;
import org.apache.commons.beanutils.converters.DoubleConverter;
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

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private ComboService comboService;

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
                cartList.get(i).setIsenrent(ov.getIsenrent());//是否为整租
                cartList.get(i).setEnrentday(ov.getEnrentday());//整租天数
            }
            if (cartList.get(i).getType() == 0) {
                //如果是租赁的商品，需要将商品的租赁日期列表取出
                List<Rentdate> rentdateList = cartService.queryRentDateList(cartList.get(i).getId());
                cartList.get(i).setRentday(rentdateList.size());//租赁天数
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

    //购物车结算校验和订单确认页数据返回
    public Map<String, Object> cartBilling(String cartids, String userid, String totalprice) {
        int flag = 0;
        double totalamount = 0;//订单总金额(=订单中自营商品的总金额+订单中第三方商品的总金额)
        double selfamount = 0;//订单中自营商品的总金额
        double shopamount = 0;//订单中第三方商品的总金额

        Map<String, Object> map = new HashMap<>();

        //首先根据cartids取出用户需要结算的所有商品
        String[] cartidarr = cartids.split(",");
        int[] cartid = new int[cartidarr.length];
        for (int i = 0; i < cartidarr.length; i++) {
            cartid[i] = Integer.parseInt(cartidarr[i]);
        }
        List<CartVo> cartVoList = cartService.queryCartVoList(cartid);//查询需要结算的购物车所有商品

        for (int i = 0; i < cartVoList.size(); i++) {

            int id = cartVoList.get(i).getId();//购物车id

            //1.校验所有商品库存
            if (cartVoList.get(i).getStock() < cartVoList.get(i).getNum()) {
                map.put("stockcode", -2);
                map.put("stockcartid", id);
                map.put("stockmsg", "商品库存不足");
                flag = 1;
            }

            //2.校验租赁商品的租赁日期
            if (cartVoList.get(i).getType() == 0) {//判断为租赁时才进行校验
                List<Rentdate> rentdateList = cartService.queryRentDateList(cartVoList.get(i).getId());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                String nowString = formatter.format(now);
                String rentdateString = formatter.format(rentdateList.get(i).getRentdate());
                for (int j = 0; j < rentdateList.size(); j++) {
                    if (now.after(rentdateList.get(i).getRentdate()) || nowString.equals(rentdateString)) {
                        map.put("rentdatecode", -3);
                        map.put("rentdatecartid", id);
                        map.put("rentdatemsg", "商品租赁日期必须从次日起租");
                        flag = 1;
                    }
                }
            }

            //3.校验商品套餐的库存
            if (cartVoList.get(i).getCombotype() != null) {
                //根据套餐id查询该套餐中所有的商品的库存
                List<GoodsVo> goodsVos = cartService.queryGoodsByComboid(cartVoList.get(i).getCombotype());
                for (int k = 0; k < goodsVos.size(); k++) {
                    if (goodsVos.get(k).getStock() < cartVoList.get(i).getNum()) {
                        map.put("combocode", -4);
                        map.put("combocartid", id);
                        map.put("combomsg", "该商品套餐中包含库存不足的商品");
                        flag = 1;
                    }
                }
            }

            //4.校验商品活动的起止日期（分为整租活动和非整租活动）
            if (cartVoList.get(i).getDiscountid() != null) {
                //根据活动id查询活动的开始时间和结束时间
                GoodsDiscount goodsDiscount = discountService.queryGoodsDiscountById(cartVoList.get(i).getDiscountid());
                Date startdate = goodsDiscount.getStartdate();
                Date enddate = goodsDiscount.getEnddate();
                Date now = new Date();
                if (now.before(startdate) || now.after(enddate)) {
                    map.put("discountcode", -5);
                    map.put("discountcartid", id);
                    map.put("discountmsg", "该商品参与的优惠活动不在活动期间");
                    flag = 1;
                }
            }

            if (flag == 0) {//如果上面的校验全部通过，进行如下操作

                //5.计算选择的结算商品的总价格（与APP入参核对）
                if (cartVoList.get(i).getType() == 0) {//租赁
                    //查询租赁日期列表
                    List<Rentdate> rentdateList = cartService.queryRentDateList(id);

                    if (cartVoList.get(i).getCombotype() != null) {//包含套餐
                        //查询套餐的折后价
                        double comboprice = comboService.queryComboPrice(cartVoList.get(i).getCombotype());
                        if (cartVoList.get(i).getDiscountid() != null) {
                            //查询购物车商品参加活动的活动折扣
                            String discount = discountService.queryDiscount(cartVoList.get(i).getDiscountid());
                            //单个套餐总价=套餐价*套餐件数*天数*活动百分比（有活动）
                            double amount = comboprice * cartVoList.get(i).getNum() * rentdateList.size() * Integer.parseInt(discount) / 100;
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }

                        } else {
                            //单个套餐总价=套餐价*套餐件数*天数（无活动）
                            double amount = comboprice * cartVoList.get(i).getNum() * rentdateList.size();
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }

                        }
                    } else {//不包含套餐
                        if (cartVoList.get(i).getDiscountid() != null) {
                            //单个商品总价=商品折后价*商品件数*天数*活动百分比（有活动）
                            double amount = cartVoList.get(i).getGoodsprice() * cartVoList.get(i).getNum() * rentdateList.size() * Integer.parseInt(cartVoList.get(i).getDiscount()) / 100;
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }
                        } else {
                            //单个商品总价=商品折后价*商品件数*天数（无活动）
                            double amount = cartVoList.get(i).getGoodsprice() * cartVoList.get(i).getNum() * rentdateList.size();
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }
                        }
                    }

                } else if (cartVoList.get(i).getType() == 1) {
                    //购买
                    if (cartVoList.get(i).getCombotype() != null) {//包含套餐
                        if (cartVoList.get(i).getDiscountid() != null) {
                            //单个套餐总价=套餐价*套餐件数*活动百分比（有活动）
                            double amount = cartVoList.get(i).getComboprice() * cartVoList.get(i).getNum() * Integer.parseInt(cartVoList.get(i).getDiscount()) / 100;
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }
                        } else {
                            //单个套餐总价=套餐价*套餐件数（无活动）
                            double amount = cartVoList.get(i).getComboprice() * cartVoList.get(i).getNum();
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }
                        }
                    } else {//不包含套餐
                        if (cartVoList.get(i).getDiscountid() != null) {//有活动
                            //单个商品总价=商品折后价*商品件数*活动百分比（有活动）
                            double amount = cartVoList.get(i).getGoodsprice() * cartVoList.get(i).getNum() * Integer.parseInt(cartVoList.get(i).getDiscount()) / 100;
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }
                        } else {//无活动
                            //单个商品总价=商品折后价*商品件数（无活动）
                            double amount = cartVoList.get(i).getGoodsprice() * cartVoList.get(i).getNum();
                            totalamount = totalamount + amount;

                            if (cartVoList.get(i).getIsself() == 1) {//自营
                                selfamount = selfamount + amount;
                            } else {//三方
                                shopamount = shopamount + amount;
                            }
                        }
                    }
                }

            }
        }

        if (totalamount != Double.parseDouble(totalprice)) {
            System.out.println("服务器计算的结算金额>>>>>>>>>>" + totalamount);
            System.out.println("客户端提交的结算金额>>>>>>>>>>" + Double.parseDouble(totalprice));
            map.put("code", -1);

            map.put("msg", "你提交的结算总价和服务端校验的总价不一致");
        } else {
            //6.查询用户的默认地址
            Address address = addressService.queryDefaultAddress(Integer.parseInt(userid));
            map.put("address", address);

            //7.按照最优算法，计算最佳优惠券（以折扣最多为最佳）
            //**************************************计算过程过于复杂，1.0版本暂时不做最佳优惠券推荐************************************

            //8.用户当前可用积分
            int points = userService.queryUserByPoints(userid);
            map.put("points", points);

            map.put("selfamount", selfamount);//自营总额
            map.put("shopamount", shopamount);//三方总额

            map.put("code", 200);
            map.put("msg", "您提交的购物车结算商品校验通过，可结算");
        }

        return map;
    }
}
