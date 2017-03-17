package com.movision.facade.Goods;

import com.movision.facade.cart.CartFacade;
import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.collection.service.CollectionService;
import com.movision.mybatis.combo.entity.ComboVo;
import com.movision.mybatis.combo.service.ComboService;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentCategery;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscount;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscountVo;
import com.movision.mybatis.goodsDiscount.service.DiscountService;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.AddCartUtil;
import com.movision.utils.CalculateFee;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.movision.mybatis.collection.entity.Collection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author shuxf
 * @Date 2017/2/15 19:04
 */
@Service
public class GoodsFacade {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private CalculateFee calculateFee;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private AddCartUtil addCartUtil;

    @Autowired
    private CartService cartService;

    /**
     * 根据商品id查询商品详情
     *
     * @param goodsid
     * @return
     */
    public GoodsDetail queryGoodDetail(String goodsid, String userid) {

        //首先查询用户基本信息
        GoodsDetail goodsDetail = goodsService.queryGoodDetail(Integer.parseInt(goodsid));

        //根据店铺id查询店铺名称
        if (goodsDetail.getShopid() != -1) {
            String shopname = goodsService.queryShopnameById(goodsDetail.getShopid());
            goodsDetail.setShopname(shopname);
        } else {
            goodsDetail.setShopname("美番自营");
        }

        Collection collection = new Collection();
        collection.setUserid(Integer.parseInt(userid));
        collection.setGoodsid(Integer.parseInt(goodsid));
        collection.setType(1);
        //检查该用户是否收藏过该商品
        int count = collectionService.checkIsHaveGoods(collection);
        if (count == 0) {
            goodsDetail.setIscollect(0);//未收藏
        } else if (count > 0) {
            goodsDetail.setIscollect(1);//已收藏
        }

        //再查询用户商品实物图列表集合
        List<GoodsImg> goodsImgList = goodsService.queryGoodsImgList(Integer.parseInt(goodsid));
        goodsDetail.setGoodsImgList(goodsImgList);

        return goodsDetail;
    }

    public Map<String, Object> queryGoodsAssessment(String pageNo, String pageSize, String goodsid, String type) {

        Paging<GoodsAssessmentVo> pager = new Paging<>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        Map<String, Object> map = new HashMap();

        List<GoodsAssessmentVo> goodsAssessmentList = new ArrayList<>();

        if (type.equals("0")) {//查询全部评论
            goodsAssessmentList = goodsService.queryGoodsAssessment(pager, Integer.parseInt(goodsid));
        } else if (type.equals("1")) {//查询有图评论
            goodsAssessmentList = goodsService.queryImgGoodsAssessment(pager, Integer.parseInt(goodsid));
        } else if (type.equals("2")) {//查询质量好的评论
            goodsAssessmentList = goodsService.queryQualityGoodsAssessment(pager, Integer.parseInt(goodsid));
        } else if (type.equals("3")) {//查询送货快的评论
            goodsAssessmentList = goodsService.queryFastGoodsAssessment(pager, Integer.parseInt(goodsid));
        } else if (type.equals("4")) {//查询态度不错的评论
            goodsAssessmentList = goodsService.queryAttitudeGoodsAssessment(pager, Integer.parseInt(goodsid));
        } else if (type.equals("5")) {//查询质量一般的评论
            goodsAssessmentList = goodsService.queryQualityGeneral(pager, Integer.parseInt(goodsid));
        }
        pager.result(goodsAssessmentList);

        for (int i = 0; i < goodsAssessmentList.size(); i++) {
            //如果评论是有图的，查询评价图片列表
            if (goodsAssessmentList.get(i).getIsimage() == 1) {
                List<GoodsAssessmentImg> goodsAssessmentImgList = goodsService.queryGoodsAssessmentImg(goodsAssessmentList.get(i).getId());
                goodsAssessmentList.get(i).setGoodsAssessmentImgList(goodsAssessmentImgList);
            }
        }

        //查询该商品所有的官方回复信息
        List<GoodsAssessmentVo> officialReplyList = goodsService.queryAllOfficialReply(Integer.parseInt(goodsid));
        for (int i = 0; i < officialReplyList.size(); i++) {
            for (int j = 0; j < goodsAssessmentList.size(); j++) {
                if (goodsAssessmentList.get(j).getId() == officialReplyList.get(i).getPid()) {
                    goodsAssessmentList.get(j).setGoodsAssessmentVo(officialReplyList.get(i));
                }
            }
        }

        //查询各类评论的数量
        GoodsAssessmentCategery goodsAssessmentCategery = goodsService.queryAssessmentCategorySum(Integer.parseInt(goodsid));

        //输入评论列表标签汉字名称，减少IOS发布版本的频率
        goodsAssessmentCategery.setAllname("全部");
        goodsAssessmentCategery.setImgname("有图");
        goodsAssessmentCategery.setQualityname("质量好");
        goodsAssessmentCategery.setFastname("送货快");
        goodsAssessmentCategery.setAttitudename("态度不错");
        goodsAssessmentCategery.setQualitygeneralname("质量一般");

        map.put("goodsAssessmentList", pager);
        map.put("goodsAssessmentCategery", goodsAssessmentCategery);

        return map;
    }

