package com.movision.mybatis.circle.service;

import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.mapper.CircleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author shuxf
 * @Date 2017/1/18 18:22
 */
@Service
public class CircleService {

    @Autowired
    private CircleMapper circleMapper;

    public List<CircleVo> queryHotCircleList() {
        return circleMapper.queryHotCircleList();
    }

}
