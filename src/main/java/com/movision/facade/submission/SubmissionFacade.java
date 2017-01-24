package com.movision.facade.submission;

import com.movision.mybatis.submission.service.SubmissionService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/23 17:25
 */
@Service
public class SubmissionFacade {

    @Autowired
    private SubmissionService submissionService;

    public int commitSubmission(String title, String videourl, String userid, String email, String comment) {

        Map<String, Object> parammap = new HashedMap();
        parammap.put("title", title);
        parammap.put("videourl", videourl);
        parammap.put("userid", Integer.parseInt(userid));
        parammap.put("email", email);
        parammap.put("comment", comment);
        parammap.put("status", 0);
        parammap.put("intime", new Date());

        return submissionService.commitSubmission(parammap);
    }
}
