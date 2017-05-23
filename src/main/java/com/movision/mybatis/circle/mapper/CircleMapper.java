package com.movision.mybatis.circle.mapper;

import com.movision.mybatis.category.entity.Category;
import com.movision.mybatis.category.entity.CircleAndCircle;
import com.movision.mybatis.circle.entity.*;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.entity.UserRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
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

    String queryPhoneInCircleByCircleid(int circleid);

    List<Circle> queryHotCircle();

    int queryCircleScope(int circleid);

    User queryCircleOwner(int circleid);

    String queryCircleName(int circleid);

    List<CircleVo> queryCircleByLikeList(Map map);

    List<CircleVo> queryCircleManagementByLikeList(Map map);

    List<CircleIndexList> queryListByCircleCategory(Map map);

    List<Circle> queryListByCircleList(Map categoryid);

    List<Circle> queryListByCircleListByUserid(Map categoryid);

    List<CircleVo> queryCircleListByUserRole(UserRole ur);

    List<CircleVo> queryCircleList();

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

    List<MyCircle> findAllMyFollowCircleList(RowBounds rowBounds, Map map);

    int updateAuditCircle(Map map);

    Category queryCircleCategoryClassify(String categoryid);

    List<Integer> queryCIrcleIdByUserId(Integer userid);

    List<Circle> queryCircleByPhone(@Param("phone") String phone);

    int batchUpdatePhoneInCircle(Map map);
}