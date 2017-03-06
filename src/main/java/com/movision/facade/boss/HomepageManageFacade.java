package com.movision.facade.boss;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.mybatis.manageType.service.ManageTypeService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/6 14:28
 */
@Service
public class HomepageManageFacade {
    @Autowired
    HomepageManageService homepageManageService;

    @Autowired
    ManageTypeService manageTypeService;

    /**
     * 查询广告列表
     *
     * @param pager
     * @return
     */
    public List<HomepageManage> queryAdvertisementList(Paging<HomepageManage> pager) {
        return homepageManageService.queryAdvertisementList(pager);
    }

    /**
     * 用于查看广告详情
     *
     * @param id
     * @return
     */
    public HomepageManage queryAvertisementById(String id) {
        return homepageManageService.queryAvertisementById(id);
    }

    /**
     * 查询广告类型列表
     *
     * @return
     */
    public List<ManageType> queryAdvertisementTypeList() {
        return manageTypeService.queryAdvertisementTypeList();
    }

    public int addAdvertisement(String topictype, String orderid, String content, String subcontent, String url, String transurl) {
        Map map = new HashedMap();
        map.put("topictype", topictype);
        map.put("orderid", orderid);
        map.put("content", content);
        map.put("subcontent", subcontent);
        map.put("url", url);
        map.put("transurl", transurl);
        map.put("intime", new Date());
        map.put("clicksum", 0);
        map.put("ordersum", 0);
        return homepageManageService.addAdvertisement(map);
    }

    public int addAdvertisementType(String type, String name, String wide, String high, String quantity) {
        Map map = new HashedMap();
        map.put("type", type);
        map.put("name", name);
        map.put("wide", wide);
        map.put("high", high);
        map.put("quantity", quantity);
        map.put("intime", new Date());
        return manageTypeService.addAdvertisementType(map);
    }
}
