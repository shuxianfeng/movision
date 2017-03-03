package com.movision.facade.address;

import com.movision.common.constant.Constants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.service.AddressService;
import com.movision.utils.ListUtil;
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
        //判断是否是第一次新增地址，如果是第一次，则默认设置为默认地址
        List<Map<String, Object>> list = this.queryMyAddressList(ShiroUtil.getAppUserID());
        if (ListUtil.isEmpty(list)) {
            address.setIsdefault(Constants.DEFAULT_ADDRESS);
        } else {
            address.setIsdefault(Constants.NOT_DEFAULT_ADDRESS);
        }
        address.setUserid(ShiroUtil.getAppUserID());
        return addressService.addAddress(address);
    }

    public void updateAddress(Address address) {
        int n = addressService.updateAddress(address);
        if (n != 1) {
            throw new BusinessException(MsgCodeConstant.app_edit_my_address_fail, "app用户编辑我的地址失败");
        }
    }

    public Map queryAddressDetail(int id) {
        return addressService.queryAddressDetail(id);
    }


}
