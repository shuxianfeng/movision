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

import java.io.File;
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

    @Autowired
    ApiConstants ApiConstants;
    /**
     * 询价保存
     */
    public JsonResult saveAskPrice(AskPrice askPrice){
        log.debug("询价保存");
        JsonResult result = new JsonResult();
        if(askPrice.getBillurl()!=null && !askPrice.getBillurl().equals("")){
            String fileUrl = askPrice.getBillurl();
            fileUrl = ApiConstants.getUploadDoc()+ Constant.upload_price_document_url+"/"+fileUrl;
            File file = new File(fileUrl);
            if(file.exists()){
                askPriceMapper.saveAskPrice(askPrice);
                result.setCode(200);
            }
            else
            {
                result.setCode(400);
                result.setMessage("文件不存在");
                result.setMsgCode(MsgCodeConstant.file_not_exist);
            }
        }else{
            askPriceMapper.saveAskPrice(askPrice);
            result.setCode(200);
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
            if(!bean.getIsShow()){
                bean.setCompanyName("");
                bean.setLinkMan("");
                bean.setTelephone("");
                bean.setEmail("");
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
     * 根据品牌id查询代理商跟厂商（区域分组）
     */
    public JsonResult getAgentByBrandid(String id){
        JsonResult result = new JsonResult();
        List<ResultBean> provinceList = provinceMapper.findProvince();
        List<ResultBean> agentList = agentMapper.getAgentByBrandid(id);
        ResultBean resultBean = agentMapper.findManufactorByBrandid(id);
        List list = new ArrayList();
        Map map = new HashMap();
        Map map3 = new HashMap();
        map3.put(Constant.id,resultBean.getCode());
        map3.put(Constant.name,resultBean.getName());
        map.put("manufactor",map3);
        for(int i=0;i<provinceList.size();i++){
            ResultBean province = provinceList.get(i);
            Map map1 = new HashMap();
            map1.put(Constant.id,province.getCode());
            map1.put(Constant.name,province.getName());
            List list1 = new ArrayList();
            for(int j=0;j<agentList.size();j++){
                ResultBean agent = agentList.get(j);
                Map map2 = new HashMap();
                if(agent.getSmallIcon().contains(province.getCode())){
                    map2.put(Constant.id,agent.getCode());
                    map2.put(Constant.name,agent.getName());
                    list1.add(map2);
                }
            }
            map1.put("agentList",list1);
            list.add(map1);
        }
        map.put("agent",list);
        result.setCode(200);
        result.setData(map);
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
            if("2".equals(member.getIdentify())){
                map.put(Constant.companyName,member.getPersonRealName());
                map.put(Constant.linkMan,"");
                map.put(Constant.telephone,member.getFixedTelephone());
                map.put(Constant.mobile,member.getFixedMobile());
            }else{
                map.put(Constant.companyName,member.getEnterpriseName());
                map.put(Constant.linkMan,member.getEnterpriseLinkman());
                map.put(Constant.telephone,member.getFixedTelephone());
                map.put(Constant.mobile,member.getFixedMobile());
            }
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
        List<AskPriceResultBean> resultBeanList = askPriceMapper.findAll(askPriceSearch);
        List<AskPriceResultBean> resultBeanList1 = askPriceMapper.findAllByPager1(pager.getRowBounds(),askPriceSearch);
        List askList = new ArrayList();
        for(int i=0;i<resultBeanList1.size();i++){
            AskPriceResultBean resultBean = resultBeanList1.get(i);
            Map askMap = new HashMap();
            askMap.put(Constant.id,resultBean.getId());
            askMap.put(Constant.title,resultBean.getTitle());
            askMap.put(Constant.status,resultBean.getStatus());
            askMap.put(Constant.type,resultBean.getType());
            askMap.put(Constant.publishTime,resultBean.getPublishTime().substring(0,10));
            askMap.put(Constant.area,resultBean.getArea());
            List offerList = new ArrayList();
            for(int y=0;y<resultBeanList.size();y++){
                AskPriceResultBean resultBean1 = resultBeanList.get(y);
                if(resultBean.getId().equals(resultBean1.getAskid())){
                    Map offerMap = new HashMap();
                    offerMap.put(Constant.id,resultBean1.getOfferid());
                    offerMap.put(Constant.offerTime,resultBean1.getOfferTime().substring(0,19));
                    offerMap.put(Constant.companyName,resultBean1.getCompanyName());
                    offerMap.put(Constant.address,resultBean1.getAddress());
                    offerList.add(offerMap);
                }
            }
            askMap.put("offerList",offerList);
            askList.add(askMap);
        }

        return askList;
    }

    public JsonResult queryNewPriceInfo(){
        JsonResult jsonResult = new JsonResult();
        List<AskPrice> askPriceList = askPriceMapper.queryNewPriceInfo();
        List list = new ArrayList();
        for(int i=0;i<askPriceList.size();i++){
            AskPrice askPrice = askPriceList.get(i);
            Map map = new HashMap();
            map.put(Constant.id,askPrice.getId());
            map.put(Constant.companyName,askPrice.getTitle());
            list.add(map);
        }
        jsonResult.setCode(200);
        jsonResult.setData(list);
        return jsonResult;
    }
}
