package com.zhuhuibao.mybatis.product.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.product.entity.ProductParam;
import com.zhuhuibao.mybatis.product.entity.ProductWithBLOBs;
import com.zhuhuibao.mybatis.product.mapper.ProductParamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductParamService {

    private static final Logger log = LoggerFactory.getLogger(ProductParamService.class);

    @Autowired
    ProductParamMapper paramMapper;

    /**
     * 插入参数
     *
     * @param product 产品表
     * @return
     */
    public Map<String, Long> insertParam(ProductWithBLOBs product) {
        Map<String, Long> paramMap = new HashMap<>();
        log.info("insert param ");
        try {
            List<ProductParam> params = product.getParams();
            if (params != null && !params.isEmpty()) {
                for (ProductParam param : params) {
                    param.setCreateId(product.getCreateid());
                    paramMapper.insert(param);
                    log.info("paramId == " + param.getId());
                    paramMap.put(param.getPname(), param.getId());
                }
            }
        } catch (Exception ex) {
            log.error("insert params error! {}", ex);
            ex.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"操作失败");
        }
        return paramMap;
    }

    /**
     * 查询参数信息根据ID
     *
     * @param paramId
     * @return
     */
    public ProductParam queryParamById(Long paramId) {
        ProductParam param;
        try {
            param = paramMapper.selectByPrimaryKey(paramId);
        } catch (Exception e) {
            log.error("qurey param by id error! {} ", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败") ;
        }
        return param;
    }

    /**
     * 查询参数查询所有产品
     *
     * @param list
     * @return
     */
    public List<ProductParam> selectParamByIds(List<Integer> list) {
        List<ProductParam> params;
        try {
            params = paramMapper.selectParamByIds(list);
        } catch (Exception e) {
            log.error("qurey param by id error! {}", e);
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL,"查询失败") ;
        }
        return params;
    }
}
