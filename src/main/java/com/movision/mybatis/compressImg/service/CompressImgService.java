package com.movision.mybatis.compressImg.service;

import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.mapper.CompressImgMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/6/10 19:04
 */
@Service
public class CompressImgService {

    private static Logger logger = LoggerFactory.getLogger(CompressImgService.class);

    @Autowired
    CompressImgMapper compressImgMapper;

    public void insert(CompressImg compressImg) {
        try {
            logger.info("新增成功");
            compressImgMapper.insert(compressImg);
        } catch (Exception e) {
            logger.error("新增异常", e);
            throw e;
        }
    }
}
