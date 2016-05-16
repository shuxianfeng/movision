package com.zhuhuibao.mybatis.oms.service;/**
 * @author Administrator
 * @version 2016/5/16 0016
 */

import com.zhuhuibao.mybatis.oms.entity.ProjectLinkman;
import com.zhuhuibao.mybatis.oms.mapper.ProjectLinkmanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *项目联系人业务处理类，甲方乙方信息
 *@author pl
 *@create 2016/5/16 0016
 **/
@Service
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
}
