package com.movision.mybatis.afterServiceImg.mapper;

import com.movision.mybatis.afterServiceImg.entity.AfterServiceImg;
import org.springframework.stereotype.Repository;

@Repository
public interface AfterServiceImgMapper {
    int insert(AfterServiceImg record);

    int insertSelective(AfterServiceImg record);
}