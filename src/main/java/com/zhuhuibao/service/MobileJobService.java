package com.zhuhuibao.service;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触屏端招聘求职相关接口实现类
 *
 * @author liyang
 * @date 2016年10月12日
 */
@Service
@Transactional
public class MobileJobService {

    @Autowired
    private JobPositionService jobService;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 查询公司发布的职位信息
     * 
     * @param pager
     *            分页信息
     * @param id
     *            公司id
     * 
     * @return
     */
    public List<Map<String, Object>> findAllPositionByMemId(Paging<Map<String, Object>> pager, String id) {
        return jobService.findAllPositionByMemId(pager, id);
    }

    /**
     * 刷新职位
     * 
     * @param ids
     *            职位id集合
     * @throws Exception
     */
    public void refreshPosition(String ids) throws Exception {
        String[] idList = ids.split(",");
        for (String id : idList) {
            Job job = new Job();
            job.setId(id);
            jobMapper.updatePosition(job);
        }
    }

    /**
     * 获取职位详情信息
     * 
     * @param id
     *            职位id
     * @return
     */
    public Map getPositionByPositionId(String id) {
        Map map = new HashMap();
        Long createId = ShiroUtil.getCreateID();
        map.put("createid", String.valueOf(createId));
        map.put("id", id);
        return jobService.queryPositionInfoByID(map);
    }

    /**
     * 我申请的职位列表
     * 
     * @param pager
     * @param id
     * @return
     */
    public List myApplyPosition(Paging<Job> pager, String id) {
        List<Map<String, Object>> jobList = jobMapper.findAllMyApplyPosition(pager.getRowBounds(), id);
        List list = new ArrayList();
        for (Map<String, Object> result : jobList) {
            Map map = new HashMap();
            map.put(Constants.id, result.get("id"));
            map.put(Constants.name, result.get("name"));
            map.put(Constants.companyName, result.get("enterpriseName"));
            map.put(Constants.salary, result.get("salaryName"));
            map.put(Constants.publishTime, result.get("publishTime"));
            map.put(Constants.area, result.get("workArea"));
            map.put("welfare", result.get("welfare"));
            map.put("is_deleted", result.get("is_deleted"));
            map.put("companyId", result.get("companyId"));
            map.put("positionType", result.get("positionType"));
            list.add(map);
        }
        return list;
    }
}
