package com.movision.mybatis.homepageManage.mapper;

import com.movision.mybatis.homepageManage.entity.HomepageManage;

public interface HomepageManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomepageManage record);

    int insertSelective(HomepageManage record);

    HomepageManage selectByPrimaryKey(Integer id);

    HomepageManage queryMayLikeBanner();

    int updateByPrimaryKeySelective(HomepageManage record);

    int updateByPrimaryKey(HomepageManage record);
}