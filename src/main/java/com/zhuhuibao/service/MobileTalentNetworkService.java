package com.zhuhuibao.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ResumeMapper;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.JobRelResumeService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.mybatis.sitemail.entity.MessageText;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人才网业务层
 * <p/>
 * Created by Administrator on 2016/11/24 0024.
 */


@Service
@Transactional
public class MobileTalentNetworkService {

    @Autowired
    JobPositionService jobPositionService;


    @Autowired
    JobMapper jobMapper;

    @Autowired
    ChannelNewsService channelNewsService;

    @Autowired
    ResumeService resumeService;

    @Autowired
    JobRelResumeService jrrService;

    @Autowired
    ResumeMapper resumeMapper;

    /**
     * 公司详情
     *
     * @param id
     * @return
     */
    public MemberDetails queryCompanyInfo(long id) {
        return jobMapper.queryCompanyById(id);
    }

    /**
     * 职位详情
     *
     * @param map
     * @return
     */
    public Map<String, Object> getPositionByPositionId(Map<String, Object> map) {
        return jobPositionService.queryPositionInfoByID(map);
    }


    /**
     * 资讯详情
     *
     * @param aLong
     * @return
     */
    public ChannelNews selectByID(Long aLong) {
        return channelNewsService.selectByID(aLong);
    }

    /**
     * 公司招聘的其他职位
     *
     * @param id
     * @return
     */
    public List<Map<String, Object>> queryJobByCompany(String id) {
        List<Map<String, Object>> jobList = jobMapper.findAllOthersPositionByMemId(Long.parseLong(id));
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> map : jobList) {
            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("id", map.get("id"));
            tmpMap.put("enterpriseName", map.get("enterpriseName"));
            tmpMap.put(Constants.position, map.get("name"));
            tmpMap.put(Constants.salary, map.get("salaryName"));
            tmpMap.put(Constants.area, map.get("workArea"));
            tmpMap.put("province", map.get("province"));
            tmpMap.put("city", map.get("city"));
            tmpMap.put("area", map.get("area"));
            tmpMap.put("companyId", map.get("createID"));
            tmpMap.put("positionType", map.get("positionType"));
            tmpMap.put(Constants.publishTime, map.get("publishTime"));
            tmpMap.put(Constants.updateTime, map.get("updateTime"));
            tmpMap.put("educationName", map.get("educationName"));
            list.add(tmpMap);
        }
        return list;
    }


    /**
     * 收藏简历
     *
     * @param id
     * @return
     */
    public Response selCollectionResume(String id) {
        Response response = new Response();
        Long memberId = ShiroUtil.getCreateID();
        if (memberId != null) {
            int collCount = resumeService.getMaxCollCount(memberId);
            if (collCount >= JobConstant.MAX_COLL_COUNT) {
                response.setCode(400);
                response.setMessage("您的简历收藏夹已满" + JobConstant.MAX_COLL_COUNT + "，请先清空收藏夹，然后再进行简历收藏！");
                return response;
            }
            int result = resumeService.insertCollRecord(id);
            if (result > 0) {
                response.setCode(200);
            } else {
                response.setCode(400);
            }

        } else {
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }

        return response;
    }


    /**
     * 判断简历是否被收藏
     *
     * @param map
     * @return
     */
    public boolean collectionResume(Map<String, Object> map) {
        Long createId = ShiroUtil.getCreateID();
        boolean b = false;
        if (null != createId) {
            map.put("createId", Integer.parseInt(String.valueOf(createId)));
            if (jobMapper.findcollectionResumeById(map) > 0) {
                b = true;
            } else {
                b = false;
            }
        } else {
            return b;
        }
        return b;
    }

    /**
     * 简历投递
     *
     * @param jobID
     * @param recID
     * @param messageText
     * @return
     */
    public boolean queryResumeByCreateId(String jobID, Long recID, String messageText) {
        Long createID = ShiroUtil.getCreateID();
        boolean b=false;
        if (createID != null) {
            List<Resume> resumeList = resumeMapper.queryResumeByCreateId(createID);
            if (!resumeList.isEmpty()) {
                if (resumeList.get(0) != null && resumeList.get(0).getId() != null) {
                    Long resumeID = Long.valueOf(resumeList.get(0).getId());
                    Map<String, Object> map = new HashMap<>();
                    map.put("jobID", jobID);
                    map.put("resumeID", resumeID);
                    //职位没有被申请过或者申请10天后可以再次申请
                    if (jrrService.isExistApplyPosition(map) == 0) {
                        MessageText msgText = new MessageText();
                        msgText.setSendID(createID);
                        msgText.setRecID(Long.valueOf(recID));
                        msgText.setMessageText(messageText);
                        msgText.setTypeID(resumeID);
                        msgText.setType(JobConstant.SITEMAIL_TYPE_JOB_ELEVEN);
//                    response = smService.addSiteMail(msgText);
                        //删除有可能存在的简历和职位对应的关系
                        jrrService.deleteJobRelResume(map);
                        jrrService.insert(Long.valueOf(jobID), resumeID, createID);
                    }
                    b=true;
                }else {
                    b=false;
                }
            } else {
                return b;
            }
        } else {
            return b;
        }
        return b;
    }


    /***
     * 查询是否投递过简历
     *
     * @param map
     * @return
     */
    public boolean isExistApplyPosition(Map<String, Object> map) {
        Long createID = ShiroUtil.getCreateID();
        boolean b ;
        List<Resume> resumeList = resumeMapper.queryResumeByCreateId(createID);
        if ((null != createID) && (!resumeList.isEmpty())) {
            String resumeID = Long.toString(resumeMapper.queryResumeIdById(createID));
            map.put("resumeID", resumeID);
            if (jrrService.isExistApplyPosition(map) > 0) {
                b = true;
            } else {
                b = false;
            }
        } else {
            b = false;
        }
        return b;
    }


    /**
     * 查找企业
     *
     * @param jobID
     * @return
     */
    public Long querycompanyByJobId(String jobID) {
        //Long l = jobMapper.querycompanyByJobId(Long.parseLong(jobID));
        return jobMapper.querycompanyByJobId(Long.parseLong(jobID));
    }

    /**
     *查找职位的标题
     *
     * @param jobID
     * @return
     */
    public String queryJobNameByJobId(String jobID) {
        return jobMapper.queryJobNameByJobId(Long.parseLong(jobID));
    }
}
