package com.movision.facade.address;

import com.movision.common.util.ShiroUtil;
import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.service.AddressService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 19:52
 */
@Service
public class AddressFacade {
    @Autowired
    private AddressService addressService;

    public List<Map<String, Object>> queryMyAddressList(int userid) {
        Map map = new HashedMap();
        map.put("userid", userid);
        return addressService.queryMyAddressList(map);
    }

    public int addMyAddress(Address address) {
        address.setUserid(ShiroUtil.getAppUserID());
        return addressService.addAddress(address);
    }
}
