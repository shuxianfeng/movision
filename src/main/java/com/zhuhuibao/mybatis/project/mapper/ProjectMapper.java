package com.zhuhuibao.mybatis.project.mapper;
/**
 * 项目工程信息维护接口
 * @author 李光明
 * @since 2016.5.11
 */
import java.util.List;
import java.util.Map;


/**
 * 项目工程信息查询接口
 * @author 李光明
 * @since 2019.5.10
 */
import com.zhuhuibao.mybatis.project.entity.ProjectInfo;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ProjectMapper {
    //项目信息查询
    ProjectInfo queryProjectInfoByID(@Param(value = "id") Long id);
    //OMS项目信息查询
    ProjectInfo queryOmsProjectInfoByID(@Param(value = "id") Long id);
    //新增项目信息
    int addProjectInfo(ProjectInfo projectInfo);
    //修改项目信息
    int updateProjectInfo(ProjectInfo projectInfo);

    List<Map<String,String>> findAllPrjectPager(Map<String,Object> map,RowBounds rowBounds);

    List<Map<String,Object>> queryLatestProject(Map<String,Object> map);

    List<Map<String,String>> findAllOmsViewProject(Map<String,Object> map,RowBounds rowBounds);

    List<Map<String,String>> queryHomepageLatestProject(Map<String,Object> map);

    Map<String,Object> getAreaOrCity(Map areaOrCity);

    List<Map<String, String>> findPrjectByName(Map<String, Object> map);

    Map<String, String> getCatagoryByValue(List list);

    Map<String, Object> getCity(Map<String, String> areaOrCityMap);

    List<Map<String,String>> findAllPrject(Map<String, Object> map, RowBounds rowBounds);

    List<Map<String,Object>> queryLatestNProject(@Param("count") Integer count);

    List<Map<String,String>> queryDescription(Map<String,Object> map);
}
