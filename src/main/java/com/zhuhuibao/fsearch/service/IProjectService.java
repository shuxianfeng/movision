package com.zhuhuibao.fsearch.service;

import com.zhuhuibao.fsearch.pojo.spec.ProjectSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;

import java.util.Map;

/**
 * @author jianglz
 * @since 16/7/10.
 */
public interface IProjectService {
    Map<String, Object> searchProjectPage(ProjectSearchSpec spec) throws ServiceException;
}
