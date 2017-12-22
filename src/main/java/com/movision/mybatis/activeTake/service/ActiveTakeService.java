package com.movision.mybatis.activeTake.service;

import com.movision.mybatis.activeTake.entity.ActiveTake;
import com.movision.mybatis.activeTake.mapper.ActiveTakeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/12/20 10:10
 */
@Service
@Transactional

public class ActiveTakeService {

    private static Logger log = LoggerFactory.getLogger(ActiveTakeService.class);

    @Autowired
    private ActiveTakeMapper activeTakeMapper;

    public int takeActive(ActiveTake  activeTake){
        try {
            log.info("投票");
            return  activeTakeMapper.takeActive(activeTake);
        }catch (Exception e){
            log.error("投票失败");
            throw e;
        }
    }

    public int deviceCount(Map map){
        try {
            log.info("该设备号投票总数");
            return  activeTakeMapper.deviceCount(map);
        }catch (Exception e){
            log.error("该设备号投票总数失败");
            throw e;
        }

    }

    public int postidCount(Map map){
        try {
            log.info("该设备号投票总数");
            return  activeTakeMapper.postidCount(map);
        }catch (Exception e){
            log.error("该设备号投票总数失败");
            throw e;
        }

    }


    public int takeCount(Map map){
        try {
            log.info("该帖子投票总数");
            return  activeTakeMapper.takeCount(map);
        }catch (Exception e){
            log.error("该帖子投票总数失败");
            throw e;
        }

    }


    public int activeid(int postid){
        try {
            log.info("根据帖子id查活动id");
            return  activeTakeMapper.activeid(postid);
        }catch (Exception e){
            log.error("根据帖子id查活动id失败");
            throw e;
        }

    }

}
