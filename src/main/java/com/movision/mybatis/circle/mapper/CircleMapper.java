package com.movision.mybatis.circle.mapper;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.entity.CircleAndCircle;
import com.movision.mybatis.circle.entity.*;
import com.movision.mybatis.user.entity.User;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface CircleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(Integer id);

    List<CircleVo> queryHotCircleList();

    CircleVo queryCircleIndex1(int circleid);

    List<CircleVo> queryCircleByCategory(int categoryid);

    List<CircleVo> queryAuditCircle();

    int queryIsSupport(Map<String, Object> parammap);

    void addSupportSum(Map<String, Object> parammap);

    void addSupportRecored(Map<String, Object> parammap);

    CircleVo queryCircleInfo(int circleid);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);

    String queryCircleByPhone(int circleid);

    List<Circle> queryHotCircle();

    int queryCircleScope(int circleid);

    User queryCircleOwner(int circleid);

    List<CircleVo> queryCircleByLikeList(Map map);

    List<CircleIndexList> queryListByCircleCategory(Map map);

    List<Circle> queryListByCircleList(Map categoryid);

    List<Circle> queryDiscoverList();

    Integer queryCircleDiscover(String circleid);

    Integer updateDiscover(Map<String, Integer> map);

    Integer updateDiscoverDel(String circleid);

    Integer queryCircleRecommendIndex(String circleid);

    Integer updateCircleIndex(Integer circleid);

    Integer updateCircleIndexDel(String circleid);

    CircleDetails quryCircleDetails(Integer circleid);

    int updateCircle(CircleDetails circleDetails);

    CircleDetails queryCircleByShow(Integer circleid);

    List<Integer> queryCircleByOrderidList();

    int insertCircle(CircleDetails circleDetails);

    List<Circle> findAllMyFollowCircleList(RowBounds rowBounds, Map map);

    int updateAuditCircle(Map map);

    int updateCircleCategoryClassify(Map map);

    Category queryCircleCategoryClassify(String categoryid);
}