package com.movision.mybatis.postSensitiveWords.service;

import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;
import com.movision.mybatis.postSensitiveWords.mapper.PostSensitiveWordsMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhanglei
 * @Date 2017/4/26 10:04
 */
@Service
public class PostSensitiveWordsService {

    private static Logger log = org.slf4j.LoggerFactory.getLogger(PostSensitiveWordsService.class);

    @Autowired
    private PostSensitiveWordsMapper postSensitiveWordsMapper;

    public List<PostSensitiveWords> findAllPostSensitiveWords(Paging<PostSensitiveWords> pager) {
        try {
            log.info("查询脱敏列表");
            return postSensitiveWordsMapper.findAllPostSensitiveWords(pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询脱敏列表失败");
            throw e;
        }
    }

    public int insert(PostSensitiveWords postSensitiveWords) {
        try {
            log.info("增加脱敏成功");
            return postSensitiveWordsMapper.insert(postSensitiveWords);
        } catch (Exception e) {
            log.error("增加脱敏失败");
            throw e;
        }
    }

    public int updateByPrimaryKeySelective(PostSensitiveWords postSensitiveWords) {
        try {
            log.info("修改脱敏");
            return postSensitiveWordsMapper.updateByPrimaryKeySelective(postSensitiveWords);
        } catch (Exception e) {
            log.error("修改脱敏");
            throw e;
        }
    }
}
