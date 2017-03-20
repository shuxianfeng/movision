package com.movision.fsearch.service;

import com.movision.fsearch.pojo.spec.GoodsSearchSpec;
import com.movision.fsearch.service.exception.ServiceException;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/20 16:02
 */
public interface IGoodsSearchService {

    Map<String, Object> search(GoodsSearchSpec spec) throws ServiceException;
}
