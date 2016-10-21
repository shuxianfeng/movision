package com.zhuhuibao.service;

import java.util.List;
import java.util.Map;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.expo.entity.DistributedOrder;
import com.zhuhuibao.mybatis.expo.entity.Exhibition;
import com.zhuhuibao.mybatis.expo.mapper.ExhibitionMapper;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.expo.service.ExpoService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.pagination.model.Paging;

/**
 * 会展service
 * 
 * @author tongxinglong
 * @date 2016/10/19 0019.
 */
@Transactional
@Service
public class MobileExhibitionService {
    private static final Logger log = LoggerFactory.getLogger(MobileExhibitionService.class);

    @Autowired
    private ExpoService expoService;

    @Autowired
    private ExhibitionMapper exhibitionMapper;

    /**
     * 我的会展信息列表
     *
     * @param pager,map
     */
    public List<Map<String, String>> getMyExhibitionList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            String viewsOrder = (String.valueOf(map.get("publishTimeOrder"))).toUpperCase();
            String publishTimeOrder = (String.valueOf(map.get("publishTimeOrder"))).toUpperCase();
            // 此处必须判断viewsOrder和publishTimeOrder是否在标准参数中，以防止SQL注入
            if (!ArrayUtils.contains(Constants.ORDER_TYPE_KEYWORD, viewsOrder)) {
                map.put("viewsOrder", "");
            }
            if (!ArrayUtils.contains(Constants.ORDER_TYPE_KEYWORD, publishTimeOrder)) {
                map.put("publishTimeOrder", "");
            }

            return exhibitionMapper.findAllMyExhibition(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("ExpoService::findAllMyExhibition", e);
            throw e;
        }
    }

    /**
     * 获取会展详情信息
     * 
     * @param exhibitionId
     * @return
     */
    public Exhibition getExhibitionById(String exhibitionId) {
        return expoService.queryExhibitionInfoById(exhibitionId);
    }

    /**
     * 更新会展点击率
     * 
     * @param exhibition
     */
    public void updateExhibitionViews(Exhibition exhibition) {
        try {
            expoService.updateExhibitionViews(exhibition);
        } catch (Exception e) {
            log.error("updateExhibitionViews", e);
            throw e;
        }
    }

    /**
     * 会展信息列表
     *
     * @param pager,map
     */
    public List<Map<String, String>> getExhibitionList(Paging<Map<String, String>> pager, Map<String, Object> map) {
        try {
            return exhibitionMapper.findAllExhibition(pager.getRowBounds(), map);
        } catch (Exception e) {
            log.error("getExhibitionList", e);
            throw e;
        }
    }

    /**
     * 发布分布式会展定制
     *
     * @param distributedOrder
     */
    public int addDistributedOrder(DistributedOrder distributedOrder) {
        try {
            return expoService.publishDistributedOrder(distributedOrder);
        } catch (Exception e) {
            log.error("addDistributedOrder", e);
            // e.printStackTrace();
            throw e;
        }
    }
}
