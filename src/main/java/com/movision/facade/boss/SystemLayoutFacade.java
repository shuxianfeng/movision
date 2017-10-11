package com.movision.facade.boss;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.systemLayout.entity.SystemLayout;
import com.movision.mybatis.systemLayout.service.SystemLayoutService;
import com.movision.utils.pagination.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/10/11 13:41
 */
@Service
public class SystemLayoutFacade {

    public void insertSystemLayout(String module, String type, String value, String describe) {
        SystemLayout systemLayout = new SystemLayout();
        if (StringUtil.isNotEmpty(module)) {
            systemLayout.setModule(module);
        }
        if (StringUtil.isNotEmpty(type)) {
            systemLayout.setType(type);
        }
        if (StringUtil.isNotEmpty(value)) {
            systemLayout.setValue(value);
        }
        if (StringUtil.isNotEmpty(describe)) {
            systemLayout.setDescribe(describe);
        }
        systemLayoutService.insertSystemLayout(systemLayout);
    }

    @Autowired
    private SystemLayoutService systemLayoutService;

    public List<SystemLayout> querySystemLayotAll(String type, Paging<SystemLayout> pag) {
        return systemLayoutService.querySystemLayoutAll(type, pag);
    }

    /**
     * 根据id查询系统配置详情
     *
     * @param id
     * @return
     */
    public SystemLayout querySystemLayoutById(String id) {
        if (StringUtil.isNotEmpty(id)) {
            return systemLayoutService.querySystemLayoutById(Integer.parseInt(id));
        } else {
            return null;
        }
    }

    /**
     * 编辑系统配置
     *
     * @param id
     * @param module
     * @param type
     * @param value
     * @param describe
     */
    public void updateSystemLayoutById(String id, String module, String type, String value, String describe) {
        SystemLayout layout = new SystemLayout();
        if (StringUtil.isNotEmpty(id)) {
            layout.setId(Integer.parseInt(id));
        }
        if (StringUtil.isNotEmpty(module)) {
            layout.setModule(module);
        }
        if (StringUtil.isNotEmpty(type)) {
            layout.setType(type);
        }
        if (StringUtil.isNotEmpty(value)) {
            layout.setValue(value);
        }
        if (StringUtil.isNotEmpty(describe)) {
            layout.setDescribe(describe);
        }
        systemLayoutService.updateSystemLayoutById(layout);
    }

    /**
     * 根据id删除系统配置
     *
     * @param id
     */
    public void deleteSystemLayoutById(String id) {
        systemLayoutService.deleteSystemLayoutById(Integer.parseInt(id));
    }
}
