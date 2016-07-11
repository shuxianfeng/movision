package com.zhuhuibao.fsearch.service;

import com.zhuhuibao.fsearch.pojo.spec.ProductSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;

import java.util.Map;

public interface IProductsService {

	Map<String, Object> search(ProductSearchSpec spec) throws ServiceException;

}
