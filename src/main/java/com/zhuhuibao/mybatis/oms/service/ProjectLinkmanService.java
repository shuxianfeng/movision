package com.zhuhuibao.mybatis.oms.service;
import com.zhuhuibao.mybatis.oms.entity.ProjectLinkman;
import com.zhuhuibao.mybatis.oms.mapper.ProjectLinkmanMapper;
import com.zhuhuibao.utils.pagination.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *项目联系人业务处理类，甲方乙方信息
 *@author pl
 *@create 2016/5/16 0016
 **/
@Service
@Transactional
public class ProjectLinkmanService {

    private static final Logger log = LoggerFactory.getLogger(ProjectLinkmanService.class);

    @Autowired
    private ProjectLinkmanMapper linkmanMapper;

    /**
     * 添加项目联系人信息
     * @param linkmanInfo 项目联系人信息
     * @return
     */
    public int addProjectLinkmanInfo(ProjectLinkman linkmanInfo) throws Exception {
        int result=0;
        try {
            result = linkmanMapper.insertSelective(linkmanInfo);

        } catch (Exception e) {
            log.error("add project error!", e);
            throw e;

        }
        return result;
    }

    public List<ProjectLinkman> queryProjectLinkmanByProjectID(Long projectID) throws Exception
    {
        log.info("query Linkman By ProjectID = "+projectID);
        List<ProjectLinkman> linkmanList = null;
        try
        {
            linkmanList = linkmanMapper.queryProjectLinkmanByProjectID(projectID);
        }catch (Exception e)
        {
            log.error("query Linkman By ProjectID error!");
            throw e;
        }
        return linkmanList;
    }
    
    public int updateByPrimaryKeySelective(ProjectLinkman linkman)
    {
    	log.info("update Linkman info = "+StringUtils.beanToString(linkman));
    	int result = 0;
        try
        {
        	result = linkmanMapper.updateByPrimaryKeySelective(linkman);
        }catch (Exception e)
        {
            log.error("update Linkman info error!");
            throw e;
        }
        return result;
    }
}
