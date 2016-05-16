package com.zhuhuibao.mybatis.oms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.mybatis.oms.entity.ProjectInfo;
import com.zhuhuibao.mybatis.oms.mapper.ProjectMapper;

/**
 * 项目信息业务处理类
 * @author Created by gmli on 2016/5/10
 * @created 2016-05-13
 */
@Service
@Transactional
public class ProjectService {
	private static final Logger log = LoggerFactory
			.getLogger(OmsMemService.class);
	@Autowired
	private ProjectMapper projectMapper;
   /**
    * 查询项目信息
    * @param projectID 项目信息ID
    * @return 项目信息
     * @throws SQLException 
    */
	public int queryProjectInfoByID(Long projectID) {
		log.info("query project info by id "+projectID);
		int result = 0;
		try {
			projectMapper.queryProjectInfoByID(projectID);
			 
		} catch (Exception e) {
			log.error("select by primary key error!", e);
			throw e;
			
		}	
		return result;
	}
	/**
	 * 添加项目工程信息
	 * @param projectInfo 项目工程信息
	 * @return
	 */
	public int addProjectInfo(ProjectInfo projectInfo) throws SQLException {
		int result=0;
		try {
			result = projectMapper.addProjectInfo(projectInfo);
			 
		} catch (Exception e) {
			log.error("add project error!", e);
			throw new SQLException();
			
		}	
		return result;
	}
	/**
	 * 修改项目信息
	 * @param projectInfo
	 * @return
	 * @throws SQLException
	 */
	public int updateProjectInfo(ProjectInfo projectInfo) throws SQLException {
		int result=0;
		try {
			result = projectMapper.updateProjectInfo(projectInfo);
			 
		} catch (Exception e) {
			log.error("upate project error!", e);
			throw new SQLException();
			
		}	
		return result;
	}

	/**
	 * 根据条件查询项目分页信息
	 * @param map 项目信息搜素条件
	 * @return
	 */
	public List<ProjectInfo> findAllPrjectPager(Map<String,Object> map, Paging<ProjectInfo> page)
	{
		log.info("search project info for pager condition = "+ StringUtils.mapToString(map));
		List<ProjectInfo> projectList = null;
		try {
			projectList = projectMapper.findAllPrjectPager(map,page.getRowBounds());
		}catch(Exception e)
		{
			log.error("search project info for pager error!");
			throw e;
		}
		return projectList;
	}

	/**
	 * 根据条件查询最新项目信息
	 * @param map 项目信息搜素条件 count：指定项目信息条数
	 * @return
	 */
	public List<ProjectInfo> queryLatestProject(Map<String,Object> map)
	{
		log.info("query latest project info condition = "+ StringUtils.mapToString(map));
		List<ProjectInfo> projectList = null;
		try {
			projectList = projectMapper.queryLatestProject(map);
		}catch(Exception e)
		{
			log.error("query latest project info error!");
			throw e;
		}
		return projectList;
	}
}
