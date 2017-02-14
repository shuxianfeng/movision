package com.movision.facade.mall;

import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.post.entity.PostVo;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/2/13 15:05
 */
@Service
public class MallIndexFacade {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private HomepageManageService homepageManageService;

    /**
     * 查询月度销量前十的商品列表
     *
     * @return
     */
    public Map<String, Object> queryMonthHot() {

        Map<String, Object> map = new HashMap<>();
        List<GoodsVo> monthHotList;

        //先查询商城首页月度热销banner
        HomepageManage monthHomepage = homepageManageService.queryBanner(3);//商城--月度热销banner类型topictype为3
        map.put("monthHomepage", monthHomepage);

        //查询热销榜前十商品
        monthHotList = goodsService.queryMonthHot();

        //如果热销榜商品不足10件（用其他商品随机填充）
        if (monthHotList.size() < 10) {
            List<GoodsVo> defaultList;//热销缺省商品列表

            int[] ids = new int[10];//已经在热销榜的商品id的数组
            for (int i = 0; i < monthHotList.size(); i++) {
                ids[i] = monthHotList.get(i).getId();
            }

            int defaultcount = 10 - monthHotList.size();//前十热销榜缺省数

            Map<String, Object> parammap = new HashMap<>();
            parammap.put("ids", ids);
            parammap.put("defaultcount", defaultcount);
            defaultList = goodsService.queryDefaultGoods(parammap);
            for (int i = 0; i < defaultList.size(); i++) {
                monthHotList.add(defaultList.get(i));
            }
            map.put("monthHotList", monthHotList);
        }

        return map;
    }

    public List<GoodsVo> queryAllMonthHot(String pageNo, String pageSize) {

        if (StringUtils.isEmpty(pageNo)) {
            pageNo = "1";
        }
        if (StringUtils.isEmpty(pageSize)) {
            pageSize = "10";
        }
        Paging<GoodsVo> pager = new Paging<GoodsVo>(Integer.parseInt(pageNo), Integer.parseInt(pageSize));

        List<GoodsVo> allMonthHotList = goodsService.queryAllMonthHot(pager);

        return allMonthHotList;
    }
}