    @Transactional
    public void insertGoodAssessment(String userid, String goodsid, String suborderid, String goodpoint, String logisticpoint,
                                     String servicepoint, String content, String isanonymity, String imgsurl) {
        //插入评论
        GoodsAssessment goodsAssessment = new GoodsAssessment();
        goodsAssessment.setUserid(Integer.parseInt(userid));
        goodsAssessment.setGoodid(Integer.parseInt(goodsid));
        goodsAssessment.setSuborderid(Integer.parseInt(suborderid));
        goodsAssessment.setGoodpoint(Integer.parseInt(goodpoint));
        goodsAssessment.setLogisticpoint(Integer.parseInt(logisticpoint));
        goodsAssessment.setServicepoint(Integer.parseInt(servicepoint));
        goodsAssessment.setContent(content);
        if (!StringUtils.isEmpty(imgsurl)) {
            goodsAssessment.setIsimage(1);//有图
        } else {
            goodsAssessment.setIsimage(0);//无图
        }
        goodsAssessment.setIsanonymity(Integer.parseInt(isanonymity));
        goodsAssessment.setCreatetime(new Date());
        goodsService.insertGoodAssessment(goodsAssessment);//插入评论

        int assessmentid = goodsAssessment.getId();//获取插入的主键id

        if (!StringUtils.isEmpty(imgsurl)) {//如果有图的话需要保存图片信息
            List<GoodsAssessmentImg> goodsAssessmentImgList = new ArrayList<>();
            String[] imgurlarray = imgsurl.split(",");
            for (int i = 0; i < imgurlarray.length; i++) {
                GoodsAssessmentImg goodsAssessmentImg = new GoodsAssessmentImg();
                goodsAssessmentImg.setAssessmentid(assessmentid);
                goodsAssessmentImg.setImgurl(imgurlarray[i]);
                goodsAssessmentImg.setOrderid(i);
                goodsAssessmentImgList.add(goodsAssessmentImg);
            }
            goodsService.insertGoodAssessmentImg(goodsAssessmentImgList);//插入晒单图片
        }
    }

    public Map<String, Object> queryCombo(String goodsid, String goodsposition) {
        Map<String, Object> map = new HashMap<>();
        //查询该商品有无库存
        int storenum = goodsService.queryStore(Integer.parseInt(goodsid));
        if (storenum > 0) {

            //查询商品小方图
            String imgurl = goodsService.queryGoodsImg(Integer.parseInt(goodsid));
            map.put("smallimg", imgurl);

            //查询套餐
            List<ComboVo> comboList = goodsService.queryCombo(Integer.parseInt(goodsid));
            //再查询套餐剩余库存(取套餐中商品库存的最小值)
            for (int i = 0; i < comboList.size(); i++) {
                //套餐库存
                int stork = goodsService.queryComboStork(comboList.get(i).getComboid());
                comboList.get(i).setStock(stork);
            }
            map.put("comboList", comboList);

            //查询活动
            Goods goods = new Goods();
            goods.setGoodsposition(Integer.parseInt(goodsposition));
            List<GoodsDiscountVo> goodsDiscountList = discountService.querygoodsDiscount(goods);

            for (int i = 0; i < goodsDiscountList.size(); i++) {

                Date startTime = goodsDiscountList.get(i).getStartdate();
                Date endTime = goodsDiscountList.get(i).getEnddate();
                Date now = new Date();

                if (now.after(startTime) && now.before(endTime)) {
                    goodsDiscountList.get(i).setStatus(1);//进行中
                } else if (now.before(startTime)) {
                    goodsDiscountList.get(i).setStatus(0);//未开始
                } else if (now.after(endTime)) {
                    goodsDiscountList.get(i).setStatus(2);//已结束
                }
            }
            map.put("goodsDiscountList", goodsDiscountList);
        }
        map.put("storenum", storenum);
        return map;
    }

