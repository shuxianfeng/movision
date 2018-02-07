package com.movision.mybatis.photoOrder.mapper;

import com.movision.mybatis.photoOrder.entity.PhotoOrder;
import com.movision.mybatis.photoOrder.entity.PhotoOrderVo;

import java.util.List;
import java.util.Map;

public interface PhotoOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PhotoOrder record);

    int insertSelective(PhotoOrder record);

    PhotoOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PhotoOrder record);

    int updateByPrimaryKey(PhotoOrder record);

    List<PhotoOrderVo> queryAllPhotoOrder(int id);

    int selectOrderUserid(int id);

    int updateOrder(Map map);

    PhotoOrder selectOrder(int id);
}