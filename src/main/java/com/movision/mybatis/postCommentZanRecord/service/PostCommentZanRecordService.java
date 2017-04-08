package com.movision.mybatis.postCommentZanRecord.service;

import com.movision.mybatis.comment.entity.Comment;
import com.movision.mybatis.comment.entity.CommentVo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecord;
import com.movision.mybatis.postCommentZanRecord.entity.PostCommentZanRecordVo;
import com.movision.mybatis.postCommentZanRecord.mapper.PostCommentZanRecordMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/4/6 16:28
 */
@Service
@Transactional
public class PostCommentZanRecordService {

    private static Logger log = LoggerFactory.getLogger(PostCommentZanRecordService.class);

    @Autowired
    private PostCommentZanRecordMapper recordMapper;

    /**
     * 根据用户id查询
     *
     * @param userid
     * @return
     */
    public PostCommentZanRecordVo queryByUserid(Integer userid) {
        try {
            log.info("根据用户id查询");
            return recordMapper.queryByUserid(userid);
        } catch (Exception e) {
            log.error("根据用户id查询失败");
            throw e;
        }
    }

    /**
     * 查询全部
     *
     * @param userid
     * @param
     * @return
     */
    public List<PostCommentZanRecordVo> findAllCommentZanList(Integer userid) {
        try {
            log.info("查询全部");
            return recordMapper.findAllCommentZanList(userid);
        } catch (Exception e) {
            log.error("查询全部失败");
            throw e;
        }
    }

    public List<CommentVo> queryComment(Integer commentid) {
        try {
            log.info("查询评论");
            return recordMapper.queryComment(commentid);
        } catch (Exception e) {
            log.error("查询评论失败");
            throw e;
        }
    }

}
