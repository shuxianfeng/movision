package com.zhuhuibao.mybatis.oms.service;

/**
 * Created by cxx on 2016/3/14 0014.
 */

import com.zhuhuibao.common.Constant;
import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SysBean;
import com.zhuhuibao.mybatis.oms.entity.Category;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类目管理
 * @author cxx
 *
 */
@Service
@Transactional
public class CategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 查询品牌所属大系统
     */
    public List<ResultBean> findSystemByBrand(String id)throws Exception{
        try{
            return categoryMapper.findSystemByBrand(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询品牌所属大系统,子系统
     */
    public List<SysBean> findCategoryByBrand(String id)throws Exception{
        try{
            return categoryMapper.findCategoryByBrand(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询所有大系统
     */
    public List<ResultBean> findSystemList()throws Exception{
        try{
            return categoryMapper.findSystemList();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询所有大系统,子系统
     */
    public List<SysBean> searchAll()throws Exception{
        try{
            return categoryMapper.searchAll();
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据大系统id查询子系统
     */
    public List<ResultBean> findSubSystemList(String id)throws Exception{
        try{
            return categoryMapper.findSubSystemList(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据大系统id查询子系统(limit 8)
     */
    public List<ResultBean> findSubSystemListLimit(String id)throws Exception{
        try{
            return categoryMapper.findSubSystemListLimit(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据大系统id查询大系统信息
     */
    public Category findSystem(String id)throws Exception{
        try{
            return categoryMapper.findSystem(id);
        }catch (Exception e){
            throw e;
        }
    }


    /**
     * 添加类目
     */
    public int addSystem(Category category)throws Exception{
        try{
            return categoryMapper.addSystem(category);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 更新类目
     */
    public int updateSystem(Category category)throws Exception{
        try{
            return categoryMapper.updateSystem(category);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 删除类目
     */
    public int deleteSystem(Category category)throws Exception{
        try{
            return categoryMapper.deleteSystem(category);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询系统信息
     */
    public ResultBean querySystem(String id)throws Exception{
        try{
            return categoryMapper.querySystem(id);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 查询所有系统信息
     */
    public JsonResult findAllSystem(){
        JsonResult result = new JsonResult();
        List<ResultBean> sysList = categoryMapper.findSystemList();
        List<SysBean> allList = categoryMapper.searchAll();
        List list = new ArrayList();
        for(int i=0;i<sysList.size();i++){
            List list1 = new ArrayList();
            Map map1 = new HashMap();
            ResultBean a = sysList.get(i);
            map1.put(Constant.id,a.getCode());
            map1.put(Constant.name,a.getName());
            map1.put(Constant.sort,a.getSort());
            for(int y=0;y<allList.size();y++) {
                Map map2 = new HashMap();
                SysBean b = allList.get(y);
                if (a.getCode().equals(b.getId())) {
                    map2.put(Constant.id, b.getCode());
                    map2.put(Constant.name, b.getSubSystemName());
                    map2.put(Constant.sort,b.getSort());
                    list1.add(map2);
                }
            }
            map1.put("subSystem",list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);
        return result;
    }
}
