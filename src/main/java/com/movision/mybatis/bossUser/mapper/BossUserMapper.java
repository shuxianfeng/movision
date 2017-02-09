package com.movision.mybatis.bossUser.mapper;

import com.movision.mybatis.bossUser.entity.BossUser;
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

    int updateBossUserLoginInfo(BossUser bossUser);

    int delBossUser(int[] ids);

    List<Map<String, Object>> findAllBossUser(RowBounds rowBounds, Map<String, Object> map);

    Map<String, Object> selectBossUserDetail(@Param("userid") Integer userid);

    List<BossUser> selectByRoleid(@Param("roleid") Integer roleid);

}