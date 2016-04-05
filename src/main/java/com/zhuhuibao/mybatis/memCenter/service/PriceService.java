package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.*;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.AgentMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 询报价业务处理
 * Created by cxx on 2016/3/29 0029.
 */
@Service
@Transactional
public class PriceService {

    private static final Logger log = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    AskPriceMapper askPriceMapper;

    @Autowired
    ProvinceMapper provinceMapper;

    @Autowired
    AgentMapper agentMapper;

    @Autowired
    MemberMapper memberMapper;
    /**
     * 询价保存
     */
    public JsonResult saveAskPrice(AskPrice askPrice){

        log.debug("询价保存");
        JsonResult result = new JsonResult();
        try{
            int isSave = askPriceMapper.saveAskPrice(askPrice);
            if(isSave==1){
                result.setCode(200);
            }else{
                result.setCode(400);
                result.setMessage("增加产品定向询价失败");
            }
        }catch (Exception e){
            log.error("saveAskPrice error!"+e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据Id具体某条询价信息
     */
    public JsonResult queryAskPriceByID(String id){
        log.debug("根据Id具体某条询价信息");
        JsonResult result = new JsonResult();
        try{
            AskPriceBean bean = askPriceMapper.queryAskPriceByID(id);
            SimpleDateFormat sdf =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(bean.getEndTime());
            if(date.before(new Date())){
                bean.setStatusName("结束");
            }else{
                bean.setStatusName("报价中");
            }

            if("1".equals(bean.getType())){
                bean.setTypeName("公开询价");
            }else{
                bean.setTypeName("定向询价");
            }

            if("1".equals(bean.getIsTax())){
                bean.setIsTaxName("含税报价");
            }else{
                bean.setIsTaxName("非含税报价");
            }
            result.setCode(200);
            result.setData(bean);
        }catch (Exception e){
            log.error("queryAskPriceByID error",e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获得代理商信息
     */
    public JsonResult getProxyInfoByProvince(String id){
        log.debug("获得代理商信息");
        JsonResult result = new JsonResult();
        List<ResultBean> provinceList = provinceMapper.findProvince();
        List<ResultBean> agentList = agentMapper.findAgent(id);
        List list = new ArrayList();
        for(int i=0;i<provinceList.size();i++){
            ResultBean province = provinceList.get(i);
            Map map1 = new HashMap();
            map1.put(Constant.id,province.getCode());
            map1.put(Constant.name,province.getName());
            List list1 = new ArrayList();
            for(int j=0;j<agentList.size();j++){
                ResultBean agent = agentList.get(j);
                Map map2 = new HashMap();
                if(agent.getSmallIcon().contains(province.getSmallIcon())){
                    map2.put(Constant.id,agent.getCode());
                    map2.put(Constant.name,agent.getName());
                    list1.add(map2);
                }
            }
            map1.put("agentList",list1);
            list.add(map1);
        }
        result.setCode(200);
        result.setData(list);
        return result;
    }

    /**
     * 获得我的联系方式（询报价者联系方式）
     */
    public JsonResult getLinkInfo(String id){
        JsonResult result = new JsonResult();
        Map map = new HashMap();
        Member member = memberMapper.findMemById(id);
        if(member!=null){
            map.put(Constant.companyName,member.getEnterpriseName());
            map.put(Constant.linkMan,member.getEnterpriseLinkman());
            map.put(Constant.telephone,member.getFixedTelephone());
        }
        result.setCode(200);
        result.setData(map);
        return result;
    }


    /**
     * 根据条件查询询价信息（分页）
     */
    public List<AskPriceResultBean> findAllByPager(Paging<AskPriceResultBean> pager, AskPriceSearchBean askPriceSearch){
        log.debug("查询询价信息（分页）");
        return askPriceMapper.findAllByPager(pager.getRowBounds(),askPriceSearch);
    }
}
