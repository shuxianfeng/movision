package com.movision.mybatis.postSensitiveWords.service;

import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;
import com.movision.mybatis.postSensitiveWords.mapper.PostSensitiveWordsMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<PostSensitiveWords> querySensitiveList() {
        try {
            log.info("不分页查询所有脱敏词列表");
            return postSensitiveWordsMapper.querySensitiveList();
        } catch (Exception e) {
            log.error("不分页查询所有脱敏词列表失败");
            throw e;
        }
    }

    public int insert(PostSensitiveWords postSensitiveWords) {
        try {
            log.info("增加脱敏成功");
            return postSensitiveWordsMapper.insertSelective(postSensitiveWords);
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

    public int deleteByPrimaryKey(Integer id) {
        try {
            log.info("删除脱敏");
            return postSensitiveWordsMapper.deleteByPrimaryKey(id);
        } catch (Exception e) {
            log.error("删除脱敏失败");
            throw e;
        }
    }

    public List<PostSensitiveWords> findAllPostCodition(Map map, Paging<PostSensitiveWords> pager) {
        try {
            log.info("条件搜索");
            return postSensitiveWordsMapper.findAllPostCodition(map, pager.getRowBounds());
        } catch (Exception e) {
            log.error("条件搜索失败");
            throw e;
        }
    }

    public PostSensitiveWords queryPostSensitive(Integer id) {
        try {
            log.info("数据回显");
            return postSensitiveWordsMapper.queryPostSensitive(id);
        } catch (Exception e) {
            log.error("数据回显失败");
            throw e;
        }
    }
}
