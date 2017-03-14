package com.movision.facade.boss;

import com.ibm.icu.text.SimpleDateFormat;
import com.movision.mybatis.combo.entity.Combo;
import com.movision.mybatis.combo.entity.ComboVo;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsCom;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodscombo.entity.GoodsCombo;
import com.movision.mybatis.goodscombo.entity.GoodsComboDetail;
import com.movision.mybatis.goodscombo.entity.GoodsComboVo;
import com.movision.mybatis.orderoperation.entity.Orderoperation;
import com.movision.utils.pagination.model.Paging;
import com.movision.utils.pagination.util.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NamedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @Author zhanglei
 * @Date 2017/2/23 9:45
 */
@Service
public class GoodsListFacade {
    @Autowired
    GoodsService goodsService = new GoodsService();
    private static Logger log = LoggerFactory.getLogger(GoodsListFacade.class);
    @Value("#{configProperties['img.domain']}")
    private String imgdomain;
    /**
     * 商品管理--查询商品
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> queryGoodsList(Paging<GoodsVo> pager) {
        List<GoodsVo> list = goodsService.queryGoodsList(pager);
        return list;
    }


    /**
     * 商品管理--删除商品
     *
     * @param id
     * @return
     */
    public int deleteGoods(Integer id) {
        return goodsService.deleteGoods(id);
    }

    /**
     * 商品管理--删除评价
     *
     * @param id
     * @return
     */
    public int deleteAssessment(Integer id) {
        return goodsService.deleteAssessment(id);
    }


    /**
     * 商品管理--上架
     *
     * @param id
     * @return
     */
    public int queryByGoods(Integer id) {
        int isdel = goodsService.queryisdel(id);
        int result = 0;
        if (isdel == 1) {
            result = goodsService.queryByGoods(id);
        } else if (isdel == 0) {
            result = goodsService.queryByGoodsDown(id);
        }
        return result;
    }


    /**
     * 商品管理--推荐到热门
     * @param id
     * @return
     */
    public int queryHot(Integer id) {
        return goodsService.queryHot(id);
    }

    /**
     * 商品管理--推荐到精选
     *
     * @param id
     * @return
     */
    public int queryisessence(Integer id) {
        return goodsService.queryisessence(id);
    }

