package com.movision.mybatis.role.mapper;

import com.movision.mybatis.role.entity.Role;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;


public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    int delRoles(int[] ids);

    List<Role> selectRoleList(RowBounds rowBounds, Map<String, Object> map);

    List<Role> selectRoleComboList();
}