package com.zhuhuibao.mybatis.memCenter.mapper;

import com.zhuhuibao.common.pojo.CommonBean;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.mybatis.memCenter.entity.Province;

import java.util.List;
import java.util.Map;

public interface ProvinceMapper {
    List<ResultBean> findProvince();

    List<CommonBean> searchProvinceByPinYin();

    Province getProInfo(String code);

    Map<String,String> findProInfo(String provinceCode);
}