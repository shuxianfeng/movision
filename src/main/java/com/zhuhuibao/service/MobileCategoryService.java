package com.zhuhuibao.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.common.pojo.SysBean;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;

/**
 * 系统分类相关接口实现类
 *
 * @author liyang
 * @date 2016年10月17日
 */
@Service
@Transactional
public class MobileCategoryService {

    private static final Logger log = LoggerFactory.getLogger(MobileCategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * 检索系统一级分类及包括下面的二级分类信息
     *
     * @return
     */
    public List selCategory() {
        List<ResultBean> sysList = categoryMapper.findSystemList();
        List<SysBean> allList = categoryMapper.searchAll();
        List list = new ArrayList();
        for (int i = 0; i < sysList.size(); i++) {
            List subList = new ArrayList();
            Map map = new HashMap();
            ResultBean a = sysList.get(i);
            map.put(Constants.code, a.getCode());
            map.put(Constants.name, a.getName());
            for (int y = 0; y < allList.size(); y++) {
                Map subMap = new HashMap();
                SysBean b = allList.get(y);
                if (a.getCode().equals(b.getId())) {
                    subMap.put(Constants.code, b.getCode());
                    subMap.put(Constants.name, b.getSubSystemName());
                    subList.add(subMap);
                }
            }
            map.put("subTypeArray", subList);
            list.add(map);
        }
        return list;
    }
}
