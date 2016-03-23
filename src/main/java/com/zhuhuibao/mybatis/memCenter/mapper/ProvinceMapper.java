package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Province;

import java.util.List;

public interface ProvinceMapper {
    List<ResultBean> findProvince();

    List<ResultBean> searchProvinceByPinYin();
}