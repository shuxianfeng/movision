package com.movision.fsearch.service;

import com.movision.fsearch.pojo.spec.NormalSearchSpec;
import com.movision.fsearch.service.exception.ServiceException;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/8/1 10:06
 */
public interface ILabelSearchService {
    Map<String, Object> search(NormalSearchSpec spec) throws ServiceException;
}
