package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.MemShopCheck;
import com.zhuhuibao.mybatis.memCenter.mapper.MemShopCheckMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author jianglz
 * @since 2016/8/3.
 */
@Service
public class MemShopCheckService {
    private static final Logger log = LoggerFactory.getLogger(MemShopCheckService.class);

    @Autowired
    MemShopCheckMapper mapper;

    public MemShopCheck findByCompanyID(Long companyId) {
        return mapper.findByCompanyID(companyId);
    }

    public void update(MemShopCheck record) {
        int num;
        try {
            num = mapper.updateByPrimaryKeySelective(record);
            if (num != 1) {
                log.error("t_m_shop_check:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新数据失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>",e);
            throw e;
        }
    }

    public void insert(MemShopCheck record) {
        int num;
        try {
            num = mapper.insertSelective(record);
            if (num != 1) {
                log.error("t_m_shop_check:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public MemShopCheck findByID(String shopId) {
        try {
            return mapper.selectByPrimaryKey(Integer.valueOf(shopId));
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }

    public List<MemShopCheck> findAllByCondition(Paging<MemShopCheck> pager, Map<String, String> paramMap) {
        try {
            return mapper.findAllByCondition(pager.getRowBounds(), paramMap);
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
    }
}
