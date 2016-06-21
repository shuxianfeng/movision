package com.zhuhuibao.mybatis.project.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.ProjectConstant;
import com.zhuhuibao.mybatis.oms.service.OmsMemService;
import com.zhuhuibao.mybatis.project.entity.ProjectInfo;
import com.zhuhuibao.mybatis.project.entity.ProjectLinkman;
import com.zhuhuibao.mybatis.project.mapper.ProjectMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;

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
	    * OMS查询项目信息
	    * @param projectID 项目信息ID
	    * @return 项目信息
	     * @throws SQLException
	    */
		public ProjectInfo queryOmsProjectInfoByID(Long projectID) {
			log.info("query project info by id "+projectID);
			ProjectInfo projectInfo;
			try {
				projectInfo = projectMapper.queryOmsProjectInfoByID(projectID);

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
				//乙方中的设计师信息
				List<ProjectLinkman> partyBDesignList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				List<ProjectLinkman> partyBFirstList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				List<ProjectLinkman> partyBWorkList = new ArrayList<ProjectLinkman>();
				//乙方中的分包商信息
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
				//乙方信息
				Map<String,Object> partyB = new TreeMap<>();
				partyB.put("design",partyBDesignList);
				partyB.put("first",partyBFirstList);
				partyB.put("engineering",partyBWorkList);
				partyB.put("second",partyBSecondList);

				map.put("partyB",partyB);

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
	 * OMS查询项目信息详情
	 * @param projectID 项目信息ID
	 * @return 项目信息
     */
	public Map<String,Object> queryOmsProjectDetail(Long projectID) throws Exception
	{
		Map<String,Object> map = new HashMap<String,Object>();
		log.info("query project detail info projectId = "+projectID);
		try
		{
			ProjectInfo projectInfo = queryOmsProjectInfoByID(projectID);
			//项目信息
			map.put("project",projectInfo);
			//根据项目ID查询联系人信息
			List<ProjectLinkman> linkmanList = linkmanService.queryProjectLinkmanByProjectID(projectID);
			if(!linkmanList.isEmpty())
			{
				//甲方信息
				Map<String,List<ProjectLinkman>> partyAMap = new HashMap<String,List<ProjectLinkman>>();
				List<ProjectLinkman> partyAList = new ArrayList<ProjectLinkman>();
				//乙方中的设计师信息
				List<ProjectLinkman> partyBDesignList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				List<ProjectLinkman> partyBFirstList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				List<ProjectLinkman> partyBWorkList = new ArrayList<ProjectLinkman>();
				//乙方中的分包商信息
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
				//乙方信息
				Map<String,Object> partyB = new TreeMap<>();
				partyB.put("design",partyBDesignList);
				partyB.put("first",partyBFirstList);
				partyB.put("engineering",partyBWorkList);
				partyB.put("second",partyBSecondList);

				map.put("partyB",partyB);

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
	 * 查询未登录的项目信息详情
	 * @param projectID 项目信息ID
	 * @return 项目信息
	 */
	public Map<String,Object> previewUnLoginProject(Long projectID) throws Exception
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
				//乙方中的设计师信息
				List<ProjectLinkman> partyBDesignList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				List<ProjectLinkman> partyBFirstList = new ArrayList<ProjectLinkman>();
				//乙方中的工程商信息
				List<ProjectLinkman> partyBWorkList = new ArrayList<ProjectLinkman>();
				//乙方中的分包商信息
				List<ProjectLinkman> partyBSecondList = new ArrayList<ProjectLinkman>();
				int size = linkmanList.size();
				for(int i = 0;i < size;i++)
				{
					StringBuilder sb = null;
					ProjectLinkman linkman= linkmanList.get(i);
					//甲方信息
					if(linkman.getPartyType() == 1)
					{
						hideLinkman(linkman);
						partyAList.add(linkman);
					}
					else//乙方信息
					{
						//1:设计师，2：总包商，3：工程商，4:分包商
						if(linkman.getTypePartyB() == 1) {
							hideLinkman(linkman);
							partyBDesignList.add(linkman);
						} else if(linkman.getTypePartyB() == 2) {
							hideLinkman(linkman);
							partyBFirstList.add(linkman);
						}
						else if(linkman.getTypePartyB() == 3) {
							hideLinkman(linkman);
							partyBWorkList.add(linkman);
						}else if(linkman.getTypePartyB() == 4){
							hideLinkman(linkman);
							partyBSecondList.add(linkman);
						}

					}
				}
				//乙方信息
				Map<String,Object> partyB = new TreeMap<>();
				partyB.put("design",partyBDesignList);
				partyB.put("first",partyBFirstList);
				partyB.put("engineering",partyBWorkList);
				partyB.put("second",partyBSecondList);

				map.put("partyB",partyB);

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
	 * 隐藏联系人部分信息
	 * @param linkman 联系人信息
     */
	private void hideLinkman(ProjectLinkman linkman) {
		StringBuilder sb;
		linkman.setName(ProjectConstant.HiddenStar.TEN.toString());
		linkman.setNote(ProjectConstant.HiddenStar.TEN.toString());
		linkman.setAddress(ProjectConstant.HiddenStar.TEN.toString());
		//联系人
		String lman = linkman.getLinkman();
		if(!StringUtils.isEmpty(lman)) {
            linkman.setLinkman(linkman.getLinkman().substring(0,1) + ProjectConstant.HiddenStar.THREE.toString());
        }
		//手机
		String mobile = linkman.getMobile();
		if(!StringUtils.isEmpty(mobile))
        {
            sb = new StringBuilder(mobile);
            sb.replace(sb.length()-4,sb.length(), ProjectConstant.HiddenStar.FOUR.toString());
            linkman.setMobile(sb.toString());
        }
		//座机
		String tel = linkman.getTelephone();
		if(!StringUtils.isEmpty(tel) && tel.length() > 5)
        {
            sb = new StringBuilder(tel);
            sb.replace(sb.length()-4,sb.length(), ProjectConstant.HiddenStar.FOUR.toString());
            linkman.setTelephone(sb.toString());
        }
		//传真
		String fax = linkman.getFax();
		if(!StringUtils.isEmpty(fax))
        {
            sb = new StringBuilder(fax);
            sb.replace(sb.length()-4,sb.length(), ProjectConstant.HiddenStar.FOUR.toString());
            linkman.setFax(sb.toString());
        }
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
		log.info("update project info ="+StringUtils.beanToString(projectInfo));
		int result = 0;
		try {
			result = projectMapper.updateProjectInfo(projectInfo);
			List<ProjectLinkman> partyAList = projectInfo.getPartyAList();
			List<ProjectLinkman> partyBList = projectInfo.getPartyBList();
			if(partyAList!=null&&!partyAList.isEmpty())
			{
				for(int i=0;i<partyAList.size();i++ )
				{
					
				  linkmanService.updateByPrimaryKeySelective(partyAList.get(i));
				}
				 
			}
			if(partyBList!=null&&!partyBList.isEmpty()){
				for(int i=0;i<partyBList.size();i++ )
				{
				  linkmanService.updateByPrimaryKeySelective(partyBList.get(i));
				}
			}
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
	public List<Map<String,String>> findAllPrjectPager(Map<String,Object> map, Paging<Map<String,String>> page) throws Exception
	{
		log.info("search project info for pager condition = "+ StringUtils.mapToString(map));
		List<Map<String,String>> projectList = null;
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
	public List<Map<String,String>> queryLatestProject(Map<String,Object> map)
	{
		log.info("query latest project info condition = "+ StringUtils.mapToString(map));
		List<Map<String,String>> projectList = null;
		try {
			projectList = projectMapper.queryLatestProject(map);
		}catch(Exception e)
		{
			log.error("query latest project info error!");
			throw e;
		}
		return projectList;
	}

	/**
	 * 首页查询最新项目信息
	 * @param map 项目信息搜素条件 count：指定项目信息条数
	 * @return
	 */
	public List<Map<String,String>> queryHomepageLatestProject(Map<String,Object> map)
	{
		log.info("query homepage latest project info condition = "+ StringUtils.mapToString(map));
		List<Map<String,String>> projectList = null;
		try {
			projectList = projectMapper.queryHomepageLatestProject(map);
		}catch(Exception e)
		{
			log.error("query homepage latest project info error!");
			throw e;
		}
		return projectList;
	}

	/**
	 * 根据条件查询项目分页信息
	 * @param map 查询条件
	 * @return
	 */
	public List<Map<String,String>> queryOmsViewProject(Map<String,Object> map, Paging<Map<String,String>> page) throws Exception
	{
		log.info("search my viewer project info viewerId = "+ StringUtils.mapToString(map));
		List<Map<String,String>> projectList;
		try {
			projectList = projectMapper.findAllOmsViewProject(map,page.getRowBounds());
		}catch(Exception e)
		{
			log.error("search my viewer project info viewerId error!");
			throw e;
		}
		return projectList;
	}

    /**
     * 获取地区
     * @param areaOrCity
     * @return
     */
	public Map<String,Object> getAreaOrCity(Map areaOrCityMap) {
		Map<String,Object> codeMap;
		try {
			  codeMap = projectMapper.getAreaOrCity(areaOrCityMap);
			 
		}catch(Exception e)
		{
			log.error("check isview project error!");
			throw e;
		}
		return codeMap;
	}
}
