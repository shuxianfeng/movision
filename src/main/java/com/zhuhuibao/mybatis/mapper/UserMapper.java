package com.zhuhuibao.mybatis.mapper;

import com.zhuhuibao.mybatis.entity.User;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User findByMobile(String mobile);

    List<User> findAll();

    List<User> findAllByPager(RowBounds rowBounds);
}