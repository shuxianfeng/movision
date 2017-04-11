package com.movision.mybatis.bossUser.mapper;

import com.movision.mybatis.bossUser.entity.BossUser;
import com.movision.mybatis.bossUser.entity.BossUserVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;


public interface BossUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BossUser record);

    int insertSelective(BossUser record);

    BossUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BossUser record);

    int updateByPrimaryKey(BossUser record);

    BossUser selectByPhone(BossUser bossUser);

    BossUser selectByUsername(String username);

    int updateBossUserLoginTime(BossUser bossUser);

    int delBossUser(int[] ids);

    List<Map<String, Object>> findAllBossUser(RowBounds rowBounds, Map<String, Object> map);

    Map<String, Object> selectBossUserDetail(@Param("uid") Integer userid);

    List<BossUser> selectByRoleid(@Param("roleid") Integer roleid);

    int isExistPhone(@Param("phone") String phone);

    int isExistSameName(BossUser bossUser);

    Integer queryUserByIscontribute(Integer userid);

    Integer queryUserByiscircle(Integer userid);

    Integer queryUserBycirclemanagements(Integer userid);

    Integer queryUserById(Integer userid);

    Integer queryUserIdBySpeciallyGuest(Integer userid);

    BossUser queryUserByAdministrator(Integer userid);

    Integer queryPostByUserid(Map map);

    Integer queryCommentByUserid(Map map);

    Integer queryCircleManageCommentByUserid(Map map);

    Integer querySpeciallyCommentByUserid(Integer id);

    Integer queryCircleIdToCircle(Integer circleid);

    Integer queryCircleManageIdToCircle(Map map);

}