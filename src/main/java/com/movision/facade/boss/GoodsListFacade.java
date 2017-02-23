package com.movision.facade.boss;

import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/2/23 9:45
 */
@Service
public class GoodsListFacade {
    @Autowired
    GoodsService goodsService = new GoodsService();


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
     * @param minprice
     * @param maxprice
     * @param minstock
     * @param maxstock
     * @param minsales
     * @param maxsales
     * @param mincollect
     * @param maxcollect
     * @return
     */
    public List<GoodsVo> queryGoodsCondition(String name, String producttags, String brand, String protype, String isdel, String allstatue, String minorigprice, String maxorigprice,
                                             String minprice, String maxprice, String minstock, String maxstock, String minsales, String maxsales, String mincollect, String maxcollect, Paging<GoodsVo> pager) {

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
        if (minprice != null) {
            map.put("minprice", minprice);
        }
        if (maxprice != null) {
            map.put("maxprice", maxprice);
        }
        if (minstock != null) {
            map.put("minstock", minstock);
        }
        if (maxstock != null) {
            map.put("maxstock", maxstock);
        }
        if (minsales != null) {
            map.put("minsales", minsales);
        }
        if (maxsales != null) {
            map.put("maxsales", maxsales);
        }
        if (mincollect != null) {
            map.put("mincollect", mincollect);
        }
        if (maxcollect != null) {
            map.put("maxcollect", maxcollect);
        }
        return goodsService.queryGoodsCondition(map, pager);
    }
}
