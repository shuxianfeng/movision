package com.movision.mybatis.imUserAccusation.service;

import com.movision.mybatis.imUserAccusation.entity.ImUserAccPage;
import com.movision.mybatis.imUserAccusation.entity.ImUserAccusation;
import com.movision.mybatis.imUserAccusation.mapper.ImUserAccusationMapper;
import com.movision.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/22 10:05
 */
@Service
public class ImUserAccusationService {

    private static Logger log = LoggerFactory.getLogger(ImUserAccusationService.class);

    @Autowired
    private ImUserAccusationMapper imUserAccusationMapper;

    public int addRecord(ImUserAccusation imUserAccusation) {
        try {
            log.info("增加举报im用户记录");
            return imUserAccusationMapper.insertSelective(imUserAccusation);
        } catch (Exception e) {
            log.error("增加举报im用户记录失败", e);
            throw e;
        }
    }

    public List<ImUserAccusation> queryNotHandleSelectiveRecord(ImUserAccusation imUserAccusation) {
        try {
            log.info("查询未处理的指定举报im记录");
            return imUserAccusationMapper.queryNotHandleSelectiveRecord(imUserAccusation);
        } catch (Exception e) {
            log.error("查询未处理的指定举报im记录失败", e);
            throw e;
        }
    }

    public List<ImUserAccPage> findAllImuserAccusation(Paging<ImUserAccPage> pagePaging, Map map) {
        try {
            log.info("查询IM用户举报记录");
            return imUserAccusationMapper.findAllImuserAccusation(pagePaging.getRowBounds(), map);
        } catch (Exception e) {
            log.error("查询IM用户举报记录失败", e);
            throw e;
        }
    }


}
