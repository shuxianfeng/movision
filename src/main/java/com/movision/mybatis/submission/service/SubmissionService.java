package com.movision.mybatis.submission.service;

import com.movision.mybatis.submission.mapper.SubmissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/24 9:17
 */
@Service
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
}
