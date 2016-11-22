package com.external.service;

import com.external.form.WeChatNewsForm;
import com.external.form.WeChatNewsTypeENum;
import com.zhuhuibao.mybatis.weChat.entity.WeChatNews;
import com.zhuhuibao.mybatis.weChat.mapper.WeChatNewsMapper;
import com.zhuhuibao.utils.DateUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 微信h5页面资讯接口实现
 *
 * @author liyang
 * @date 2016年11月22日
 */
@Service
@Transactional
public class WeChatNewsService {

    @Autowired
    private WeChatNewsMapper weChatNewsMapper;

    /**
     * OMS根据条件分页查询系统资讯信息
     *
     * @param title  标题
     * @param type   分类 1:banner,2:魅力时尚,3:健康人生,4留情岁月,5:成人性趣'
     * @param status 状态
     * @param pager  分页信息
     * @return
     */
    public List<WeChatNewsForm> selWeChatNewsList(String title, String type, String status, Paging<WeChatNewsForm> pager) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("title", title);
        queryMap.put("type", type);
        queryMap.put("status", status);
        List<WeChatNewsForm> chatNewsForms = new ArrayList<>();
        List<WeChatNews> list = weChatNewsMapper.findAllWeChatNewsList(queryMap, pager.getRowBounds());
        if (!CollectionUtils.isEmpty(list)) {
            for (WeChatNews chatNews : list) {
                WeChatNewsForm form = new WeChatNewsForm();
                form.setWeChatNews(chatNews);
                if (null != chatNews.getAddTime()) {
                    form.setAddTimeStr(DateUtils.date2Str(chatNews.getAddTime(), DateUtils.DEFAULT_DATE_FORMAT));
                }
                if (null != chatNews.getUpdateTime()) {
                    form.setUpdateTimeStr(DateUtils.date2Str(chatNews.getAddTime(), DateUtils.DEFAULT_DATE_FORMAT));
                }
                if (null != chatNews.getType()) {
                    form.setShowTypeName(WeChatNewsTypeENum.getName(chatNews.getType()));
                }
                chatNewsForms.add(form);
            }
        }
        return chatNewsForms;
    }

    /**
     * 新增微信资讯信息
     *
     * @param weChatNews
     */
    public void addWeChatNews(WeChatNews weChatNews) {
        weChatNews.setUpdateTime(new Date());
        weChatNews.setAddTime(new Date());
        weChatNews.setStatus(0);
        weChatNewsMapper.insertSelective(weChatNews);
    }

    /**
     * 更新微信资讯信息
     *
     * @param weChatNews
     */
    public void updateWeChatNews(WeChatNews weChatNews) {
        weChatNews.setUpdateTime(new Date());
        weChatNewsMapper.updateByPrimaryKeySelective(weChatNews);

    }

    /**
     * 删除微信资讯信息
     *
     * @param id
     */
    public void deleteWeChatNews(String id) {
        weChatNewsMapper.deleteByPrimaryKey(Long.valueOf(id));
    }

    /**
     * 查询微信资讯信息
     *
     * @param id
     */
    public WeChatNews selWeChatNews(String id) {
        return weChatNewsMapper.selectByPrimaryKey(Long.valueOf(id));
    }

    /**
     * 批量删除微信资讯信息
     *
     * @param ids
     */
    public void batchDelWeChatNews(String ids) {
        String[] delIds = ids.split(",");
        if (delIds.length > 0) {
            for (String id : delIds) {
                this.deleteWeChatNews(id);
            }
        }
    }

    /**
     * 批量发布微信资讯信息
     *
     * @param ids
     */
    public void batchPubWeChatNews(String ids) {
        String[] updateIds = ids.split(",");
        if (updateIds.length > 0) {
            for (String id : updateIds) {
                WeChatNews weChatNews = new WeChatNews();
                weChatNews.setId(Long.valueOf(id));
                weChatNews.setStatus(1);
                this.updateWeChatNews(weChatNews);
            }
        }
    }
}
