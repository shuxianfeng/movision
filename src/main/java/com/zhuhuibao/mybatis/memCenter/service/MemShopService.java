package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.MemberShop;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberShopMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 商铺
 *
 * @author jianglz
 * @since 16/6/22.
 */
@Service
public class MemShopService {

    private static final Logger log = LoggerFactory.getLogger(MemShopService.class);

    @Autowired
    MemberShopMapper mapper;


    /**
     * 插入
     *
     * @param record
     */
    public void insert(MemberShop record) {
        int num;
        try {
            num = mapper.insertSelective(record);
            if (num != 1) {
                log.error("t_m_shop:插入数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "插入数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }

    }

    /**
     * 根据公司ID查询
     *
     * @param companyId
     * @return
     */
    public MemberShop findByCompanyID(Long companyId) {
        return mapper.findByCompanyID(companyId);
    }


    /**
     * 更新记录
     *
     * @param record
     */
    public void upload(MemberShop record) {
        int num;
        try {
            num = mapper.updateByPrimaryKeySelective(record);
            if (num != 1) {
                log.error("t_m_shop:更新数据失败");
                throw new BusinessException(MsgCodeConstant.DB_INSERT_FAIL, "更新数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * @param pager
     * @param paramMap
     * @return
     */
    public List<MemberShop> findAllByCondition(Paging<MemberShop> pager, Map<String, String> paramMap) {
        try {
            return mapper.findAllByCondition(pager.getRowBounds(), paramMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 根据ID查询
     *
     * @param shopId
     * @return
     */
    public MemberShop findByID(String shopId) {
        try {
            return mapper.selectByPrimaryKey(Integer.valueOf(shopId));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
