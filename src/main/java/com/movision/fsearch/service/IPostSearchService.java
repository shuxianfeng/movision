package com.movision.fsearch.service;

import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.exception.ServiceException;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/27 16:29
 */
public interface IPostSearchService {
    Map<String, Object> search(NormalSearchSpec spec) throws ServiceException;
}
