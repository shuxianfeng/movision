package com.zhuhuibao.mybatis.oms.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhuhuibao.mybatis.oms.entity.ProjectLinkman;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.tools.ant.Project;
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

	@Autowired
	private ProjectLinkmanService linkmanService;
   /**
    * 查询项目信息
    * @param projectID 项目信息ID
    * @return 项目信息
     * @throws SQLException 
    */
	public ProjectInfo queryProjectInfoByID(Long projectID) {
		log.info("query project info by id "+projectID);
		ProjectInfo projectInfo;
		try {
			projectInfo = projectMapper.queryProjectInfoByID(projectID);
			 
		} catch (Exception e) {
			log.error("select by primary key error!", e);
			throw e;
			
		}	
		return projectInfo;
	}

	/**
	 * 查询项目信息详情
	 * @param projectID 项目信息ID
	 * @return 项目信息
     */
	public Map<String,Object> queryProjectDetail(Long projectID) throws Exception
	{
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("query project detail info projectId = "+projectID);
		try
		{
			ProjectInfo projectInfo = queryProjectInfoByID(projectID);
			//项目信息
			map.put("project",projectInfo);
			//根据项目ID查询联系人信息
			List<ProjectLinkman> linkmanList = linkmanService.queryProjectLinkmanByProjectID(projectID);
			if(!linkmanList.isEmpty())
			{
				//甲方信息
				Map<String,List<ProjectLinkman>> partyAMap = new HashMap<String,List<ProjectLinkman>>();
				List<ProjectLinkman> partyAList = new ArrayList<ProjectLinkman>();
				//乙方信息
				Map<String,Object> partyBMap = new HashMap<String,Object>();
				List<Map<String,List<ProjectLinkman>>> partyBList = new ArrayList<Map<String,List<ProjectLinkman>>>();
				//乙方中的设计师信息
				Map<String,List<ProjectLinkman>> partyBDesignMap = new HashMap<String,List<ProjectLinkman>>();
				List<ProjectLinkman> partyBDesignList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				Map<String,List<ProjectLinkman>> partyBFirstMap = new HashMap<String,List<ProjectLinkman>>();
				List<ProjectLinkman> partyBFirstList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				Map<String,List<ProjectLinkman>> partyBWorkMap = new HashMap<String,List<ProjectLinkman>>();
				List<ProjectLinkman> partyBWorkList = new ArrayList<ProjectLinkman>();
				//乙方中的分包商信息
				Map<String,List<ProjectLinkman>> partyBSecondMap = new HashMap<String,List<ProjectLinkman>>();
				List<ProjectLinkman> partyBSecondList = new ArrayList<ProjectLinkman>();
				int size = linkmanList.size();
				for(int i = 0;i < size;i++)
				{
					ProjectLinkman linkman= linkmanList.get(i);
					//甲方信息
					if(linkman.getPartyType() == 1)
					{
						partyAList.add(linkman);
					}
					else//乙方信息
					{
						//1:设计师，2：总包商，3：工程商，4:分包商
						if(linkman.getTypePartyB() == 1) {
							partyBDesignList.add(linkman);
						} else if(linkman.getTypePartyB() == 2) {
							partyBFirstList.add(linkman);
						}
						else if(linkman.getTypePartyB() == 3) {
							partyBWorkList.add(linkman);
						}else if(linkman.getTypePartyB() == 4){
							partyBSecondList.add(linkman);
						}

					}
				}
				partyBDesignMap.put("design",partyBDesignList);
				partyBFirstMap.put("first",partyBFirstList);
				partyBWorkMap.put("engineering",partyBWorkList);
				partyBSecondMap.put("second",partyBSecondList);
				partyBList.add(partyBDesignMap);
				partyBList.add(partyBFirstMap);
				partyBList.add(partyBWorkMap);
				partyBList.add(partyBSecondMap);
				map.put("partyB",partyBList);
				map.put("partyA",partyAList);
			}
		}
		catch(Exception e)
		{
			log.error("query project detail info error!");
			throw e;
		}
		return map;
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
			Long projectId = projectInfo.getId();
			//甲方信息
			List<ProjectLinkman> partyAlist = projectInfo.getPartyAList();
			insertProjectLinkman(projectId, partyAlist);
			//乙方信息
			List<ProjectLinkman> partyBlist = projectInfo.getPartyBList();
			insertProjectLinkman(projectId, partyBlist);

		} catch (Exception e) {
			log.error("add project error!", e);
			throw new SQLException();
			
		}	
		return result;
	}

	/**
	 * 插入项目联系人信息 甲方乙方信息
	 * @param projectId  项目ID
	 * @param partylist  联系人信息
	 * @throws Exception
     */
	private void insertProjectLinkman(Long projectId, List<ProjectLinkman> partylist) throws Exception {
		log.info("projectId = "+projectId);
		try {
			if (!partylist.isEmpty()) {
				int partyASize = partylist.size();
				for (int i = 0; i < partyASize; i++) {
					ProjectLinkman partyA = partylist.get(i);
					partyA.setProjectid(projectId);
					linkmanService.addProjectLinkmanInfo(partyA);
				}
			}
		}catch(Exception e)
		{
			log.error("insert project linkman info error!");
			throw e;
		}
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
	public List<ProjectInfo> findAllPrjectPager(Map<String,Object> map, Paging<ProjectInfo> page) throws Exception
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
