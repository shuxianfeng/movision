package com.zhuhuibao.service;

import java.util.*;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.news.entity.News;
import com.zhuhuibao.mybatis.news.entity.NewsRecommendPlace;
import com.zhuhuibao.mybatis.news.form.NewsForm;
import com.zhuhuibao.mybatis.news.mapper.NewsMapper;
import com.zhuhuibao.mybatis.news.mapper.NewsRecommendPlaceMapper;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 资讯相关接口实现类
 *
 * @author liyang
 * @date 2016年10月17日
 */
@Service
@Transactional
public class NewsService {

    private static final Logger log = LoggerFactory.getLogger(NewsService.class);

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private NewsRecommendPlaceMapper placeMapper;

    /**
     * OMS根据条件分页查询系统资讯信息
     *
     * @param title
     *            标题
     * @param type
     *            分类
     * @param status
     *            状态
     * @param pager
     *            分页信息
     * @return
     */
    public List<NewsForm> sel_news_list(String title, String type, String status, Paging<NewsForm> pager) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("title", title);
        queryMap.put("type", type);
        // 资讯分类 为 "行业资讯"
        if (null != type && type.indexOf("1") == 0 && type.length() > 1) {
            queryMap.put("subType", type.substring(1));
            queryMap.put("type", null);
        }
        queryMap.put("status", status);
        List<NewsForm> list = newsMapper.findAllNewsPager(queryMap, pager.getRowBounds());
        List<NewsForm> fixList = new ArrayList<>();
        for (NewsForm newsForm : list) {
            List<NewsRecommendPlace> places = placeMapper.getPlaceByNewsId(newsForm.getNews().getId());
            String recPlace = "";
            for (NewsRecommendPlace place : places) {
                recPlace.concat(place.getRecommendPlace());
            }
            newsForm.setRecPlace(recPlace);
            fixList.add(newsForm);
        }
        return fixList;
    }

    /**
     * 增加一条资讯信息
     *
     * @param news
     *            资讯
     * @return
     */
    public void add_news(News news, String recPlace) {
        completeInsertBaseInfo(news);
        int id = newsMapper.insertSelective(news);

        NewsRecommendPlace place = new NewsRecommendPlace();
        place.setUpdateTime(new Date());
        place.setAddTime(new Date());
        if (null != news.getId()) {
            place.setNewsId(news.getId());
        } else {
            place.setNewsId(10L);
        }

        place.setRecommendPlace(recPlace);
        placeMapper.insertSelective(place);
    }

    /**
     * 完善资讯添加前的基础信息
     * 
     * @param news
     */
    private void completeInsertBaseInfo(News news) {
        news.setStatus(0);
        if (null == news.getViews()) {
            news.setViews(0L);
        }
        Date curDate = new Date();
        news.setAddTime(curDate);
        news.setUpdateTime(curDate);
        news.setPublisherId(ShiroUtil.getOmsCreateID());
        if (null == news.getPublishTime()) {
            news.setPublishTime(curDate);
        }
    }

    /**
     * 删除资讯信息
     *
     * @param id
     *            主键id
     * @return
     */
    public boolean del_news(int id) {
        return newsMapper.deleteByPrimaryKey(Long.valueOf(id)) > 0;
    }

    /**
     * oms后台更新资讯信息
     *
     * @param news
     *            资讯
     * @return
     */
    public void update_news(News news, String recPlace) {
        newsMapper.updateByPrimaryKeySelective(news);
        List<NewsRecommendPlace> places = placeMapper.getPlaceByNewsId(news.getId());
        for (NewsRecommendPlace place : places) {
            placeMapper.deleteByPrimaryKey(place.getId());
        }
        String[] split = recPlace.split(",");
        if (split.length > 0) {
            for (String place : split) {
                NewsRecommendPlace recommendPlace = new NewsRecommendPlace();
                recommendPlace.setUpdateTime(new Date());
                recommendPlace.setAddTime(new Date());
                recommendPlace.setNewsId(news.getId());
                recommendPlace.setRecommendPlace(place);
                placeMapper.insertSelective(recommendPlace);
            }
        }
    }

    /**
     * 查询资讯信息
     *
     * @param id
     *            主键id
     * @return
     */
    public NewsForm sel_news(int id) {
        NewsForm form = newsMapper.selectByPrimaryKey(Long.valueOf(id));
        List<NewsRecommendPlace> places = placeMapper.getPlaceByNewsId(form.getNews().getId());
        String recPlace = "";
        for (NewsRecommendPlace place : places) {
            recPlace = recPlace.concat(place.getRecommendPlace());
        }
        form.setRecPlace(recPlace);
        return form;
    }

    /**
     * 批量删除资讯信息
     *
     * @param ids
     *            批量惭怍的资讯id 两个id之间用","隔开
     * @return
     */
    public boolean batch_del_news(String ids) {
        String[] delIds = ids.split(",");
        if (delIds.length > 0) {
            for (String id : delIds) {
                if (this.del_news(Integer.valueOf(id))) {
                    List<NewsRecommendPlace> places = placeMapper.getPlaceByNewsId(Long.valueOf(id));
                    for (NewsRecommendPlace rec : places) {
                        placeMapper.deleteByPrimaryKey(rec.getId());
                    }
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 批量发布资讯信息
     *
     * @param ids
     *            批量惭怍的资讯id 两个id之间用","隔开
     * @return
     */
    public void batch_pub_news(String ids) {
        String[] pubIds = ids.split(",");
        List<Integer> pIds = new ArrayList<>();
        for (String id : pubIds) {
            pIds.add(Integer.valueOf(id));
        }
        if (pIds.size() > 0) {
            newsMapper.batchPubNews(pIds);
        }
    }

    /**
     * 批量修改资讯信息的属性
     *
     * @param ids
     * @param recPlace
     * @param type
     */
    public void batch_modifty_rec_place(String ids, String recPlace, int type) {
        String[] newsIds = ids.split(",");
        String[] recPlaceArr = recPlace.split(",");
        for (String id : newsIds) {
            List<NewsRecommendPlace> places = placeMapper.getPlaceByNewsId(Long.valueOf(id));
            // 删除属性
            if (type == 0) {
                for (NewsRecommendPlace r : places) {
                    for (String rep : recPlaceArr) {
                        if (rep.equals(r.getRecommendPlace())) {
                            placeMapper.deleteByPrimaryKey(r.getId());
                            break;
                        }
                    }
                }
            } else {
                for (String rep : recPlaceArr) {
                    boolean ifContains = false;
                    for (NewsRecommendPlace r : places) {
                        if (rep.equals(r.getRecommendPlace())) {
                            ifContains = true;
                            break;
                        }
                    }
                    // 如果不包含
                    if (!ifContains) {
                        NewsRecommendPlace recommendPlace = new NewsRecommendPlace();
                        recommendPlace.setUpdateTime(new Date());
                        recommendPlace.setAddTime(new Date());
                        recommendPlace.setNewsId(Long.valueOf(id));
                        recommendPlace.setRecommendPlace(rep);
                        placeMapper.insertSelective(recommendPlace);
                    }
                }
            }
        }
    }

    /**
     * 触屏端列表展示页面
     *
     * @param queryType
     *            列表类型:1 全部，2 热点，3 分类
     * @param type
     *            一级分类:1 行业资讯,2 专题,3 筑慧访谈,4 曝光台,5 工程商新闻,6 深度观察,7 活动
     * @param subtype
     *            行业资讯下面的二级分类:1 网络及硬件,2 安全防范,3 楼宇自动化,4 数据中心,5 智能家居,6 影音视频,7
     *            应用系统,8 智能照明,9 行业软件
     * @param pager
     *            分页对象
     * @return
     */
    public List<NewsForm> mobile_sel_news_list(String queryType, String type, String subtype, Paging<NewsForm> pager) {
        // 查询全部类型对应列表页面
        if (queryType.equals("1")) {
            return newsMapper.findAllMobileNews(pager.getRowBounds());
        } else if (queryType.equals("2")) {
            return newsMapper.findAllHotMobileNews(pager.getRowBounds());
        } else {
            Map queryMap = new HashMap();
            queryMap.put("type", type);
            queryMap.put("subtype", subtype);
            return newsMapper.findAllMobileNewsByType(queryMap, pager.getRowBounds());
        }
    }

    /**
     * 更新资讯信息(不包括推荐位置)
     *
     * @param news
     *            资讯
     * @return
     */
    public void upd_news(News news) {
        newsMapper.updateByPrimaryKeySelective(news);
    }

}
