package com.movision.mybatis.address.mapper;

import com.movision.mybatis.address.entity.Address;

import java.util.List;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address selectByPrimaryKey(Integer id);

    List<Address> queryAddressList(int userid);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}