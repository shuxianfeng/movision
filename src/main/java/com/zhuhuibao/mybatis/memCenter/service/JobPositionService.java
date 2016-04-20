package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by cxx on 2016/4/18 0018.
 */
@Service
@Transactional
public class JobPositionService {
    private static final Logger log = LoggerFactory.getLogger(JobPositionService.class);

    @Autowired
    private JobMapper jobMapper;
    /**
     * 发布职位
     */
    public JsonResult publishPosition(Job job){
        JsonResult jsonResult = new JsonResult();
        try{
            int isPublish = jobMapper.publishPosition(job);
            if(isPublish==1){
                jsonResult.setCode(200);
            }else{
                jsonResult.setCode(400);
                jsonResult.setMessage("发布失败");
            }
        }catch (Exception e){
            log.error("publishPosition error",e);
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * 查询公司已发布的职位
     */
    public JsonResult findAllPositionByMemId(Paging<Job> pager, String id){
        JsonResult jsonResult = new JsonResult();
        List<Job> jobList = jobMapper.findAllByPager(pager.getRowBounds(),id);
        List list = new ArrayList();
        for(int i=0;i<jobList.size();i++){
            Job job = jobList.get(i);
            Map map = new HashMap();
            map.put(Constant.position,job.getName());
            map.put(Constant.salary,job.getSalaryName());
            map.put(Constant.area,job.getWorkArea());
            map.put(Constant.id,job.getId());
            map.put(Constant.publishTime,job.getPublishTime());
            list.add(map);
        }
        pager.result(list);
        jsonResult.setData(pager);
        jsonResult.setCode(200);
        return jsonResult;
    }

    /**
     * 查询公司发布的某条职位的信息
     */
    public JsonResult getPositionByPositionId(String id){
        JsonResult result = new JsonResult();
        Job job = jobMapper.getPositionByPositionId(id);
        result.setCode(200);
        result.setData(job);
        return result;
    }

    /**
     * 删除已发布的职位
     */
    public JsonResult deletePosition(String ids[]){
        JsonResult jsonResult = new JsonResult();
        int isDelete = 0;
        for(int i = 0; i < ids.length; i++){
            String id = ids[i];
            isDelete = jobMapper.deletePosition(id);
            try{
                if(isDelete==1){
                    jsonResult.setCode(200);
                }else {
                    jsonResult.setCode(400);
                    jsonResult.setMessage("删除失败");
                }
            }catch (Exception e){
                log.error("deletePosition error",e);
                e.printStackTrace();
            }
        }
        return jsonResult;
    }

    /**
     * 更新编辑已发布的职位
     */
    public JsonResult updatePosition(Job job){
        JsonResult result = new JsonResult();
        int isUpdate = jobMapper.updatePosition(job);
        try{
            if(isUpdate==1){
                result.setCode(200);
            }else {
                result.setCode(400);
                result.setMessage("更新失败");
            }
        }catch (Exception e){
            log.error("deletePosition error",e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询最新招聘职位
     */
    public JsonResult searchNewPosition(){
        JsonResult result = new JsonResult();
        List<Job> jobList = jobMapper.searchNewPosition();
        result.setData(jobList);
        result.setCode(200);
        return result;
    }

    /**
     * 查询推荐职位
     */
    public JsonResult searchRecommendPosition(String id){
        JsonResult result = new JsonResult();
        List<Job> jobList = jobMapper.searchRecommendPosition(id);
        result.setData(jobList);
        result.setCode(200);
        return result;
    }
}
