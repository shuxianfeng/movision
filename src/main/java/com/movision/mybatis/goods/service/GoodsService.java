package com.movision.mybatis.goods.service;

import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.mapper.GoodsMapper;
import com.movision.mybatis.post.mapper.PostMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/3 19:38
 */
@Service
@Transactional
public class GoodsService {

    private static Logger log = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private GoodsMapper goodsMapper;

    public List<GoodsVo> queryActiveGoods(Paging<Goods> pager, String postid) {
        try {
            log.info("查询商城促销类商品列表");
            return goodsMapper.queryActiveGoods(pager.getRowBounds(), Integer.parseInt(postid));
        } catch (Exception e) {
            log.error("查询商城促销类商品列表失败");
            throw e;
        }

    }

    public List<GoodsVo> queryMonthHot() {
        try {
            log.info("查询月度销量前十商品列表");
            return goodsMapper.queryMonthHot();
        } catch (Exception e) {
            log.error("查询月度销量前十商品列表失败");
            throw e;
        }
    }

    public List<GoodsVo> queryDefaultGoods(Map<String, Object> parammap) {
        try {
            log.info("查询月度热销商品的缺省商品列表");
            return goodsMapper.queryDefaultGoods(parammap);
        } catch (Exception e) {
            log.error("查询月度热销商品的缺省商品列表失败");
            throw e;
        }
    }
}
