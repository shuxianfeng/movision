package com.movision.mybatis.shop.mapper;

import com.movision.mybatis.shop.entity.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shop record);

    int insertSelective(Shop record);

    Shop selectByPrimaryKey(Integer id);

    Shop queryShopInfo(int afterserviceid);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);
}