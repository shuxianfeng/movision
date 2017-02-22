package com.movision.mybatis.circle.mapper;

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

    int queryCircleOwner(int circleid);

    List<CircleVo> findAllqueryCircleByList(Integer category);

    String queryCircleBycirclemaster(String phone);

    List<Integer> querycirclemanagerlist(Integer circleid);

    Integer queryFollowSum(Integer circleid);

    Integer queryCircleByNum();

    Circle queryCircleByName(Integer circleid);

    List<Integer> queryListByCircleCategory();

    List<Circle> queryListByCircleList(Integer in);

    List<User> queryCircleUserList(Integer categoryid);

    CircleFollowNum queryFollowAndNewNum(Integer categoryid);

    CircleFollowNum queryFollowAndNewNumt(Integer circleid);

    CirclePostNum queryCirclePostNum(Integer categoryid);

    CirclePostNum queryCirclePostNumt(Integer circleid);

    CircleVo queryCircleSupportnum(Integer categoryid);

    List<Circle> queryDiscoverList();

    Integer updateDiscover(Map<String, Integer> map);

    Integer updateCircleIndex(Integer circleid);

    CircleDetails quryCircleDetails(Integer circleid);

    int updateCircle(CircleDetails circleDetails);

    CircleDetails queryCircleByShow(Integer circleid);

    List<Integer> queryCircleByOrderidList();

}