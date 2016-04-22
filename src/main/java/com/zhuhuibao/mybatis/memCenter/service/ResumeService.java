package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.mapper.ResumeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 发布简历
     */
    public JsonResult setUpResume(Resume resume){
        JsonResult jsonResult = new JsonResult();
        int isSetUp = resumeMapper.setUpResume(resume);
        try{
            if(isSetUp==1){
                jsonResult.setCode(200);
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
}
