package com.movision.mybatis.postProcessRecord.service;

import com.movision.mybatis.postProcessRecord.entity.PostProcessRecord;
import com.movision.mybatis.postProcessRecord.mapper.PostProcessRecordMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/4/22 11:23
 */
@Service
public class PostProcessRecordService {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(PostProcessRecordService.class);

    @Autowired
    private PostProcessRecordMapper postProcessRecordMapper;


    /**
     * 查询帖子是否被设为加精活精选
     *
     * @param postid
     * @return
     */
    public PostProcessRecord queryPostByIsessenceOrIshot(Integer postid) {
        try {
            log.info("查询帖子是否被设为加精活精选");
            return postProcessRecordMapper.queryPostByIsessenceOrIshot(postid);
        } catch (Exception e) {
            log.error("查询帖子是否被设为加精活精选异常", e);
            throw e;
        }
    }

    /**
     * 更新帖子加精和热门记录
     *
     * @param postProcessRecord
     */
    public void updateProcessRecord(PostProcessRecord postProcessRecord) {
        try {
            log.info("更新帖子加精和热门记录");
            postProcessRecordMapper.updateProcessRecord(postProcessRecord);
        } catch (Exception e) {
            log.error("更新帖子加精和热门记录异常", e);
            throw e;
        }
    }

    /**
     * 新增帖子加精和热门记录
     *
     * @param postProcessRecord
     */
    public void insertProcessRecord(PostProcessRecord postProcessRecord) {
        try {
            log.info("新增帖子加精和热门记录");
            postProcessRecordMapper.insertProcessRecord(postProcessRecord);
        } catch (Exception e) {
            log.error("新增帖子加精和热门记录异常", e);
            throw e;
        }
    }
}
