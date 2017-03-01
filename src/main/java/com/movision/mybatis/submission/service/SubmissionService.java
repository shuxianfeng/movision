package com.movision.mybatis.submission.service;

import com.movision.mybatis.submission.entity.SubmissionVo;
import com.movision.mybatis.submission.mapper.SubmissionMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/24 9:17
 */
@Service
@Transactional
public class SubmissionService {

    private static Logger log = LoggerFactory.getLogger(SubmissionService.class);

    @Autowired
    private SubmissionMapper submissionMapper;

    public int commitSubmission(Map<String, Object> parammap) {
        try {
            log.info("提交原生视频投稿申请");
            return submissionMapper.commitSubmission(parammap);
        } catch (Exception e) {
            log.error("提交原生视频投稿申请失败");
            throw e;
        }
    }

    /**
     * 查询投稿列表
     *
     * @param pager
     * @return
     */
    public List<SubmissionVo> queryContributeList(Paging<SubmissionVo> pager) {
        try {
            log.info("查询投稿列表");
            return submissionMapper.findAllQueryContributeList(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询投稿列表异常");
            throw e;
        }
    }

    /**
     * 条件查询投稿列表
     *
     * @param map
     * @param pager
     * @return
     */
    public List<SubmissionVo> queryUniteConditionByContribute(Map map, Paging<SubmissionVo> pager) {
        try {
            log.info("条件查询投稿列表");
            return submissionMapper.findAllqueryUniteConditionByContribute(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件查询投稿列表异常");
            throw e;
        }
    }
}
