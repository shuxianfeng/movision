package com.movision.mybatis.compressImg.service;

import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.mapper.CompressImgMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
            compressImgMapper.insertSelective(compressImg);
        } catch (Exception e) {
            logger.error("新增异常", e);
            throw e;
        }
    }

    /**
     * 查询是否有缩略图
     *
     * @param map
     * @return
     */
    public String queryUrlIsCompress(Map map) {
        try {
            logger.info("查询是否有缩略图");
            return compressImgMapper.queryUrlIsCompress(map);
        } catch (Exception e) {
            logger.error("查询帖子封面是否有缩略图异常", e);
            throw e;
        }
    }

    /**
     * 根据压缩图查询是否存在原图
     *
     * @param compressimgurl
     * @return
     */
    public String queryUrlIsProtoimg(String compressimgurl) {
        try {
            logger.info("根据压缩图查询是否存在原图");
            return compressImgMapper.queryUrlIsProtoimg(compressimgurl);
        } catch (Exception e) {
            logger.error("根据压缩图查询是否存在原图异常", e);
            throw e;
        }
    }

    /**
     * 根据缩略图查询图片信息
     *
     * @param com
     * @return
     */
    public CompressImg queryProtoBycompress(CompressImg com) {
        return compressImgMapper.queryProtoBycompress(com);
    }
}
