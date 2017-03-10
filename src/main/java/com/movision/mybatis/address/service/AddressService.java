package com.movision.mybatis.address.service;

import com.movision.mybatis.address.entity.Address;
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

    public int addAddress(Address address) {
        try {
            log.info("添加我的收获地址");
            return addressMapper.insertSelective(address);
        } catch (Exception e) {
            log.error("添加我的收获地址失败", e);
            throw e;
        }
    }

    public Address queryDefaultAddress(int userid) {
        try {
            log.info("根据用户id查询默认地址");
            return addressMapper.queryDefaultAddress(userid);
        } catch (Exception e) {
            log.error("根据用户id查询默认地址失败");
            throw e;
        }
    }

    public int updateAddress(Address address) {
        try {
            log.info("编辑我的收获地址");
            return addressMapper.updateByPrimaryKeySelective(address);
        } catch (Exception e) {
            log.error("编辑我的收获地址失败", e);
            throw e;
        }
    }

    public Map queryAddressDetail(int id) {
        try {
            log.info("查询地址详情");
            return addressMapper.queryAddressDetail(id);
        } catch (Exception e) {
            log.error("查询地址详情失败", e);
            throw e;
        }
    }

    public Address queryAddressById(int addressid) {
        try {
            log.info("根据地址id查询地址详情");
            return addressMapper.queryAddressById(addressid);
        } catch (Exception e) {
            log.error("根据地址id查询地址详情失败");
            throw e;
        }
    }

    public Address queryNameByCode(Map<String, Object> parammap) {
        try {
            log.info("根据省市区县code查询省市区县名称");
            return addressMapper.queryNameByCode(parammap);
        } catch (Exception e) {
            log.error("根据省市区县code查询省市区县名称失败");
            throw e;
        }
    }

    public Address queryMyDefaultAddress(int userid) {
        try {
            log.info("查询我的默认地址");
            return addressMapper.queryDefaultAddress(userid);
        } catch (Exception e) {
            log.error("查询我的默认地址失败", e);
            throw e;
        }
    }

}