    @Transactional
    public Map<String, Object> immediateRent(String goodsid, String userid, String combotype, String discountid, String rentdate, String isdebug, String num, String provincecode, String citycode) throws ParseException {

        Map<String, Object> map = new HashMap<>();//定义返回map
        int flag = 0;//定义标志位

        //校验商品是否下架
        int count = goodsService.queryIsOnline(Integer.parseInt(goodsid));
        if (count == 0) {
            map.put("code", -1);//商品已下架
            map.put("msg", "商品已下架");
            flag = -1;
        }

        //校验商品库存
        int stock = goodsService.queryStore(Integer.parseInt(goodsid));
        if (stock < Integer.parseInt(num)) {
            map.put("code", -2);//库存不足
            map.put("msg", "商品库存不足");
            flag = -1;
        }

        //校验套餐库存
        if (!StringUtils.isEmpty(combotype)) {
            //根据商品id查询套餐库存
            int combostoock = goodsService.queryAllStock(Integer.parseInt(combotype));
            if (combostoock < Integer.parseInt(num)) {
                map.put("code", -3);//套餐库存不足
                map.put("msg", "套餐库存不足");
                flag = -1;
            }
        }

        //校验活动是否合法
        if (!StringUtils.isEmpty(discountid)) {
            //根据活动id校验活动是否合法
            GoodsDiscount goodsDiscount = discountService.queryGoodsDiscountById(Integer.parseInt(discountid));
            if (null != goodsDiscount) {
                Date startdate = goodsDiscount.getStartdate();
                Date enddate = goodsDiscount.getEnddate();
                Date now = new Date();
                if (now.before(startdate) || now.after(enddate)) {
                    map.put("code", -4);
                    map.put("msg", "该商品参与的优惠活动不在活动期间");
                    flag = -1;
                }
            } else {
                map.put("code", -4);
                map.put("msg", "该商品参与的优惠活动已下架");
                flag = -1;
            }
        }

        //校验租赁日期
        if (!StringUtils.isEmpty(rentdate)) {
            String[] rentdatearr = rentdate.split(",");//租赁日期数组
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            String nowString = formatter.format(now);//当前日期字符串
            String rentdateString;//租赁日期字符串

            for (int j = 0; j < rentdatearr.length; j++) {
                rentdateString = rentdatearr[j];
                if (now.after(formatter.parse(rentdateString)) || nowString.equals(rentdateString)) {//当前日期大于或等于租赁日期时
                    map.put("code", -5);
                    map.put("msg", "商品租赁日期必须从次日起租");
                    flag = -1;
                }
            }
        }

        if (flag == 0) {
            //先根据用户id查询该用户的默认地址
            Address address = goodsService.queryDefaultAddress(Integer.parseInt(userid));
            //定义运费变量map
            Map<String, Object> feemap = new HashMap<>();

            //查询用户可用积分数
            int userpoint = userService.queryUserPoint(Integer.parseInt(userid));

            //根据商品id查询商品定位type
            int type = goodsService.queryGoodsPosition(Integer.parseInt(goodsid));

            //加入购物车并返回购物车id
            int cartid = addCartUtil.addGoodsCart(userid, goodsid, combotype, discountid, isdebug, num, type, rentdate);

            //根据购物车id查询cartVoList(购物车id封装到数组里，数组查询后封装到List<CartVo>，调用公共接口)
            int[] cartids = new int[1];
            cartids[0] = cartid;
            List<CartVo> cartVoList = cartService.queryCartVoList(cartids);

            //计算运费
            //根据判断条件来决定是否在这里进行运费结算----结算时调用计算运费的公共方法
            if (null != address) {//判空 防止空指针
                if (address.getProvince().equals(provincecode) && address.getCity().equals(citycode)) {
                    //调用公共计算接口计算运费
                    feemap = calculateFee.GetFee(cartVoList, address.getLng(), address.getLat());
                }
            } else {
                feemap.put("totalfee", 0.0);
            }

            map.put("code", 200);
            map.put("address", address);
            map.put("userpoint", userpoint);
            map.put("feemap", feemap);
            map.put("cartid", cartid);
        }
        return map;
    }

    public List<Map> findAllMyCollectGoods(Paging<Map> pager, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return goodsService.findAllMyCollectGoodsList(pager, map);
    }
}
