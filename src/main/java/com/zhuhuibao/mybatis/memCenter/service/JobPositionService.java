package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.MsgCodeConstant;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.memCenter.entity.Position;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.PositionMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;
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

    @Autowired
    private PositionMapper positionMapper;
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
            map.put(Constant.publishTime,job.getPublishTime().substring(0,10));
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

    /**
     * 查询最新发布的职位
     */
    public JsonResult searchLatestPublishPosition(){
        JsonResult result = new JsonResult();
        List<Job> jobList = jobMapper.searchLatestPublishPosition();
        result.setData(jobList);
        result.setCode(200);
        return result;
    }

    /**
     * 职位类别
     */
    public JsonResult positionType(){
        JsonResult jsonResult = new JsonResult();
        List<Position> positionList = positionMapper.findPosition();
        List<Position> subPositionList = positionMapper.findSubPosition();
        List list1 = new ArrayList();
        for(int i=0;i<positionList.size();i++){
            Position position = positionList.get(i);
            Map map = new HashMap();
            map.put(Constant.code,position.getId());
            map.put(Constant.name,position.getName());
            List list = new ArrayList();
            for(int y=0;y<subPositionList.size();y++){
                Position subPosition = subPositionList.get(y);
                if(position.getId().equals(subPosition.getParentId())){
                    Map map1 = new HashMap();
                    map1.put(Constant.code,subPosition.getId());
                    map1.put(Constant.name,subPosition.getName());
                    list.add(map1);
                }
            }
            map.put(Constant.subPositionList,list);
            list1.add(map);
        }
        jsonResult.setCode(200);
        jsonResult.setData(list1);
        return jsonResult;
    }

    /**
     * 查询发布职位公司信息
     * @param id
     * @return
     */
    public JsonResult queryCompanyInfo(Long id)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            MemberDetails member = jobMapper.queryCompanyInfo(id);
            jsonResult.setData(member);
        }
        catch(Exception e)
        {
            log.error("add offer price error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 查询招聘栏目的广告位
     * @param map 查询条件
     * @return
     */
    public JsonResult queryAdvertisingPosition(Map<String,Object> map)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            List<MemberDetails> memberList = jobMapper.queryAdvertisingPosition(map);
            jsonResult.setData(memberList);
        }
        catch(Exception e)
        {
            log.error("add offer price error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 查询企业发布的职位详情
     * @param id 职位ID
     * @return
     */
    public JsonResult queryPositionInfoByID(Long id)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            Job job = jobMapper.queryPositionInfoByID(id);
            jsonResult.setData(job);
        }
        catch(Exception e)
        {
            log.error("add offer price error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 查询企业发布的其它求职位
     * @param map 查询条件
     * @return
     */
    public List<Job> findAllOtherPosition(Paging<Job> pager,Map<String,Object> map)
    {
        List<Job> jobList = new ArrayList<Job>();
        try
        {
            jobList = jobMapper.findAllOtherPosition(pager.getRowBounds(),map);
        }
        catch(Exception e)
        {
            log.error("add offer price error!",e);

        }
        return jobList;
    }

    /**
     * 我申请的职位
     */
    public JsonResult myApplyPosition(Paging<Job> pager,String id){
        JsonResult jsonResult = new JsonResult();
        List<Job> jobList = jobMapper.findAllMyApplyPosition(pager.getRowBounds(),id);
        List list = new ArrayList();
        for(int i=0;i<jobList.size();i++){
            Job job = jobList.get(i);
            Map map = new HashMap();
            map.put(Constant.id,job.getId());
            map.put(Constant.name,job.getName());
            map.put(Constant.companyName,job.getEnterpriseName());
            map.put(Constant.salary,job.getSalaryName());
            map.put(Constant.publishTime,job.getPublishTime());
            map.put(Constant.area,job.getWorkArea());
            map.put(Constant.welfare,job.getWelfare());
            list.add(map);
        }
        pager.result(list);
        jsonResult.setCode(200);
        jsonResult.setData(pager);
        return jsonResult;
    }
}
