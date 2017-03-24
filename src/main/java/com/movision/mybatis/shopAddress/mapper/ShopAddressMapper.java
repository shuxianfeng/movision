package com.movision.mybatis.shopAddress.mapper;

import com.movision.mybatis.shopAddress.entity.ShopAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopAddressMapper {
    int insert(ShopAddress record);

    int insertSelective(ShopAddress record);

    void delShopAddress(int shopid);

    void saveShopAddress(ShopAddress shopAddress);

    ShopAddress queryShopAddressByShopid(int shopid);
}