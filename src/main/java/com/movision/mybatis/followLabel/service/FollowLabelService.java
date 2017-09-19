package com.movision.mybatis.followLabel.service;

import com.movision.mybatis.followLabel.entity.FollowLabel;
import com.movision.mybatis.followLabel.mapper.FollowLabelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/7/25 9:57
 */
@Service
public class FollowLabelService {
    private static Logger log = LoggerFactory.getLogger(FollowLabelService.class);
    @Autowired
    private FollowLabelMapper followLabelMapper;

    public int insertSelective(FollowLabel record) {
        try {
            log.info("关注标签");
            return followLabelMapper.insertSelective(record);
        } catch (Exception e) {
            log.error("关注标签失败");
            throw e;
        }
    }


    public int yesOrNo(Map record) {
        try {
            log.info("是否关注标签");
            return followLabelMapper.yesOrNo(record);
        } catch (Exception e) {
            log.error("是否关注标签失败");
            throw e;
        }
    }

    public int cancleLabel(Map map) {
        try {
            log.info("取消关注标签");
            return followLabelMapper.cancleLabel(map);
        } catch (Exception e) {
            log.error("取消关注标签失败");
            throw e;
        }
    }


    public int updatePostLabel(int labelid) {
        try {
            log.info("增加标签关注数量");
            return followLabelMapper.updatePostLabel(labelid);
        } catch (Exception e) {
            log.error("增加标签关注数量失败");
            throw e;
        }
    }


    public int updatePostLabelLess(int labelid) {
        try {
            log.info("减少标签关注数量");
            return followLabelMapper.updatePostLabelLess(labelid);
        } catch (Exception e) {
            log.error("减少标签关注数量失败");
            throw e;
        }
    }

    public int updateLabelHeatValue(Map map) {
        try {
            log.info("修改标签热度");
            return followLabelMapper.updateLabelHeatValue(map);
        } catch (Exception e) {
            log.error("修改标签热度失败", e);
            throw e;
        }
    }

    public int updateLabelHeatValueByPost(Map map) {
        try {
            log.info("发帖时修改标签热度");
            return followLabelMapper.updateLabelHeatValueByPost(map);
        } catch (Exception e) {
            log.error("发帖时修改标签热度失败", e);
            throw e;
        }
    }
}
