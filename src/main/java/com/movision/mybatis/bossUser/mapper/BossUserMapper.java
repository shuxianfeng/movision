package com.movision.mybatis.bossUser.mapper;

import com.movision.mybatis.bossUser.entity.BossUser;


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
}