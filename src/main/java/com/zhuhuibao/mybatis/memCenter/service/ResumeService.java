package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.*;
import com.zhuhuibao.mybatis.memCenter.entity.Job;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.mapper.ResumeMapper;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhuhuibao.common.ApiConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cxx on 2016/4/19 0019.
 */
@Service
@Transactional
public class ResumeService {
    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    @Autowired
    private ResumeMapper resumeMapper;

    @Autowired
    ApiConstants apiConstants;

    /**
     * 发布简历
     */
    public JsonResult setUpResume(Resume resume,String id){
        JsonResult jsonResult = new JsonResult();
        int isSetUp = resumeMapper.setUpResume(resume);
        try{
            if(isSetUp==1){
                Resume resume1 = resumeMapper.searchMyResume(id);
                jsonResult.setCode(200);
                jsonResult.setData(resume1.getId());
            }else {
                jsonResult.setMessage("发布失败");
            }
        }catch (Exception e){
            log.error("setUpResume error",e);
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * 查询我创建的简历
     */
    public JsonResult searchMyResume(String id){
        JsonResult jsonResult = new JsonResult();
        Resume resume = resumeMapper.searchMyResume(id);
        Resume resume1 = resumeMapper.searchMyResumeAllInfo(id);
        if(resume1!=null){
            Integer i = 0;
            if(resume1.getJobExperience()!=null){
                i = i+1;
            }
            if(resume1.getEduExperience()!=null){
                i=i+1;
            }
            if(resume1.getProjectExperience()!=null){
                i=i+1;
            }
            if(resume1.getAttach()!=null){
                i=i+1;
            }
            if(resume1.getPhoto()!=null){
                i=i+1;
            }
            if(resume1.getPhoto()!=null){
                i=i+1;
            }
            if(resume1.getExperienceYear()!=null){
                i=i+1;
            }
            if(resume1.getCompany()!=null){
                i=i+1;
            }
            if(resume1.getPositionName()!=null){
                i=i+1;
            }
            Integer a = (i+16)/24;
            String b = Integer.toString(a*100)+"%";
            Map map = new HashMap();
            map.put("info",resume);
            map.put("percent",b);
            jsonResult.setCode(200);
            jsonResult.setData(map);
        }else{
            jsonResult.setCode(400);
            jsonResult.setMessage("暂无简历");
        }
        return jsonResult;
    }

    /**
     * 查询我创建的简历的全部信息
     */
    public JsonResult searchMyResumeAllInfo(String id){
        JsonResult jsonResult = new JsonResult();
        Resume resume = resumeMapper.searchMyResumeAllInfo(id);
        jsonResult.setCode(200);
        jsonResult.setData(resume);
        return jsonResult;
    }

    /**
     * 更新简历,刷新简历
     */
    public JsonResult updateResume(Resume resume){
        JsonResult jsonResult = new JsonResult();
        int isUpdate = resumeMapper.updateResume(resume);
        try{
            if(isUpdate==1){
                jsonResult.setCode(200);
                jsonResult.setData(resume.getId());
            }else {
                jsonResult.setCode(400);
                jsonResult.setMessage("更新失败");
            }
        }catch (Exception e){
            log.error("updateResume error",e);
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * 预览简历
     */
    public JsonResult previewResume(String id){
        JsonResult jsonResult = new JsonResult();
        Resume resume = resumeMapper.previewResume(id);
        jsonResult.setCode(200);
        jsonResult.setData(resume);
        return jsonResult;
    }

    public JsonResult findAllResume(Paging<Resume> pager, Map<String,Object> map)
    {
        JsonResult jsonResult = new JsonResult();
        try
        {
            List<Resume> resumeList = resumeMapper.findAllResume(pager.getRowBounds(),map);
            pager.result(resumeList);
            jsonResult.setData(pager);
        }
        catch(Exception e)
        {
            log.error("find all resume error!",e);
            jsonResult.setCode(MsgCodeConstant.response_status_400);
            jsonResult.setMsgCode(MsgCodeConstant.mcode_common_failure);
            jsonResult.setMessage((MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
            return jsonResult;
        }
        return jsonResult;
    }

    /**
     * 我收到的简历
     */
    public JsonResult receiveResume(Paging<Resume> pager,String id){
        JsonResult jsonResult = new JsonResult();
        List<Resume> resumeList = resumeMapper.findAllReceiveResume(pager.getRowBounds(),id);
        List list = new ArrayList();
        for(int i=0;i<resumeList.size();i++){
            Resume resume = resumeList.get(i);
            Map map = new HashMap();
            map.put(Constant.id,resume.getId());
            map.put(Constant.name,resume.getName());
            map.put(Constant.publishTime,resume.getPublishTime());
            map.put(Constant.realName,resume.getRealName());
            map.put(Constant.experienceYear,resume.getExperienceYear());
            list.add(map);
        }
        pager.result(list);
        jsonResult.setCode(200);
        jsonResult.setData(pager);
        return jsonResult;
    }

    /**
     * 导出简历
     */
    public Map<String,String> exportResume(String id){
        Map<String,String> resumeMap = new HashMap<String,String>();
        Resume resume = resumeMapper.previewResume(id);
        if(resume != null && resume.getAttach() != null)
        {
            String url = apiConstants.getUploadDoc()+"/job/"+resume.getAttach();
            resume.setAttach(url);
            resumeMap.put("title",resume.getTitle());
            resumeMap.put("name",resume.getRealName());
            resumeMap.put("sex",resume.getSex());
            resumeMap.put("marriage",resume.getMarriage());
            resumeMap.put("birthYear",resume.getBirthYear());
            resumeMap.put("education",resume.getEducation());
            resumeMap.put("liveArea",resume.getLiveArea());
            resumeMap.put("workYear",resume.getWorkYear());
            resumeMap.put("mobile",resume.getMobile());
            resumeMap.put("email",resume.getEmail());
            resumeMap.put("jobNature",resume.getJobNature());
            resumeMap.put("post",resume.getPost());
            resumeMap.put("jobArea",resume.getJobArea());
            resumeMap.put("hopeSalary",resume.getHopeSalary());
            resumeMap.put("status",resume.getStatus());
            resumeMap.put("eduExperience",resume.getEduExperience());
            resumeMap.put("jobExperience",resume.getJobExperience());
            resumeMap.put("projectExperience",resume.getProjectExperience());
        }
        return resumeMap;
    }

    public String downloadBill(String id){
        Resume resume = resumeMapper.searchResumeById(id);
        String fileUrl = "";
        if(resume!=null){
            fileUrl = resume.getAttach();
        }
        return fileUrl;
    }

    /**
     * 根据会员ID查询出他自己的简历
     * @param createID
     * @return
     */
    public Resume queryResumeByCreateId(Long createID)
    {
        Resume resume = null;
        try
        {
            List<Resume> resumeList = resumeMapper.queryResumeByCreateId(createID);
            if(!resumeList.isEmpty())
            {
                resume = resumeList.get(0);
            }
        }catch(Exception e)
        {
            log.error("query resume by createID error!",e);
        }
        return resume;
    }

    /**
     * 最新求职
     */
    public JsonResult queryLatestResume(int count){
        JsonResult jsonResult = new JsonResult();
        List<Resume> resumeList = resumeMapper.queryLatestResume(count);
        List list = new ArrayList();
        for(int i=0;i<resumeList.size();i++){
            Resume resume = resumeList.get(i);
            Map map = new HashMap();
            map.put(Constant.id,resume.getId());
            map.put(Constant.logo,resume.getPhoto());
            map.put(Constant.status,resume.getStatus());
            map.put(Constant.area,resume.getJobCity());
            map.put(Constant.name,resume.getRealName());
            map.put(Constant.position,resume.getPost());
            map.put(Constant.experienceYear,resume.getWorkYear());
            map.put(Constant.age,resume.getBirthYear());
            list.add(map);
        }
        jsonResult.setCode(200);
        jsonResult.setData(list);
        return jsonResult;
    }
}
