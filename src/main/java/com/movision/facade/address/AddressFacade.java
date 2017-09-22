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
import com.movision.mybatis.city.entity.City;
import com.movision.mybatis.city.service.CityService;
import com.movision.mybatis.shopAddress.entity.ShopAddress;
import com.movision.mybatis.shopAddress.service.ShopAddressService;
import com.movision.utils.CalculateFee;
import com.movision.utils.ListUtil;
import com.movision.utils.propertiesLoader.PropertiesLoader;
import org.apache.commons.collections.map.HashedMap;
import com.movision.utils.baidu.SnCal;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @Author zhuangyuhao
 * @Date 2017/2/27 19:52
 */
@Service
public class AddressFacade {
    private static Logger log = LoggerFactory.getLogger(AddressFacade.class);
    /**
     * 秘钥
     */
    private static final String BAIDU_MAP_API_SK = PropertiesLoader.getValue("baidu.sk");

    /**
     * ak
     */
    private static final String BAIDU_MAP_API_AK = PropertiesLoader.getValue("baidu.ak");

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShopAddressService shopAddressService;

    @Autowired
    private CartService cartService;

    @Autowired
    private CalculateFee calculateFee;

    @Autowired
    private CityService cityService;


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
        Map<String, Object> parammap = wrapAddressParam(provincecode, citycode, areacode);
        Address address = addressService.queryNameByCode(parammap);

        String addressStr = address.getProvince() + address.getCity() + address.getDistrict() + street;
        //通过地址计算经纬度, 计算签名
        String sn = SnCal.getSn(wrapSignParam4GeoCoder(addressStr));
        //通过http的get请求url
        String baiduurl = PropertiesLoader.getValue("baidu.geocoding.api.url");
        //拼接百度接口的请求url
        String url = baiduurl + "?address=" + addressStr + "&output=json&ak=" + BAIDU_MAP_API_AK + "&sn=" + sn;
        String result = "";
        try {
            // 根据地址获取请求
            CloseableHttpResponse response = doHttpGetRequest(url);

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
                ShopAddress shopAddress = wrapShopAddress(shopid, provincecode, citycode, areacode, street, address, lng, lat);
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

    /**
     * 封装生成签名的传参
     *
     * @param address
     * @return
     */
    public Map wrapSignParam4GeoCoder(String address) {
        // 计算sn跟参数对出现顺序有关，get请求请使用LinkedHashMap保存<key,value>，该方法根据key的插入顺序排序；post请使用TreeMap保存<key,value>，
        // 该方法会自动将key按照字母a-z顺序排序。
        // 所以get请求可自定义参数顺序（sn参数必须在最后）发送请求，但是post请求必须按照字母a-z顺序填充body（sn参数必须在最后）。
        // 以get请求为例：http://api.map.baidu.com/geocoder/v2/?address=百度大厦&output=json&ak=yourak，paramsMap中先放入address，
        // 再放output，然后放ak，放入顺序必须跟get请求中对应参数的出现顺序保持一致。
        Map paramsMap = new LinkedHashMap<>();
        paramsMap.put("address", address);
        paramsMap.put("output", "json");
        paramsMap.put("ak", BAIDU_MAP_API_AK);
        return paramsMap;
    }

    public Map wrapSignParam4ReverseGeoCoder(String location) {
        Map paramsMap = new LinkedHashMap<>();
        paramsMap.put("callback", "renderReverse");
        paramsMap.put("location", location);
        paramsMap.put("output", "json");
        paramsMap.put("ak", BAIDU_MAP_API_AK);
        return paramsMap;
    }

    private Map<String, Object> wrapAddressParam(String provincecode, String citycode, String areacode) {
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("provincecode", Integer.parseInt(provincecode));
        parammap.put("citycode", Integer.parseInt(citycode));
        parammap.put("areacode", Integer.parseInt(areacode));
        return parammap;
    }

    private ShopAddress wrapShopAddress(String shopid, String provincecode, String citycode, String areacode, String street, Address address, BigDecimal lng, BigDecimal lat) {
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
        return shopAddress;
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

    /**
     * @param lat
     * @param lng
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public Map<String, Object> getAddressByLatAndLng(String lat, String lng) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        int flag = 0;//设置标志位
        String location = lat + "," + lng;   //纬度坐标+经度坐标
        //通过http的get请求url
        String baiduurl = PropertiesLoader.getValue("baidu.geocoding.api.url"); //http://api.map.baidu.com/geocoder/v2/
        //获取签名
        String sn = SnCal.getSn(wrapSignParam4ReverseGeoCoder(location));
        //拼接百度接口的请求url
        String url = baiduurl + "?callback=renderReverse" + "&location=" + location + "&output=json&ak=" + BAIDU_MAP_API_AK + "&sn=" + sn;
        String result = "";
        String citycode = null;
        Map map = new HashMap();
        try {
            CloseableHttpResponse response = doHttpGetRequest(url);

            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            log.info("返回的json结果集>>>>>>>>>>>>>>>>>>>>>" + result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            int status = (int) jsonObject.get("status");
            if (status == 0) {
                flag = 1;   //设置成功
                JSONObject re = (JSONObject) JSON.toJSON(jsonObject.get("result"));
                JSONObject addressComponent = (JSONObject) JSON.toJSON(re.get("addressComponent"));
                String cityname = String.valueOf(addressComponent.get("city")); //获取到城市名称
                if (StringUtils.isEmpty(cityname)) {
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "根据经纬度调用百度地图API无法获取到对应城市名称");
                }
                log.debug("调用百度sdk，根据经纬度获取到的城市名称是：" + cityname);
                //城市名称转化为城市编码
                City city = cityService.selectCityByName(cityname);
                if (null == city) {
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "根据城市名称无法获取到城市编码");
                }
                citycode = city.getCode();

            } else if (status == 1) {
                flag = -2;//无相关结果
            }
        } catch (Exception e) {
            log.error("调用百度api，根据经纬度查询城市编码失败", e);
            flag = -1;//设置地址失败
        }
        map.put("flag", flag);
        map.put("citycode", citycode);

        return map;
    }

    private CloseableHttpResponse doHttpGetRequest(String url) throws IOException {
        // 根据地址获取请求
        HttpGet httpGet = new HttpGet(url); //这里发送get请求
        // 获取当前客户端对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 通过请求对象获取响应对象
        return httpclient.execute(httpGet);
    }

}
