package com.zhuhuibao.service;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import com.zhuhuibao.pojo.ProductSearchSpec;
import com.zhuhuibao.service.exception.ServiceException;

public interface IProductsService {

	Map<String, Object> search(ProductSearchSpec spec) throws ServiceException;

}
