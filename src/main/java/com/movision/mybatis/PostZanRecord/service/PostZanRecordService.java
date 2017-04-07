package com.movision.mybatis.PostZanRecord.service;

import com.movision.mybatis.PostZanRecord.entity.PostZanRecord;
import com.movision.mybatis.PostZanRecord.entity.PostZanRecordVo;
import com.movision.mybatis.PostZanRecord.mapper.PostZanRecordMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/4/6 16:30
 */
@Service
@Transactional
public class PostZanRecordService {
    private static Logger log = LoggerFactory.getLogger(PostZanRecordService.class);

    @Autowired
    private PostZanRecordMapper recordMapper;

    /**
     * 根据用户id查询
     *
     * @param userid
     * @return
     */
    public PostZanRecordVo queryByUserid(String userid) {
        try {
            log.info("根据用户id查询");
            return recordMapper.queryByUserid(userid);
        } catch (Exception e) {
            log.error("根据用户id查询失败");
            throw e;
        }
    }

    /**
     * 查询全部
     *
     * @param userid
     * @param pager
     * @return
     */
    public List<Map> findAllZanList(String userid, Paging<Map> pager) {
        try {
            log.info("查询全部");
            return recordMapper.findAllZanList(userid, pager.getRowBounds());
        } catch (Exception e) {
            log.error("查询全部失败");
            throw e;
        }
    }
}
