package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.Member;
import com.zhuhuibao.mybatis.memCenter.mapper.AgentMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.MemberMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.ProvinceMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.MapUtil;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.file.FileUtil;
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
 * 询报价业务处理 Created by cxx on 2016/3/29 0029.
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
    ZhbService zhbService;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    com.zhuhuibao.common.constant.ApiConstants ApiConstants;

    /**
     * 询价保存
     */
    public Response saveAskPrice(AskPrice askPrice) throws Exception {
        log.debug("询价保存");
        Response result = new Response();
        if (askPrice.getBillurl() != null && !askPrice.getBillurl().equals("")) {
            String fileUrl = askPrice.getBillurl();
            if (!fileUtil.isExistFile(fileUrl, "doc", "price")) {
                throw new BusinessException(MsgCodeConstant.file_not_exist, "文件不存在");
            }
        }
        boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.XJFB.toString());
        if (bool) {
            askPriceMapper.saveAskPrice(askPrice);
            zhbService.payForGoods(askPrice.getId(), ZhbPaymentConstant.goodsType.XJFB.toString());
        } else {// 支付失败稍后重试，联系客服
            throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
        }
        // else {
        // askPriceMapper.saveAskPrice(askPrice);
        // result.setCode(200);
        // }
        return result;
    }

    /**
     * 根据Id具体某条询价信息
     */
    public AskPriceBean queryAskPriceByID(String id) {
        log.debug("根据Id具体某条询价信息");
        AskPriceBean bean = new AskPriceBean();
        try {
            Long mem_id = ShiroUtil.getCreateID();
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("createid", mem_id);
            bean = askPriceMapper.queryAskPrice(map);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(bean.getEndTime());
            if (date.before(new Date())) {
                bean.setStatusName("结束");
            } else {
                bean.setStatusName("报价中");
            }

            if ("1".equals(bean.getType())) {
                bean.setTypeName("公开询价");
            } else {
                bean.setTypeName("定向询价");
            }

            if ("1".equals(bean.getIsTax())) {
                bean.setIsTaxName("含税报价");
            } else {
                bean.setIsTaxName("非含税报价");
            }
            if (!bean.getIsShow()) {
                bean.setCompanyName("");
                bean.setLinkMan("");
                bean.setTelephone("");
                bean.setEmail("");
            }
        } catch (Exception e) {
            log.error("queryAskPriceByID error", e);
        }
        return bean;
    }

    /**
     * 获得我的联系方式（询报价者联系方式）
     */
    public Map<String, String> getLinkInfo(String id) {
        Map<String, String> map = new HashMap<>();
        Member member = memberMapper.findMemById(id);
        if (member != null) {
            map.put(Constants.companyName, member.getEnterpriseName());
            map.put(Constants.telephone, member.getFixedTelephone());
            map.put(Constants.mobile, member.getFixedMobile());
            map.put("email", member.getEmail());
            if ("2".equals(member.getIdentify())) { // 个人用户
                map.put(Constants.linkMan, member.getPersonRealName());
            } else {
                map.put(Constants.linkMan, member.getEnterpriseLinkman());
            }
        }
        return map;
    }

    /**
     * 查询询价信息
     * 
     * @param pager
     * @param askPriceSearch
     * @return
     */
    public List<AskPriceResultBean> findAllEnquiryList(Paging<AskPriceResultBean> pager, AskPriceSearchBean askPriceSearch) {

        return askPriceMapper.findAllByPager1(pager.getRowBounds(), askPriceSearch);
    }

    /**
     * 查询询价信息
     * 
     * @param askId
     * @return
     */
    public AskPriceBean findAskPriceByAskidMemId(Long askId, Long memberId) {
        Map<String, Object> map = MapUtil.convert2HashMap("askId", askId, "memberId", memberId);

        return askPriceMapper.queryAskPriceByAskidMemId(map);
    }

    /**
     * 根据ID查询询价信息
     * 
     * @param askId
     * @return
     */
    public AskPriceBean findAskPriceById(Long askId) {

        return askPriceMapper.queryAskPriceByID(String.valueOf(askId));
    }

    /**
     * 根据条件查询询价信息（分页）
     */
    public List<AskPriceResultBean> findAllByPager(Paging<AskPriceResultBean> pager, AskPriceSearchBean askPriceSearch) {
        log.debug("查询询价信息（分页）");
        List<AskPriceResultBean> resultBeanList = askPriceMapper.findAll(askPriceSearch);
        List<AskPriceResultBean> resultBeanList1 = askPriceMapper.findAllByPager1(pager.getRowBounds(), askPriceSearch);
        List askList = new ArrayList();
        for (AskPriceResultBean resultBean : resultBeanList1) {
            Map askMap = new HashMap();
            askMap.put(Constants.id, resultBean.getId());
            askMap.put(Constants.title, resultBean.getTitle());
            askMap.put(Constants.status, resultBean.getStatus());
            askMap.put(Constants.type, resultBean.getType());
            askMap.put(Constants.publishTime, resultBean.getPublishTime().substring(0, 10));
            askMap.put(Constants.area, resultBean.getArea());
            List offerList = new ArrayList();
            for (AskPriceResultBean resultBean1 : resultBeanList) {
                if (resultBean.getId().equals(resultBean1.getAskid())) {
                    Map offerMap = new HashMap();
                    offerMap.put(Constants.id, resultBean1.getOfferid());
                    offerMap.put(Constants.offerTime, resultBean1.getOfferTime().substring(0, 19));
                    offerMap.put(Constants.companyName, resultBean1.getCompanyName());
                    offerMap.put(Constants.address, resultBean1.getAddress());
                    offerList.add(offerMap);
                }
            }
            askMap.put("offerList", offerList);
            askList.add(askMap);
        }

        return askList;
    }

    /**
     * 最新公开询价(限六条)
     */
    public Response queryNewPriceInfo(int count, String createid) {
        Response response = new Response();
        List<AskPrice> askPriceList = askPriceMapper.queryNewPriceInfo(count, createid);
        List list = new ArrayList();
        for (AskPrice askPrice : askPriceList) {
            Map map = new HashMap();
            map.put(Constants.id, askPrice.getId());
            map.put(Constants.companyName, askPrice.getTitle());
            list.add(map);
        }
        response.setCode(200);
        response.setData(list);
        return response;
    }

    /**
     * 最新公开询价(分页)
     */
    public List<AskPrice> queryNewPriceInfoList(Paging<AskPrice> pager, AskPriceSearchBean askPriceSearch) {
        List<AskPrice> askPriceList = askPriceMapper.findAllNewPriceInfoList(pager.getRowBounds(), askPriceSearch);
        List list = new ArrayList();
        for (AskPrice askPrice : askPriceList) {
            Map map = new HashMap();
            map.put(Constants.id, askPrice.getId());
            map.put(Constants.title, askPrice.getTitle());
            map.put(Constants.publishTime, askPrice.getEndTime().substring(0, 10));
            map.put(Constants.area, askPrice.getProvinceCode());
            map.put("isCan", askPrice.getIsCan());
            map.put("count", askPrice.getCount());
            list.add(map);
        }
        return list;
    }
}
