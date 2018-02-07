package com.movision.mybatis.photo.mapper;

import com.movision.mybatis.photo.entity.Photo;
import com.movision.mybatis.photo.entity.PhotoVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface PhotoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Photo record);

    int insertSelective(Photo record);

    Photo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Photo record);

    int updateByPrimaryKey(Photo record);

    PhotoVo queryPhotoDetails(Integer id);

    List<PhotoVo> findAllPhoto(Map map,RowBounds rowBounds);

    Photo selectCreditScore(int id);

    int updateCreditScore(Map map);

    int updatePhotoMoney(Map map);

    int updatePhotoStatus(Map map);
 }