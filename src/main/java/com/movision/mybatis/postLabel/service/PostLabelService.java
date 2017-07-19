package com.movision.mybatis.postLabel.service;

import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.mapper.PostLabelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/19 15:50
 */
@Service
public class PostLabelService {

    private static Logger log = LoggerFactory.getLogger(PostLabelService.class);
    @Autowired
    private PostLabelMapper postLabelMapper;

    public List<PostLabel> queryLabelName() {
        try {
            log.info("查询所有标签");
            return postLabelMapper.queryLableName();
        } catch (Exception e) {
            log.error("查询所有标签失败", e);
            throw e;
        }
    }

    public Integer updateLabelHeatValue(Map map) {
        try {
            log.info("根据标签修改热度");
            return postLabelMapper.updateLabelHeatValue(map);
        } catch (Exception e) {
            log.error("根据标签修改热度失败", e);
            throw e;
        }
    }

    public List<PostLabel> queryLabelHeatValue() {
        try {
            log.info("根据热度排序");
            return postLabelMapper.queryLabelHeatValue();
        } catch (Exception e) {
            log.error("根据热度排序失败", e);
            throw e;
        }
    }
}
