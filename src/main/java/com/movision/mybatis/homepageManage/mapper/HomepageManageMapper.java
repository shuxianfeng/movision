package com.movision.mybatis.homepageManage.mapper;

import com.movision.mybatis.homepageManage.entity.HomepageManage;

import java.util.List;

public interface HomepageManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomepageManage record);

    int insertSelective(HomepageManage record);

    HomepageManage selectByPrimaryKey(Integer id);

    HomepageManage queryBanner(int type);

    List<HomepageManage> queryBannerList(int type);

    int updateByPrimaryKeySelective(HomepageManage record);

    int updateByPrimaryKey(HomepageManage record);
}