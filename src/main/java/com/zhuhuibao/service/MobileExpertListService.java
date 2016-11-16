package com.zhuhuibao.service;

import com.zhuhuibao.mybatis.expert.mapper.ExpertMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class MobileExpertListService {


    @Autowired
    private ExpertMapper expertMapper;

    /**
     *
     * @param  id  :用户的Id
     * @return     查看过的专家信息
     */
    public Map<String, Object> findAllMyLookedExpertListById(String id) {
       return expertMapper.findAllMyLookedExpertListById(id);
    }
}