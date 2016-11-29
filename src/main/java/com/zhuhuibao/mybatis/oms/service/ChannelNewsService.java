package com.zhuhuibao.mybatis.oms.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MessageTextConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.mapper.ChannelNewsMapper;
import com.zhuhuibao.mybatis.sitemail.service.SiteMailService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
@Transactional
public class ChannelNewsService {
    private static final Logger log = LoggerFactory.getLogger(ChannelNewsService.class);

    @Autowired
    ChannelNewsMapper channel;

    @Autowired
    SiteMailService siteMailService;

    /**
     * 增加频道信息
     *
     * @param news
     * @return
     */
    public Response addChannelNews(ChannelNews news) {
        Response response = new Response();
        try {
            int result = channel.insertSelective(news);
            if(result != 1){
                throw new BusinessException(MsgCodeConstant.mcode_common_failure,
                        MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure)));
            }
        } catch (Exception e) {
            log.error("add channel news error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 更新频道信息
     *
     * @param channelNews 频道信息
     * @return
     */
    public Response updateByPrimaryKeySelective(ChannelNews channelNews) {
        Response response = new Response();
        try {
            channel.updateByPrimaryKeySelective(channelNews);
            if ("2".equals(String.valueOf(channelNews.getStatus()))) {
            	
                siteMailService.addRefuseReasonMail(ShiroUtil.getOmsCreateID(), channelNews.getCreateid(), 
                		channelNews.getReason(), MessageTextConstant.CHANNELNEWS, channelNews.getTitle(),
                		String.valueOf(channelNews.getId()));
            }
        } catch (Exception e) {
            log.error("add channel news error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 更新点击率
     *
     * @param id 频道ID
     * @return
     */
    public Response updateViews(Long id) {
        Response response = new Response();
        try {
            channel.updateViews(id);
        } catch (Exception e) {
            log.error("add channel news error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 查询频道信息
     *
     * @param id 频道信息
     * @return
     */
    public Response selectByPrimaryKey(Long id) {
        Response response = new Response();
        try {
            ChannelNews news = channel.selectByPrimaryKey(id);
            response.setData(news);
        } catch (Exception e) {
            log.error("select by primary key error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 查询资讯信息根据频道ID
     *
     * @param channelMap 频道资讯页条件
     * @return Response
     */
    public List<ChannelNews> queryNewsByChannelInfo(Map<String, Object> channelMap) {
        List<ChannelNews> newsList;
        try {
            newsList = channel.queryNewsByChannelInfo(channelMap);
        } catch (Exception e) {
            log.error("select by primary key error!", e);
            throw e;
        }
        return newsList;
    }

    /**
     * 查询主频道某个栏目点击率排行
     *
     * @param channelMap 频道资讯页条件
     * @return Response
     */
    public Response queryViewsByChannel(Map<String, Object> channelMap) {
        Response response = new Response();
        try {
            List<ChannelNews> newsList = channel.queryViewsByChannel(channelMap);
            response.setData(newsList);
        } catch (Exception e) {
            log.error("select by primary key error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 查询资讯信息根据频道ID
     *
     * @param channelMap 频道资讯页条件
     * @return List<ChannelNews>
     */
    public List<ChannelNews> findAllNewsList(Paging<ChannelNews> pager, Map<String, Object> channelMap) {
        List<ChannelNews> newsList;
        try {
            newsList = channel.findAllNewsList(pager.getRowBounds(), channelMap);
        } catch (Exception e) {
            log.error("find all news List pager error!", e);
            throw e;
        }
        return newsList;
    }

    /**
     * 查询技术频道的新技术播报
     *
     * @param channelMap 频道资讯页条件
     * @return List<ChannelNews>
     */
    public List<Map<String, Object>> findAllTechNewsList(Paging<Map<String, Object>> pager, Map<String, Object> channelMap) {
        List<Map<String, Object>> newsList;
        try {
            newsList = channel.findAllTechNewsList(pager.getRowBounds(), channelMap);
        } catch (Exception e) {
            log.error("find all news List pager error!", e);
            throw e;
        }
        return newsList;
    }

    /**
     * 查询手机端技术主页 - 所有的新技术
     * @param channelMap
     * @return
     */
    public List<Map<String, Object>> findAllTechNewsList4Mobile(Map<String, Object> channelMap) {
        List<Map<String, Object>> newsList;
        try {
            newsList = channel.findAllTechNewsList4Mobile(channelMap);
        } catch (Exception e) {
            log.error("find all news List pager error!", e);
            throw e;
        }
        return newsList;
    }

    /**
     * 预览技术频道的新技术播报
     *
     * @param channelMap 频道资讯页条件
     * @return List<ChannelNews>
     */
    public Map<String, Object> previewNewsInfo(Map<String, Object> channelMap) {
        log.info("preview tech news info " + StringUtils.mapToString(channelMap));
        Map<String, Object> newsList;
        try {
            newsList = channel.previewNewsInfo(channelMap);
        } catch (Exception e) {
            log.error("preview tech news info error!", e);
            throw e;
        }
        return newsList;
    }

    /**
     * 查询资讯内容管理
     *
     * @param channelMap 频道资讯页条件
     * @return List<ChannelNews>
     */
    public List<ChannelNews> findAllContentList(Paging<ChannelNews> pager, Map<String, Object> channelMap) {
        List<ChannelNews> newsList = null;
        try {
            newsList = channel.findAllContentList(pager.getRowBounds(), channelMap);
        } catch (Exception e) {
            log.error("find all news List pager error!", e);
            throw e;
        }
        return newsList;
    }

    /**
     * 查询所说项目
     *
     * @return Response
     */
    public Response queryChannelList() {
        Response response = new Response();
        try {
            List<ChannelNews> newsList = channel.queryChannelList();
            response.setData(newsList);
        } catch (Exception e) {
            log.error("select by primary key error!", e);
            throw e;
        }
        return response;
    }

    /**
     * 删除咨询信息
     *
     * @param id 频道资讯页条件
     * @return int
     */
    public int batchDelNews(String id) {
        log.debug("删除咨询信息");
        return channel.batchDelNews(id);
    }

    public List<Map<String, String>> queryHomepageTechnologyList(Map<String, Object> map) {
        try {
            return channel.queryHomepageTechnologyList(map);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public List<Map<String, String>> findAllPassNewsByType(Paging<Map<String, String>> pager, Map<String, Object> params) {
        List<Map<String, String>> list;
        try {
            list = channel.findAllPassNewsByType(pager.getRowBounds(), params);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return list;
    }


    public List<Map<String, String>> findJobNews4Mobile(Paging<Map<String, String>> pager, Map<String, Object> params) {
        List<Map<String, String>> list;
        try {
            list = channel.findJobNews4Mobile(pager.getRowBounds(), params);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return list;
    }

    public List<Map<String, String>> findIndexNews(Map<String, Object> params) {
        List<Map<String, String>> list;
        try {
            list = channel.findIndexNews(params);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return list;
    }


    public List<Map<String, String>> selectHotViews(String type, Integer count) {
        List<Map<String, String>> list;
        try {
            list = channel.selectHotViews(type, count);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return list;
    }

    public List<Map<String, String>> selectNewViews(String type, Integer count) {
        List<Map<String, String>> list;
        try {
            list = channel.selectNewViews(type, count);
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return list;
    }

    public Map<String, Object> selectLastestNews(String count) {
        Map<String, Object> map = new HashMap<>();
        try {
            //资讯类别 14:面试技巧 15:职场动态 16:行业资讯

            List<Map<String, String>> msList = channel.selectNewsByType("13","1", count);
            List<Map<String, String>> zcList = channel.selectNewsByType("13", "2",count);
            List<Map<String, String>> hyList = channel.selectNewsByType("13","3", count);
            map.put("interviewList", msList);
            map.put("careerList", zcList);
            map.put("industryList", hyList);


        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return map;
    }

    public ChannelNews selectByID(Long id) {
        ChannelNews news;
        try{
            news = channel.selectByID(id);
        } catch (Exception e){
            log.error("查询{}失败","t_oms_channel_news>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
        }
        return news;
    }
     /**
      * 查询咨询列表
      * @param pager
      * @param map
      * @return
      */
	public List<Map> findAllChanNewsList(Paging<Map> pager,
			Map<String, Object> map) {
		    List<Map> newsList = null;
	        try {
	            newsList = channel.findAllChanNewsList(pager.getRowBounds(), map);
	        } catch (Exception e) {
	            log.error("查询{}失败","findAllChanNewsList>>>", e);
	            throw e;
	        }
	        return newsList;
	}
	
    /**
     * 查询咨询详情
     * @param id
     * @return
     */
	public List<Map> queryDetailsById(Long id) {
		   List<Map> newsList = null;
	        try{
	        	newsList = channel.queryDetailsById(id);
	        } catch (Exception e){
	            log.error("查询{}失败","t_oms_channel_news>>>",e);
	            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败");
	        }
	       
		return newsList;
	}
    /**
     * 查询附件名称
     * @param id
     * @return
     */
	public String queryattachName(String id) {
	  try {
            return channel.queryattachName(id);
        } catch (Exception e) {
            log.error("ChannelNewsService.queryattachName：执行异常>>>",e);
            throw e;
        }
	}
}
