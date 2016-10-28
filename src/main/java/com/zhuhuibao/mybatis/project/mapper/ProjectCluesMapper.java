package com.zhuhuibao.mybatis.project.mapper;

import com.zhuhuibao.mybatis.project.entity.ProjectClues;
import org.apache.ibatis.session.RowBounds;
import org.apache.xpath.operations.String;

import java.util.List;
import java.util.Map;

/**
 * 项目线索业务相关数据层
 *
 * @author changxinwei
 * @date 2016年10月26日
 */
public interface ProjectCluesMapper {

    /**
     * 增加项目线索业务相关数据
     *
     * @param projectClues 项目线索
     * @return
     */
    int insertSelective(ProjectClues projectClues);


    /**
     * 删除项目线索业务相关数据
     *
     * @param id 项目线索Id
     * @return
     */
    int deleteByPrimaryKey(Integer id);


    /**
     * 修改项目线索业务相关数据
     *
     * @param projectClues 项目线索
     * @return
     */
    int updateByPrimaryKeySelective(ProjectClues projectClues);


    /**
     * 查找项目线索业务相关数据
     *
     * @param id 项目线索Id
     * @return
     */
    ProjectClues selectByPrimaryKey(Integer id);


    /**
     * 根据Id查询项目线索所有数据分页显示
     *
     * @param map map
     * @return
     */
    List<Map<String, String>> findAllProjectClues(Map<String, String> map, RowBounds rowBounds);


}
