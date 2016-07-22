package com.zhuhuibao.mybatis.project.service;

import com.zhuhuibao.mybatis.project.entity.ProjectTest;
import com.zhuhuibao.mybatis.project.mapper.ProjectMapper;
import com.zhuhuibao.mybatis.project.mapper.ProjectTestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by cxx on 2016/7/22 0022.
 */
@Service
@Transactional
public class ProjectTestService {

    private static final Logger log = LoggerFactory
            .getLogger(ProjectTestService.class);

    @Autowired
    private ProjectTestMapper projectTestMapper;

    public int insert(ProjectTest projectTest) {
        try{
            return projectTestMapper.insertSelective(projectTest);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public ProjectTest query(int id) {
        try{
            return projectTestMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
