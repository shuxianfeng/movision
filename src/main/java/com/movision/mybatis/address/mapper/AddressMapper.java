package com.movision.mybatis.address.mapper;

import com.movision.mybatis.address.entity.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Address record);

    int insertSelective(Address record);

    Address queryDefaultAddress(int userid);

    Address selectByPrimaryKey(Integer id);

    List<Address> queryAddressList(int userid);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);

    List<Map<String, Object>> queryMyAddressList(Map map);

    Map<String, Object> queryAddressDetail(@Param("id") Integer id);

    Address queryNameByCode(Map<String, Object> parammap);

}