package com.movision.mybatis.homepageManage.mapper;

import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.entity.HomepageManageVo;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface HomepageManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomepageManage record);

    int insertSelective(HomepageManage record);

    HomepageManage selectByPrimaryKey(Integer id);

    HomepageManage queryBanner(int type);

    List<HomepageManage> queryBannerList(int type);

    int updateByPrimaryKeySelective(HomepageManage record);

    int updateByPrimaryKey(HomepageManage record);

    List<HomepageManageVo> findAllqueryAdvertisementList(RowBounds rowBounds);

    HomepageManageVo queryAvertisementById(String id);

    int addAdvertisement(Map map);

    int updateAdvertisement(Map map);

    List<HomepageManageVo> findAllQueryAdvertisementLike(Map map, RowBounds rowBounds);

    List<Integer> queryAdvertisementLocation(String type);

    int deleteAdvertisementOrderid(Map map);

    int updateAtionAdvertisementOrderid(Map map);

}