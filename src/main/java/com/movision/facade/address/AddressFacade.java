package com.movision.facade.address;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.movision.common.constant.Constants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.address.entity.Address;
import com.movision.mybatis.address.service.AddressService;
import com.movision.mybatis.cart.entity.CartVo;
import com.movision.mybatis.cart.service.CartService;
import com.movision.mybatis.shopAddress.entity.ShopAddress;
import com.movision.mybatis.shopAddress.service.ShopAddressService;
import com.movision.utils.CalculateFee;
import com.movision.utils.ListUtil;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.apache.commons.collections.map.HashedMap;
import com.movision.utils.baidu.SnCal;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 19:52
 */
@Service
public class AddressFacade {
    private static Logger log = LoggerFactory.getLogger(AddressFacade.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShopAddressService shopAddressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CalculateFee calculateFee;

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
            //获取设置：是否是默认地址
            Integer isdefault = address.getIsdefault();
            if (null == isdefault) {
                //未设置默认地址
                address.setIsdefault(Constants.NOT_DEFAULT_ADDRESS);
            } else {
                //1 设置了默认地址
                address.setIsdefault(isdefault);
                //2 同时取消原来的默认地址
                Address defaultAddress = addressService.queryMyDefaultAddress(ShiroUtil.getAppUserID());
                defaultAddress.setIsdefault(Constants.NOT_DEFAULT_ADDRESS);
                addressService.updateAddress(defaultAddress);
            }

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

    public int setPosition(String shopid, String provincecode, String citycode, String areacode, String street) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int flag = 0;//设置标志位
        //根据省市区县的code查询省市区县名称
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("provincecode", Integer.parseInt(provincecode));
        parammap.put("citycode", Integer.parseInt(citycode));
        parammap.put("areacode", Integer.parseInt(areacode));
        Address address = addressService.queryNameByCode(parammap);
        String addressStr = address.getProvince() + address.getCity() + address.getDistrict() + street;
        //通过地址计算经纬度
        String sn = SnCal.getSn(address.getProvince() + address.getCity() + address.getDistrict() + street);
//        String sn = SnCal.getSn(street);
        String ak = PropertiesLoader.getValue("baidu.ak");

        //通过http的get请求url
        String baiduurl = PropertiesLoader.getValue("baidu.url");
        //拼接百度接口的请求url
        String url = baiduurl + "?address=" + addressStr + "&output=json&ak=" + ak + "&sn=" + sn;
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet httpGet = new HttpGet(url);//这里发送get请求
            // 获取当前客户端对象
            CloseableHttpClient httpclient = HttpClients.createDefault();
            // 通过请求对象获取响应对象
            CloseableHttpResponse response = httpclient.execute(httpGet);

            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            log.info("返回的json结果集>>>>>>>>>>>>>>>>>>>>>" + result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            int status = (int) jsonObject.get("status");
            if (status == 0) {
                flag = 1;//可请求到正确的横纵坐标，设置成功
                JSONObject re = (JSONObject) JSON.toJSON(jsonObject.get("result"));
                JSONObject location = (JSONObject) JSON.toJSON(re.get("location"));
                BigDecimal lng = (BigDecimal) JSON.toJSON(location.get("lng"));
                BigDecimal lat = (BigDecimal) JSON.toJSON(location.get("lat"));
                log.info("经纬度>>>>>>>>" + lng + ">>>>>>>" + lat);
                //持久化地址和经纬度
                ShopAddress shopAddress = new ShopAddress();
                shopAddress.setShopid(Integer.parseInt(shopid));
                shopAddress.setProvincecode(Integer.parseInt(provincecode));
                shopAddress.setProvincename(address.getProvince());
                shopAddress.setCitycode(Integer.parseInt(citycode));
                shopAddress.setCityname(address.getCity());
                shopAddress.setAreacode(Integer.parseInt(areacode));
                shopAddress.setAreaname(address.getDistrict());
                shopAddress.setStreet(street);
                shopAddress.setLng(lng);
                shopAddress.setLat(lat);
                shopAddress.setIntime(new Date());
                shopAddressService.saveShopAddress(shopAddress);
            } else if (status == 1) {
                flag = -2;//无相关结果
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = -1;//设置地址失败
        }

        return flag;
    }

    public ShopAddress queryPosition(String shopid) {
        return shopAddressService.queryShopAddressByShopid(Integer.parseInt(shopid));
    }

    public Map<String, Object> calculateLogisticsfee(String cartids, String provincecode, String citycode, String positionprovincecode, String positioncitycode, String lng, String lat) {
        //定义运费变量map
        Map<String, Object> feemap = new HashMap<>();

        //首先根据cartids取出用户需要结算的所有商品
        String[] cartidarr = cartids.split(",");
        int[] cartid = new int[cartidarr.length];
        for (int i = 0; i < cartidarr.length; i++) {
            cartid[i] = Integer.parseInt(cartidarr[i]);
        }
        List<CartVo> cartVoList = cartService.queryCartVoList(cartid);//查询需要结算的购物车所有商品

        //根据判断条件来决定是否在这里进行运费结算----结算时调用计算运费的公共方法
        if (positionprovincecode.equals(provincecode) && positioncitycode.equals(citycode)) {
            //调用公共计算接口计算运费
            feemap = calculateFee.GetFee(cartVoList, new BigDecimal(lng), new BigDecimal(lat));
        }

        return feemap;
    }
}
