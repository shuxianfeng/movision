package com.zhuhuibao.service;

import java.io.File;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import com.zhuhuibao.pojo.ContractorSearchSpec;
import com.zhuhuibao.pojo.SupplierSearchSpec;
import com.zhuhuibao.service.exception.ServiceException;

public interface IMembersService {

	Map<String, Object> searchContractors(ContractorSearchSpec spec) throws ServiceException;
	
	Map<String, Object> searchSuppliers(SupplierSearchSpec spec) throws ServiceException;

}
