package com.zhuhuibao.mybatis.category.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.pojo.ResultBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.oms.mapper.CategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("cs")
@Transactional
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    CategoryMapper categoryMapper;

    public ResultBean getFcateByScate(int scateid) {
        ResultBean fcate = null;
        try {
            List<ResultBean> lfcate = categoryMapper.getFcateByScate(scateid);
            if (null != lfcate && lfcate.size() > 0) {
                fcate = lfcate.get(0);
            }
        } catch (Exception ex) {
            log.error("查询执行失败>>>", ex);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
        return fcate;
    }

    /**
     * 根据大系统id查询子系统
     */
    public List<ResultBean> findSubSystemList(String id) {
        try {
            return categoryMapper.findSubSystemList(id);
        } catch (Exception e) {
            log.error("查询执行失败>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
    }

    public List<Map<String, String>> findSubSystemByPid(String id) {
        try {
            return categoryMapper.findSubSystemByPid(id);
        } catch (Exception e) {
            log.error("查询执行失败>>>",e);
            throw new BusinessException(MsgCodeConstant.DB_SELECT_FAIL, "查询失败");
        }
    }
}
