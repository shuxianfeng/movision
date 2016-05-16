package com.zhuhuibao.mybatis.oms.mapper;
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
import com.zhuhuibao.mybatis.oms.entity.ProjectInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ProjectMapper {
    //项目信息查询
	ProjectInfo queryProjectInfoByID(@Param(value = "id") Long id);
    //新增项目信息
	int addProjectInfo(ProjectInfo projectInfo);
	//修改项目信息
    int updateProjectInfo(ProjectInfo projectInfo);

    List<ProjectInfo> findAllPrjectPager(Map<String,Object> map,RowBounds rowBounds);

    List<ProjectInfo> queryLatestProject(Map<String,Object> map);
}
