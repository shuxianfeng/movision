package com.zhuhuibao.fsearch.service;

import java.util.Map;

import com.zhuhuibao.fsearch.pojo.ProductSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;

public interface IProductsService {

	Map<String, Object> search(ProductSearchSpec spec) throws ServiceException;

}
