package com.movision.mybatis.address.service;

import com.movision.mybatis.address.mapper.AddressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 19:35
 */
@Service
@Transactional
public class AddressService {
    private static Logger log = LoggerFactory.getLogger(AddressService.class);

    @Autowired
    private AddressMapper addressMapper;

    public List<Map<String, Object>> queryMyAddressList(Map<String, Object> map) {
        try {
            log.info("查询我的地址列表");
            return addressMapper.queryMyAddressList(map);
        } catch (Exception e) {
            log.error("查询我的地址列表失败", e);
            throw e;
        }
    }


}
