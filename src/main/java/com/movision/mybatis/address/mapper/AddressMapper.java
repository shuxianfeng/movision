package com.movision.mybatis.address.mapper;

import com.movision.mybatis.address.entity.Address;

/**
 * 地址dao接口
 *
 * @Author zhuangyuhao
 * @Date 2017/1/16 20:33
 */
public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}