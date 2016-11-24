package com.zhuhuibao.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.JobConstant;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.MemberDetails;
import com.zhuhuibao.mybatis.memCenter.mapper.JobMapper;
import com.zhuhuibao.mybatis.memCenter.service.JobPositionService;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.oms.entity.ChannelNews;
import com.zhuhuibao.mybatis.oms.service.ChannelNewsService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    /**
     * 公司详情
     *
     * @param id
     * @return
     */
    public MemberDetails queryCompanyInfo(long id) {
        return jobMapper.queryCompanyInfo(id);
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
        return jobMapper.findAllOtherPositionById(id);
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
     * @param id
     * @return
     */
    public boolean collectionResume(String id) {
        Long createId = ShiroUtil.getCreateID();
        boolean infor=false;
        if (null != createId) {
            infor=jobMapper.findcollectionResumeById(Integer.parseInt(id),Integer.parseInt(String.valueOf(createId)));
        } else {
            return false;
        }
        return infor;
    }
}
