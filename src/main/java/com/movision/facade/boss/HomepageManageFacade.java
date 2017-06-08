package com.movision.facade.boss;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.homepageManage.entity.HomepageManage;
import com.movision.mybatis.homepageManage.entity.HomepageManageVo;
import com.movision.mybatis.homepageManage.service.HomepageManageService;
import com.movision.mybatis.manageType.entity.ManageType;
import com.movision.mybatis.manageType.service.ManageTypeService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/3/6 14:28
 */
@Service
public class HomepageManageFacade {
    @Autowired
    HomepageManageService homepageManageService;

    @Autowired
    ManageTypeService manageTypeService;

    /**
     * 查询广告列表
     *
     * @param pager
     * @return
     */
    public List<HomepageManageVo> queryAdvertisementList(Paging<HomepageManageVo> pager) {
        return homepageManageService.queryAdvertisementList(pager);
    }

    /**
     * 用于查看广告详情
     *
     * @param id
     * @return
     */
    public HomepageManageVo queryAvertisementById(String id) {
        return homepageManageService.queryAvertisementById(id);
    }

    /**
     * 查询广告类型列表
     *
     * @return
     */
    public List<ManageType> queryAdvertisementTypeList(Paging<ManageType> pager) {
        return manageTypeService.queryAdvertisementTypeList(pager);
    }

    public int addAdvertisement(String topictype, String orderid, String content, String subcontent, String url, String transurl) {
        Map map = new HashedMap();
        map.put("topictype", topictype);
        map.put("orderid", orderid);
        map.put("content", content);
        map.put("subcontent", subcontent);
        map.put("url", url);
        map.put("transurl", transurl);
        map.put("intime", new Date());
        map.put("clicksum", 0);
        map.put("ordersum", 0);
        map.put("isdel", 0);
        //查询广告位置是否可以添加广告
        int resault = homepageManageService.queryIsAdd(map);
        if (resault == 1) {
            return homepageManageService.addAdvertisement(map);
        } else {
            return -1;
        }
    }

    /**
     * 添加广告类型
     *
     * @param name     广告位置
     * @param wide     宽
     * @param high     高
     * @param quantity 广告数量
     * @return
     */
    public int addAdvertisementType(String name, String wide, String high, String quantity) {
        Map map = new HashedMap();
        map.put("name", name);
        Integer type = manageTypeService.queryAdvertisementType();
        map.put("type", type + 1);
        map.put("wide", wide);
        map.put("high", high);
        map.put("quantity", quantity);
        map.put("intime", new Date());
        return manageTypeService.addAdvertisementType(map);
    }

    /**
     * 根据id查询广告类型详情
     *
     * @param id
     * @return
     */
    public ManageType queryAdvertisementTypeById(String id) {
        return manageTypeService.queryAdvertisementTypeById(id);
    }


    /**
     * 根据条件查询广告类型名称
     *
     * @param name
     * @param pager
     * @return
     */
    public List<ManageType> queryAdvertisementTypeLikeName(String name, String type, Paging<ManageType> pager) {
        Map map = new HashedMap();
        map.put("name", name);
        map.put("type", type);
        return manageTypeService.queryAdvertisementTypeLikeName(map, pager);
    }

    /**
     * 编辑广告
     *
     * @param id
     * @return
     */
    public int updateAdvertisement(String id, String orderid, String content, String subcontent, String url, String transurl) {
        Map map = new HashedMap();
        map.put("id", id);
        if (StringUtil.isNotEmpty(orderid)) {
            map.put("orderid", orderid);
        }
        map.put("content", content);
        map.put("subcontent", subcontent);
        map.put("url", url);
        map.put("transurl", transurl);

        return homepageManageService.updateAdvertisement(map);
    }

    /**
     * 根据条件查询广告列表
     *
     * @param name
     * @param type
     * @param pager
     * @return
     */
    public List<HomepageManageVo> queryAdvertisementLike(String name, String type, Paging<HomepageManageVo> pager) {
        Map map = new HashedMap();
        map.put("name", name);
        map.put("type", type);
        return homepageManageService.queryAdvertisementLike(map, pager);
    }

    /**
     * 查询广告位置排序
     *
     * @param type
     * @return
     */
    public List<Integer> queryAdvertisementLocation(String type, String orderid) {
        //查询此广告位置可以放几个广告
        Integer str = manageTypeService.queryAdvertisementLocation(type);
        //查询此广告位置下已经有几条广告
        List<Integer> integers = homepageManageService.queryAdvertisementLocation(type);
        List<Integer> i = new ArrayList<>();
        if (str >= integers.size()) {
            for (int n = 1; n <= str; n++) {
                i.add(n);
            }
            for (int m = 0; m < i.size(); m++) {
                for (int k = 0; k < integers.size(); k++) {
                    if (integers.get(k) == i.get(m)) {
                        if (StringUtil.isNotEmpty(orderid)) {
                            if (i.get(m) != Integer.parseInt(orderid)) {//返回包含当前排序的排序
                                i.remove(m);
                            }
                        } else {
                            i.remove(m);
                        }
                    }
                }
            }
            return i;
        } else {
            return i;
        }
    }

    /**
     * 操作广告排序 （0删除 1添加）
     *
     * @param type
     * @param id
     * @param orderid
     * @return
     */
    public int operationAdvertisementOrderid(String type, String id, String orderid) {
        Map m = new HashedMap();
        m.put("id", id);
        m.put("orderid", orderid);
        if (type.equals("0")) {//删除
            return homepageManageService.deleteAdvertisementOrderid(m);
        } else if (type.equals("1") && orderid != null) {//修改
            return homepageManageService.updateAtionAdvertisementOrderid(m);
        } else {
            return -1;
        }
    }



    /**
     * 编辑广告类型
     *
     * @param id
     * @param name
     * @param wide
     * @param high
     * @return
     */
    public Map updateAdvertisementType(String id, String name, String wide, String high) {
        ManageType manageType = new ManageType();
        Map map = new HashedMap();
        if (StringUtil.isNotEmpty(id)) {
            manageType.setId(Integer.parseInt(id));
        }
        if (StringUtil.isNotEmpty(name)) {
            manageType.setName(name);
        }
        if (StringUtil.isNotEmpty(wide)) {
            manageType.setWide(Integer.parseInt(wide));
        }
        if (StringUtil.isNotEmpty(high)) {
            manageType.setHigh(Integer.parseInt(high));
        }
        int i = manageTypeService.updateAdvertisementType(manageType);
        map.put("resault", i);
        return map;
    }

    /**
     * 根据id删除广告
     *
     * @param id
     * @return
     */
    public Map deleteAdvertisement(String id) {
        Map map = new HashedMap();
        int i = homepageManageService.deleteAdvertisement(Integer.parseInt(id));
        map.put("resault", i);
        return map;
    }
}
