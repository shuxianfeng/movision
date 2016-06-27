package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.order.entity.PublishCourse;
import com.zhuhuibao.mybatis.order.mapper.PublishCourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 已发布产品 {技术培训 | 专家培训}
 */
@Service
public class PublishCourseService {

    private static final Logger log = LoggerFactory.getLogger(PublishCourseService.class);

    @Autowired
    PublishCourseMapper mapper;


    public PublishCourse getCourseById(Long courseId) {
        PublishCourse course;
        try {
            course = mapper.selectByPrimaryKey(courseId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
        }
        return course;
    }

    /**
     * 修改库存数量 (减库存)
     *
     * @param courseid  课程ID
     * @param number 购买数量
     */
    public void updateSubStockNum(Long courseid,int number) {
        int count;
        try {
            count = mapper.updateSubStockNum(courseid, number);
            if (count != 1) {
                log.error("t_p_group_publishCourse:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
        }

    }

    /**
     * 修改库存数量 (加库存)
     *
     * @param courseid  课程ID
     * @param number 购买数量
     */
    public void updateAddStockNum(Long courseid,int number) {
        int count;
        try {
            count = mapper.updateAddStockNum(courseid,number);
            if (count != 1) {
                log.error("t_p_group_publishCourse:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
        }

    }

    /**
     * 修改课程状态
     * @param status  状态
     */
    public void updateStatus(Long courseid,String status) {
        int count;
        try {
            count = mapper.updateStatus(courseid,status);
            if (count != 1) {
                log.error("t_p_group_publishCourse:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BusinessException(MsgCodeConstant.DB_UPDATE_FAIL, "更新数据失败");
        }
    }
}
