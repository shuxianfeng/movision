package com.movision.facade.boss;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.mybatis.manageType.service.ManageTypeService;
import com.movision.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