    /**
     * 商品管理--条件查询
     *
     * @param name
     * @param producttags
     * @param brand
     * @param protype
     * @param isdel
     * @param allstatue
     * @param minorigprice
     * @param maxorigprice
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    public List<GoodsVo> queryGoodsCondition(String name, String producttags, String brand, String protype, String isdel, String allstatue, String minorigprice, String maxorigprice,
                                             String pai, String mintime, String maxtime, Paging<GoodsVo> pager) {

        Map<String, Object> map = new HashedMap();
        if (name != null) {
            map.put("name", name);
        }
        if (producttags != null) {
            map.put("producttags", producttags);
        }
        if (brand != null) {
            map.put("band", brand);
        }
        if (protype != null) {
            map.put("protype", protype);
        }
        if (isdel != null) {
            map.put("isdel", isdel);
        }
        if (allstatue != null) {
            map.put("allstatue", allstatue);
        }
        if (minorigprice != null) {
            map.put("minorigprice", minorigprice);
        }
        if (maxorigprice != null) {
            map.put("maxorigprice", maxorigprice);
        }

        Date isessencetime = null;//开始时间
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {
                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        if (pai != null) {
            map.put("pai", pai);
        }
        return goodsService.queryGoodsCondition(map, pager);
    }

    /**
     * 商品管理--修改推荐日期
     *
     * @param id
     * @param recommenddate
     * @return
     */
    public Map<String, Integer> updateDate(String id, String recommenddate) {
        Map<String, Integer> map = new HashedMap();
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setId(Integer.parseInt(id));
        Date date = null;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (recommenddate != null) {
            try {
                date = format.parse(recommenddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsVo.setRecommenddate(date);
        int result = goodsService.updateDate(goodsVo);
        map.put("result", result);
        return map;
    }


    /**
     * 查询所有品牌
     *
     * @return
     */
    public List<GoodsVo> queryAllBrand() {
        List<GoodsVo> goodsVo = goodsService.findAllBrand();
        return goodsVo;
    }

    /**
     * 查询所有商品类别
     *
     * @return
     */
    public List<GoodsVo> queryAllType() {
        List<GoodsVo> goodsVo = goodsService.findAllType();
        return goodsVo;
    }

    /**
     * 根据id查商品
     *
     * @param id
     * @return
     */
    public GoodsVo queryGoodDetail(Integer id) {
        return goodsService.findGoodDetail(id);
    }

    /**
     * 修改商品
     *
     * @param imgurl
     * @param name
     * @param protype
     * @param id
     * @param price
     * @param origprice
     * @param stock
     * @param isdel
     * @param
     * @param recommenddate
     * @param brandid
     * @return
     */
    public Map<String, Object> updateGoods(String imgurl, String name, String protype, String id, String price, String origprice, String stock, String isdel, String recommenddate, String brandid, String ishot, String isessence, String attribute) {
        GoodsVo goodsVo = new GoodsVo();
        Map<String, Object> map = new HashedMap();
        goodsVo.setId(Integer.parseInt(id));
        if (name != null) {
            goodsVo.setName(name);
        }
        if (protype != null) {
            goodsVo.setProtype(Integer.parseInt(protype));
        }
        if (origprice != null) {
            goodsVo.setOrigprice(Double.parseDouble(origprice));
        }
        if (price != null) {
            goodsVo.setPrice(Double.parseDouble(price));
        }
        if (stock != null) {
            goodsVo.setStock(Integer.parseInt(stock));
        }
        if (isdel != null) {
            goodsVo.setIsdel(Integer.parseInt(isdel));
        }
        if (brandid != null) {
            goodsVo.setBrandid(brandid);
        }
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (recommenddate != null) {
            try {
                date = format.parse(recommenddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int re = 0;
        if (recommenddate == null) {
            re = goodsService.updateCom(Integer.parseInt(id));
        }
        goodsVo.setRecommenddate(date);
        /**String ishot;
        String productids[] = tuijian.split(",");
        for (int i = 0; i < productids.length; i++) {
            ishot = productids[i];
         if (ishot.equals("")) {
                goodsVo.setIshot(0);
                goodsVo.setIsessence(0);
         } else if (ishot.equals("1")) {
                goodsVo.setIshot(1);
         } else if (ishot.equals("2")) {
                goodsVo.setIsessence(1);
            }
         }*/
        if (ishot != null) {
            goodsVo.setIshot(Integer.parseInt(ishot));
        }
        if (isessence != null) {
            goodsVo.setIsessence(Integer.parseInt(isessence));
        }
        if (attribute != null) {
            goodsVo.setAttribute(attribute);
        }
        if (imgurl != null) {
            map.put("imgurl", imgurl);
        }
        map.put("id", id);
        int result = goodsService.updateGoods(goodsVo);
        int res = goodsService.updateImage(map);
            map.put("result", result);
            map.put("res", res);
        map.put("re", re);
        return map;
    }

    /**
     * 取消今日推荐
     *
     * @param id
     * @return
     */
    public int updateCom(Integer id) {
        return goodsService.updateCom(id);
    }

    /**
     * 今日推荐
     *
     * @param id
     * @return
     */
    public Goods todayCommend(Integer id) {
        return goodsService.todayCommend(id);
    }

    /**
     * 评价列表
     *
     * @param pager
     * @return
     */
    public List<GoodsAssessmentVo> queryAllAssessment(Paging<GoodsAssessmentVo> pager, String goodsid) {
        List<GoodsAssessmentVo> list = goodsService.queryAllAssessment(pager, goodsid);
        for (int i = 0; i < list.size(); i++) {
            String pid = list.get(i).getId().toString();
            if (!StringUtils.isEmpty(pid)) {
                int result = goodsService.queryAssessment(Integer.parseInt(pid));
                list.get(i).setResult(result);
            }
        }
        return list;
    }

    /**
     * 条件查询
     *
     * @param nickname
     * @param content
     * @param mintime
     * @param maxtime
     * @return
     */
    public List<GoodsAssessmentVo> queryAllAssessmentCondition(String goodsid, String nickname, String content, String pai, String mintime, String maxtime, Paging<GoodsAssessmentVo> pager) {
        Map<String, Object> map = new HashedMap();
        if (goodsid != null) {
            map.put("goodsid", goodsid);
        }
        if (nickname != null) {
            map.put("nickname", nickname);
        }
        if (content != null) {
            map.put("content", content);
        }
        if (pai != null) {
            map.put("pai", pai);
        }
        Date isessencetime = null;//开始时间
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {
                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        return goodsService.queryAllAssessmentCodition(map, pager);
    }

    /**
     * 评价详情
     *
     * @param id
     * @return
     */
    public GoodsAssessmentVo queryAssessmentRemark(Integer id) {
        return goodsService.queryAssessmentRemark(id);
    }


    /**
     * 商品参数图
     *
     * @param goodsid
     * @return
     */
    public GoodsImg queryImgGoods(Integer goodsid) {
        return goodsService.queryImgGoods(goodsid);
    }

    /**
     * 商品描述图
     *
     * @param goodsid
     * @return
     */
    public GoodsImg queryCommodityDescription(Integer goodsid) {
        return goodsService.queryCommodityDescription(goodsid);
    }

    /**
     * 商品图
     *
     * @param goodsid
     * @return
     */
    public List<GoodsImg> queryAllGoodsPicture(Integer goodsid) {
        return goodsService.queryAllGoodsPicture(goodsid);
    }
    /**
     * 晒图
     *
     * @param id
     * @return
     */
    public List<GoodsImg> queryblueprint(Integer id) {
        return goodsService.queryblueprint(id);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    public Integer deleteGoodsPicture(Integer id) {
        return goodsService.deleteGoodsPicture(id);
    }

    /**
     * 回复评论
     *
     * @param content
     * @param
     * @param
     * @return
     */
    public Map<String, Integer> addAssessment(String content, String goodid, String pid) {
        int result = 0;
        Map<String, Integer> map = new HashedMap();
            GoodsAssessment goodsAssessment = new GoodsAssessment();
            goodsAssessment.setUserid(-1);
            goodsAssessment.setContent(content);
            goodsAssessment.setGoodid(Integer.parseInt(goodid));
            goodsAssessment.setCreatetime(new Date());
            goodsAssessment.setIsimage(0);
            goodsAssessment.setIsanonymity(0);
            goodsAssessment.setPid(Integer.parseInt(pid));
            result = goodsService.addAssessment(goodsAssessment);
        map.put("result", result);
        return map;
    }

    /**
     * 增加图片
     *
     * @param
     * @param goodsid
     * @param img_url
     * @return
     */
    public Map<String, Integer> addpicture(String goodsid, String img_url, String oderid) {
        int res = goodsService.deletebanner(goodsid);
        Map<String, Integer> map = new HashedMap();
        GoodsImg goodsImg = new GoodsImg();
        goodsImg.setType(0);
        goodsImg.setGoodsid(Integer.parseInt(goodsid));
        String imgurls;
        String productids[] = img_url.split(",");
        String oderids;
        String orderid[] = oderid.split(",");
        int result = 0;
        for (int i = 0; i < productids.length; i++) {
            imgurls = productids[i];
            oderids = orderid[i];
            goodsImg.setImgurl(imgurls);
            goodsImg.setOderid(Integer.parseInt(oderids));
            result = goodsService.addPicture(goodsImg);
        }
        map.put("result", result);
        map.put("res", res);
        return map;
    }

    /**
     * 修改参数图
     *
     * @param goodsid
     * @param img_url
     * @return
     */
    public Map<String, Integer> updateImgGoods(String goodsid, String img_url) {
        Map<String, Integer> map = new HashedMap();
        GoodsImg img = new GoodsImg();
        img.setGoodsid(Integer.parseInt(goodsid));

        img.setImgurl(img_url);
            int result = goodsService.updateImgGoods(img);
            map.put("result", result);

        return map;
    }

    /**
     * 修改描述图
     *
     * @param goodsid
     * @param img_url
     * @return
     */
    public Map<String, Integer> updateCommodityDescription(String goodsid, String img_url) {
        Map<String, Integer> map = new HashedMap();
        GoodsImg img = new GoodsImg();
        img.setGoodsid(Integer.parseInt(goodsid));

        img.setImgurl(img_url);
        int result = goodsService.updateCommodityDescription(img);
        map.put("result", result);

        return map;
    }

    /**
     * 商品添加
     *
     * @param
     * @param img_url
     * @param
     * @param name
     * @param
     * @param protype
     * @param brandid
     * @param price
     * @param origprice
     * @param
     * @param stock
     * @param isdel
     * @param recommenddate
     * @param attribute
     * @param
     * @return
     */
    public Map<String, Integer> addGoods(String img_url, String name, String protype, String brandid, String price, String origprice, String stock, String isdel, String recommenddate, String attribute, String ishot, String isessence) {
        Map<String, Integer> map = new HashedMap();
        GoodsVo goodsVo = new GoodsVo();
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (recommenddate != null) {
            try {
                date = format.parse(recommenddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsVo.setRecommenddate(date);
        goodsVo.setIsdamage(0);
        goodsVo.setIsquality(1);
        goodsVo.setGoodsposition(0);
        goodsVo.setIscombo(0);
        goodsVo.setIsspecial(1);
        goodsVo.setIsseckill(0);
        goodsVo.setIshomepage(0);
        goodsVo.setOnlinetime(new Date());
        goodsVo.setShopid(-1);
        goodsVo.setBrandid(brandid);
        goodsVo.setProtype(Integer.parseInt(protype));
        goodsVo.setPrice(Double.parseDouble(price));
        goodsVo.setOrigprice(Double.parseDouble(origprice));
        goodsVo.setName(name);
        goodsVo.setIsdel(Integer.parseInt(isdel));
        goodsVo.setStock(Integer.parseInt(stock));
        goodsVo.setProvincecode("320000");
        goodsVo.setCitycode("320100");
        /** String ishot;
        String productids[] = tuijian.split(",");
        for (int i = 0; i < productids.length; i++) {
            ishot = productids[i];
            if (ishot == "0") {
                goodsVo.setIshot(0);
                goodsVo.setIsessence(0);
            } else if (ishot == "1") {
                goodsVo.setIshot(1);
            } else if (ishot == "2") {
                goodsVo.setIsessence(1);
            }
         }*/
        goodsVo.setIshot(Integer.parseInt(ishot));
        goodsVo.setIsessence(Integer.parseInt(isessence));
        goodsVo.setAttribute(attribute);
        int res = goodsService.addGoods(goodsVo);
        int id = goodsVo.getId();
        map.put("res", res);
        GoodsImg img = new GoodsImg();
        img.setGoodsid(id);
        img.setType(2);
        img.setImgurl(img_url);
        int result = goodsService.addGoodsPic(img);
            map.put("result", result);
        map.put("id", id);
        return map;
    }

    /**
     * 套餐列表
     *
     * @param pager
     * @return
     */
    public List<GoodsComboVo> queryCom(Paging<GoodsComboVo> pager) {
        List<GoodsComboVo> list = goodsService.findAllCombo(pager);
        for (int i = 0; i < list.size(); i++) {
            List<GoodsComboVo> good = goodsService.findAllC(list.get(i).getComboid());
            list.get(i).setList(good);
        }
        return list;
    }


    /**
     * 根据id查询商品
     *
     * @param comboid
     * @return
     */
    public List<GoodsComboVo> queryName(Integer comboid) {
        return goodsService.queryName(comboid);
    }

    /**
     * 删除套餐
     *
     * @param comboid
     * @return
     */
    public int deleteComGoods(Integer comboid) {
        int result = goodsService.queryByCom(comboid);
        int re = 0;
        if (result >= 1) {
            return re;
        } else {
            re = goodsService.deleteComGoods(comboid);
        }
        return re;
    }

    /**
     * 条件搜索
     *
     * @param comboname
     * @param name
     * @param allstatue
     * @param minrex
     * @param maxrex
     * @param mintime
     * @param maxtime
     * @param pager
     * @return
     */
    public List<GoodsComboVo> findAllComCondition(String comboname, String name, String allstatue, String comboid, String minrex, String maxrex, String mintime, String maxtime, String pai, Paging<GoodsComboVo> pager) {
        Map<String, Object> map = new HashedMap();
        if (comboname != null) {
            map.put("comboname", comboname);
        }
        if (name != null) {
            map.put("name", name);
        }

            map.put("allstatue", allstatue);

        if (comboid != null) {
            map.put("comboid", comboid);
        }
        if (minrex != null) {
            map.put("minrex", minrex);
        }
        if (maxrex != null) {
            map.put("maxrex", maxrex);
        }
        Date isessencetime = null;//开始时间
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {
                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        if (pai != null) {
            map.put("pai", pai);
        }
        List<GoodsComboVo> list = goodsService.findAllComCondition(map, pager);
        for (int i = 0; i < list.size(); i++) {
            List<GoodsComboVo> good = goodsService.findAllC(list.get(i).getComboid());
            list.get(i).setList(good);
        }
        return list;
    }

    /**
     * 根据id查询套餐
     *
     * @param comboid
     * @return
     */
    public List<GoodsComboVo> findByIdCom(Integer comboid) {
        List<GoodsComboVo> list = goodsService.findByIdCom(comboid);
        for (int i = 0; i < list.size(); i++) {
            List<GoodsComboVo> good = goodsService.findAllC(list.get(i).getComboid());
            list.get(i).setList(good);
        }
        return list;
    }

    /**
     * 根据套餐id查询商品
     *
     * @param comboid
     * @param
     * @return
     */
    public Map<String, Object> findAllGoods(Integer comboid) {
        Map<String, Object> map = new HashedMap();
        List<GoodsCom> list = goodsService.findAllGoods(comboid);
        Double origprice = 0.0;
        int sales = 0;
        int stock = 0;
        Double price = 0.0;
        Double sumprice = 0.0;
        Double sumorigprice = 0.0;
        int sumsales = 0;
        int sumstock = 0;
        for (int i = 0; i < list.size(); i++) {
            int re = goodsService.queryAllStock(comboid);
            origprice = list.get(i).getOrigprice();
            sales = list.get(i).getSales();
            price = list.get(i).getPrice();
            sumorigprice += origprice;
            sumprice += price;
            sumsales += sales;
            sumstock = re;
        }
        GoodsCom goodsCom = new GoodsCom();
        goodsCom.setSumsales(sumsales);
        goodsCom.setSumstock(sumstock);
        goodsCom.setSumorigprice(sumorigprice);
        goodsCom.setSumprice(sumprice);
        map.put("goodsCom", goodsCom);
        map.put("list", list);
        return map;
    }

    /**
     * 修改套餐
     *
     * @param imgurl
     * @param comboname
     * @param combodiscountprice
     * @return
     */
    public Map<String, Integer> updateComDetail(String imgurl, String comboname, String combodiscountprice, String comboid) {
        Combo goodsComboVo = new Combo();
        goodsComboVo.setComboid(Integer.parseInt(comboid));
        goodsComboVo.setCombodiscountprice(Double.parseDouble(combodiscountprice));
        goodsComboVo.setComboname(comboname);
        goodsComboVo.setImgurl(imgurl);
        int res = goodsService.updateComDetail(goodsComboVo);
        Map<String, Integer> map = new HashedMap();
        map.put("res", res);
        return map;

    }

    /**
     * 增加套餐
     *
     * @param imgurl
     * @param comboname
     * @param combodiscountprice
     * @param goodsid
     * @return
     */
    public Map<String, Integer> addCom(String imgurl, String comboid, String comboname, String combodiscountprice, String goodsid) {
        Map<String, Integer> map = new HashedMap();
        Combo combo = new Combo();
        combo.setImgurl(imgurl);
        combo.setComboname(comboname);
        List<Integer> list = goodsService.findAllComboid();
        int comboidsd = 0;
        int com = Integer.parseInt(comboid);
        for (int i = 0; i < list.size(); i++) {
            int a = list.get(i);
            if (com == a) {
                comboidsd = com + 1;
                //combo.setComboid(comboidsd);
                com = comboidsd;
                if (com == a) {
                    break;
                }
            }
        }
        combo.setComboid(com);
        combo.setIntime(new Date());
        combo.setCombodiscountprice(Double.parseDouble(combodiscountprice));
        int res = goodsService.addCom(combo);
        int comboids = combo.getComboid();
        GoodsCombo good = new GoodsCombo();
        good.setComboid(comboids);
        String goods;
        String productids[] = goodsid.split(",");
        int result = 0;
        for (int i = 0; i < productids.length; i++) {
            goods = productids[i];
            good.setGoodsid(Integer.parseInt(goods));
            result = goodsService.addGoods(good);
        }
        map.put("result", result);
        map.put("res", res);
        return map;
    }

    /**
     * 查询所有ids
     *
     * @return
     */
    public List<Integer> findAllComboid() {
        return goodsService.findAllComboid();
    }
    /**
     * 增加参数图
     *
     * @param img_url
     * @param goodsid
     * @return
     */
    public Map<String, Integer> addImgGoods(String img_url, String goodsid) {
        Map<String, Integer> map = new HashedMap();
        GoodsImg goodsImg = new GoodsImg();
        goodsImg.setImgurl(img_url);
        goodsImg.setType(3);
        goodsImg.setGoodsid(Integer.parseInt(goodsid));
        int res = goodsService.addImgGoods(goodsImg);
        map.put("res", res);
        return map;
    }

    /**
     * 增加描述图
     *
     * @param img_url
     * @param goodsid
     * @return
     */
    public Map<String, Integer> addCommodityDescription(String img_url, String goodsid) {
        Map<String, Integer> map = new HashedMap();
        GoodsImg goodsImg = new GoodsImg();
        goodsImg.setImgurl(img_url);
        goodsImg.setType(1);
        goodsImg.setGoodsid(Integer.parseInt(goodsid));
        int res = goodsService.addCommodityDescription(goodsImg);
        map.put("res", res);
        return map;

    }

    /**
     * 根据用户id查询用户收藏的商品列表
     *
     * @param goodsid
     * @param pager
     * @return
     */
    public List<GoodsVo> queryCollectionGoodsListByUserid(String goodsid, Paging<GoodsVo> pager) {
        return goodsService.queryCollectionGoodsListByUserid(goodsid, pager);
    }

    /**
     * 查询banner图
     *
     * @return
     */
    public List<GoodsImg> queryBannerImg(String goodsid) {
        return goodsService.queryBannerImg(goodsid);
    }

}
