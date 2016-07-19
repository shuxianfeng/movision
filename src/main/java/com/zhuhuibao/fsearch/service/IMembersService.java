package com.zhuhuibao.fsearch.service;

import com.zhuhuibao.fsearch.pojo.spec.ContractorSearchSpec;
import com.zhuhuibao.fsearch.pojo.spec.SupplierSearchSpec;
import com.zhuhuibao.fsearch.service.exception.ServiceException;

import java.util.Map;

public interface IMembersService {

	Map<String, Object> searchContractors(ContractorSearchSpec spec) throws ServiceException;
	
	Map<String, Object> searchSuppliers(SupplierSearchSpec spec) throws ServiceException;

}
