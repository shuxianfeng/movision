package com.zhuhuibao.mybatis.oms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    * @param map 查询参数
    * @return 项目信息
     * @throws SQLException 
    */
	public List<ProjectInfo> queryProjectInfoList(Map<String, Object> map) throws SQLException {
		List<ProjectInfo> projectList=null;
		try {
			projectList = projectMapper.queryProjectInfoList(map);
			 
		} catch (Exception e) {
			log.error("select by primary key error!", e);
			throw new SQLException();
			
		}	
		return projectList;
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
}
