package com.zhuhuibao.service;

import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.oms.entity.User;
import com.zhuhuibao.mybatis.oms.service.UserService;
import com.zhuhuibao.mybatis.project.entity.ProjectClues;
import com.zhuhuibao.mybatis.project.form.ProjectCluesForm;
import com.zhuhuibao.mybatis.project.mapper.ProjectCluesMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目线索相关接口实现类
 *
 * @author liyang
 * @date 2016年10月28日
 */
@Service
@Transactional
public class ProjectCluesService {

    private static final Logger log = LoggerFactory.getLogger(ProjectCluesService.class);

    @Autowired
    private ProjectCluesMapper cluesMapper;

    @Autowired
    private UserService userService;

    /**
     * oms根据条件检索项目线索分页信息
     * 
     * @param addTimeStart
     * @param addTimeEnd
     * @param status
     * @param pager
     * @return
     */
    public List<ProjectCluesForm> selProjectCluesList(String addTimeStart, String addTimeEnd, String status, Paging<ProjectCluesForm> pager) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("addTimeStart", addTimeStart);
        queryMap.put("addTimeEnd", addTimeEnd);
        queryMap.put("status", status);
        List<ProjectCluesForm> forms = cluesMapper.findAllProjectClues(queryMap, pager.getRowBounds());
        for (ProjectCluesForm form : forms) {
            String userName = "";
            if (null != form.getProjectClues()) {
                if (null != form.getProjectClues().getOperateId()) {
                    User user = userService.selectById(form.getProjectClues().getOperateId());
                    userName = user != null ? user.getUsername() : "";
                }
            }
            form.setUsername(userName);
        }
        return forms;
    }

    /**
     * 增加一条项目线索信息
     *
     * @param projectClues
     *            项目线索
     * @return
     */
    public void addProjectClues(ProjectClues projectClues) {
        projectClues.setAddTime(new Date());
        projectClues.setUpdateTime(new Date());
        cluesMapper.insertSelective(projectClues);
    }

    /**
     * 更新项目线索信息
     *
     * @param projectClues
     *            项目线索
     * @return
     */
    public void updateProjectClues(ProjectClues projectClues) {
        projectClues.setUpdateTime(new Date());
        projectClues.setOperateId(ShiroUtil.getOmsCreateID().intValue());
        cluesMapper.updateByPrimaryKeySelective(projectClues);
    }

    /**
     * 删除一条项目线索信息
     *
     * @param id
     *            项目线索id
     * @return
     */
    public void deleteProjectClues(Integer id) {
        cluesMapper.deleteByPrimaryKey(id);
    }

    /**
     * 获取一条项目线索信息
     *
     * @param id
     *            项目线索id
     * @return
     */
    public ProjectCluesForm selectProjectClues(Integer id) {
        ProjectCluesForm form = cluesMapper.selectByPrimaryKey(id);
        User user = userService.selectById(form.getProjectClues().getOperateId());
        if (null != user) {
            form.setUsername(user.getUsername());
        }
        return cluesMapper.selectByPrimaryKey(id);
    }
}
