package com.movision.mybatis.video.service;

import com.movision.mybatis.video.entity.Video;
import com.movision.mybatis.video.mapper.VideoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/16 19:46
 */
@Service
public class VideoService {

    private static Logger log = LoggerFactory.getLogger(VideoService.class);
    @Autowired
    private VideoMapper videoMapper;

    /**
     * 修改视频管理
     *
     * @param vide
     * @return
     */
    public Integer updateVideoById(Video vide) {
        try {
            log.info("修改视频信息");
            return videoMapper.updateVoid(vide);
        } catch (Exception e) {
            log.error("修改视频信息异常");
            throw e;
        }
    }

    /**
     * 添加视频管理
     *
     * @param vide
     * @return
     */
    public Integer insertVideoById(Video vide) {
        try {
            log.info("添加视频管理信息");
            return videoMapper.insertSelective(vide);
        } catch (Exception e) {
            log.error("添加视频管理信息异常");
            throw e;
        }
    }

    /**
     * 查询帖子是否发视频
     *
     * @param pid
     * @return
     */
    public int queryVideoByID(Integer pid) {
        try {
            log.info("查询帖子是否发视频");
            return videoMapper.queryVideoByID(pid);
        } catch (Exception e) {
            log.error("查询帖子是否发视频异常", e);
            throw e;
        }
    }

    public List<Video> queryByPostid(Integer pid) {
        try {
            log.info("根据帖子id查询对应的视频信息");
            return videoMapper.queryByPostid(pid);
        } catch (Exception e) {
            log.error("根据帖子id查询对应的视频信息,异常", e);
            throw e;
        }
    }
}
