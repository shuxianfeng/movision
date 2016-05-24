package com.zhuhuibao.mybatis.oms.mapper;

import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.pojo.SysBean;
import com.zhuhuibao.mybatis.oms.entity.Category;

import java.util.List;

public interface CategoryMapper {

    List<ResultBean> findSystemList();

    List<ResultBean> findSubSystemList(String parentId);

    List<ResultBean> findSubSystemListLimit(String parentId);

    int addSystem(Category category);

    int updateSystem(Category category);

    int deleteSystem(Category category);

    Category findSystem(String parentId);

    List<SysBean> searchAll();
    
    List<ResultBean> getFcateByScate(int scateid);

    List<SysBean> findCategoryByBrand(String id);

    List<ResultBean> findSystemByBrand(String id);

    ResultBean querySystem(String id);

    ResultBean querySystemByScateid(String id);
}