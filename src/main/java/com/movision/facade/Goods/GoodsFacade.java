package com.movision.facade.Goods;

import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.combo.entity.Combo;
import com.movision.mybatis.combo.entity.ComboVo;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsDetail;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentCategery;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodsAssessmentImg.entity.GoodsAssessmentImg;
import com.movision.mybatis.goodsDiscount.entity.GoodsDiscountVo;
import com.movision.mybatis.goodsDiscount.service.DiscountService;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 根据商品id查询商品详情
     *
     * @param goodsid
     * @return
     */
    public GoodsDetail queryGoodDetail(String goodsid) {

        //首先查询用户基本信息
        GoodsDetail goodsDetail = goodsService.queryGoodDetail(Integer.parseInt(goodsid));

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

    public Map<String, Object> immediateRent(String comboid, String userid) {

        Map<String, Object> map = new HashMap<>();

        //先根据用户id查询该用户的所有地址列表
        List<Address> addressList = goodsService.queryAddressList(Integer.parseInt(userid));

        //根据套餐id和商品id查询，该套餐包含的所有商品
        List<GoodsVo> allRentGoodsList = goodsService.queryComboGoodsList(Integer.parseInt(comboid));

        //查询所有配送方式列表(临时屏蔽)

        //查询用户可用积分数
        int userpoint = userService.queryUserPoint(Integer.parseInt(userid));

        map.put("addressList", addressList);
        map.put("allRentGoodsList", allRentGoodsList);
        map.put("userpoint", userpoint);

        return map;
    }

    public List<Map> findAllMyCollectGoods(Paging<Map> pager, int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return goodsService.findAllMyCollectGoodsList(pager, map);
    }
}
