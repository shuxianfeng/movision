package com.movision.mybatis.afterservice.service;

import com.movision.mybatis.afterServiceImg.entity.AfterServiceImg;
import com.movision.mybatis.afterServiceImg.mapper.AfterServiceImgMapper;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.afterservice.mapper.AfterserviceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author zhurui
 * @Date 2017/3/21 11:34
 */
@Service
public class AfterServcieServcie {
    static private Logger logger = LoggerFactory.getLogger(AfterServcieServcie.class);

    @Autowired
    private AfterserviceMapper afterserviceMapper;

    @Autowired
    private AfterServiceImgMapper afterServiceImgMapper;

    /**
     * 插入售后信息
     *
     * @param afterservice
     */
    public void insertAfterInformation(Afterservice afterservice) {
        try {
            logger.info("插入售后信息");
            afterserviceMapper.insertSelective(afterservice);
        } catch (Exception e) {
            logger.error("插入售后信息异常");
            throw e;
        }
    }

    /**
     * 插入售后图片
     */
    public void insertAfterServiceImg(AfterServiceImg afterServiceImg) {
        try {
            logger.info("插入售后单中上传的图片");
            afterServiceImgMapper.insertSelective(afterServiceImg);
        } catch (Exception e) {
            logger.error("插入售后单中上传的图片失败");
            throw e;
        }
    }
}
