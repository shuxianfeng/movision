package com.movision.mybatis.circle.service;

import com.movision.mybatis.circle.entity.CircleVo;
import com.movision.mybatis.circle.mapper.CircleMapper;
import com.movision.mybatis.followCircle.mapper.FollowCircleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/1/18 18:22
 */
@Service
public class CircleService {

    private static Logger log = LoggerFactory.getLogger(CircleService.class);

    @Autowired
    private CircleMapper circleMapper;

    @Autowired
    private FollowCircleMapper followCircleMapper;

    public List<CircleVo> queryHotCircleList() {
        try {
            log.info("查询热门圈子列表");
            return circleMapper.queryHotCircleList();
        } catch (Exception e) {
            log.error("查询热门圈子列表失败");
            throw e;
        }
    }

    public CircleVo queryCircleIndex1(int circleid) {
        try {
            log.info("查询圈子详情上半部分数据");
            return circleMapper.queryCircleIndex1(circleid);
        } catch (Exception e) {
            log.error("查询圈子详情上半部分数据失败");
            throw e;
        }
    }

    public List<CircleVo> queryCircleByCategory(int categoryid) {
        try {
            log.info("通过类型查询圈子列表");
            return circleMapper.queryCircleByCategory(categoryid);
        } catch (Exception e) {
            log.error("通过类型查询圈子列表失败");
            throw e;
        }
    }

    public List<CircleVo> queryAuditCircle() {
        try {
            log.info("查询待审核圈子列表");
            return circleMapper.queryAuditCircle();
        } catch (Exception e) {
            log.error("查询待审核圈子列表失败");
            throw e;
        }
    }

    public int queryCountByFollow(Map<String, Object> parammap) {
        try {
            log.info("查询该用户对当前圈子关注的次数");
            return followCircleMapper.queryCountByFollow(parammap);
        } catch (Exception e) {
            log.info("查询该用户对当前圈子关注的次数失败");
            throw e;
        }
    }

    public String queryCircleByPhone(int circleid) {
        try {
            log.info("查询贴主手机号");
            return circleMapper.queryCircleByPhone(circleid);
        } catch (Exception e) {
            log.error("查询手机号失败");
            throw e;
        }
    }

}
