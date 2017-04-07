package com.movision.mybatis.afterservice.service;

import com.movision.mybatis.afterServiceImg.entity.AfterServiceImg;
import com.movision.mybatis.afterServiceImg.mapper.AfterServiceImgMapper;
import com.movision.mybatis.afterservice.entity.AfterServiceVo;
import com.movision.mybatis.afterservice.entity.Afterservice;
import com.movision.mybatis.afterservice.mapper.AfterserviceMapper;
import com.movision.mybatis.logisticsCompany.entity.LogisticsCompany;
import com.movision.mybatis.logisticsCompany.mapper.LogisticsCompanyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    @Autowired
    private LogisticsCompanyMapper logisticsCompanyMapper;

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

    /**
     * 用户在APP端取消售后申请
     */
    public int cancelAfterService(Map<String, Object> parammap) {
        try {
            logger.info("用户取消售后申请");
            return afterserviceMapper.cancelAfterService(parammap);
        } catch (Exception e) {
            logger.error("用户取消售后申请失败");
            throw e;
        }
    }

    /**
     * 查询售后单基本信息
     */
    public AfterServiceVo queryAfterServiceDetail(int afterserviceid) {
        try {
            logger.info("查询售后单基本信息");
            return afterserviceMapper.queryAfterServiceDetail(afterserviceid);
        } catch (Exception e) {
            logger.error("查询售后单基本信息失败");
            throw e;
        }
    }

    /**
     * 根据物流公司id查询物流公司名称
     */
    public String queryLogisticName(int id) {
        try {
            logger.info("根据物流公司id查询物流公司名称");
            return afterserviceMapper.queryLogisticName(id);
        } catch (Exception e) {
            logger.error("根据物流公司id查询物流公司名称失败");
            throw e;
        }
    }

    /**
     * 根据售后id查询售后图片列表
     */
    public List<AfterServiceImg> queryAfterServiceImgList(int afterserviceid) {
        try {
            logger.info("根据售后id查询售后图片列表");
            return afterServiceImgMapper.queryAfterServiceImgList(afterserviceid);
        } catch (Exception e) {
            logger.error("根据售后id查询售后图片列表失败");
            throw e;
        }
    }

    /**
     * 查询售后中APP用户下拉物流类型选择时的所有支持的物流枚举值列表
     */
    public List<LogisticsCompany> queryLogisticType() {
        try {
            logger.info("查询物流下拉选择枚举");
            return logisticsCompanyMapper.queryLogisticType();
        } catch (Exception e) {
            logger.error("查询物流下拉选择枚举失败");
            throw e;
        }
    }

    /**
     * APP用户在售后单中提交商品退回的物流信息
     */
    public void commitReturnLogisticInfo(Map<String, Object> parammap) {
        try {
            logger.info("APP用户在售后单中提交商品退回的物流信息");
            afterserviceMapper.commitReturnLogisticInfo(parammap);
        } catch (Exception e) {
            logger.error("APP用户在售后单中提交商品退回的物流信息失败");
            throw e;
        }
    }
}
