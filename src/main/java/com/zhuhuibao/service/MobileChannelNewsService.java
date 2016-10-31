package com.zhuhuibao.service;

import com.zhuhuibao.mybatis.oms.mapper.ChannelNewsMapper;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 频道新闻相关接口实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileChannelNewsService {

    private static final Logger log = LoggerFactory.getLogger(MobileChannelNewsService.class);

    @Autowired
    private ChannelNewsService channelNewsService;

    @Autowired
    private ChannelNewsMapper newsMapper;

    /**
     * 查询当前频道下面的新闻信息
     *
     * @param pager
     *            分页信息
     * @param isHot
     *            是否热门
     * @param type
     *            新闻类型
     * @return List<Map>
     */
    public List<Map> findAllChanNewsList(Paging<Map> pager, boolean isHot, String type) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelid", "1");
        map.put("sort", type);
        map.put("status", "1");
        map.put("isHot", isHot);
        return channelNewsService.findAllChanNewsList(pager, map);
    }

    /**
     * 查询当前新闻信息具体内容
     *
     * @param id
     *            新闻信息id
     * @return List<Map>
     */
    public List<Map> queryDetailsById(Long id) {
        channelNewsService.updateViews(id);
        return channelNewsService.queryDetailsById(id);
    }

    /**
     * 根据信息标题查询当前频道下面的新闻信息
     *
     * @param pager
     *            分页信息
     * @param isHot
     *            是否热门
     * @param type
     *            新闻类型
     * @param title
     *            新闻标题
     * @return
     */
    public List<Map> findChanNewsListByTitle(Paging<Map> pager, boolean isHot, String type, String title) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelid", "1");
        map.put("sort", type);
        map.put("status", "1");
        map.put("isHot", isHot);
        map.put("title", title);
        return newsMapper.findChanNewsListByTitle(pager.getRowBounds(), map);
    }
}
