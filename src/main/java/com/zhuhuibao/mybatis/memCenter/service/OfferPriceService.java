package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.ApiConstants;
import com.zhuhuibao.common.constant.Constants;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.AskPriceBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.AskPriceSimpleBean;
import com.zhuhuibao.mybatis.memCenter.entity.OfferAskPrice;
import com.zhuhuibao.mybatis.memCenter.entity.OfferPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.mapper.OfferPriceMapper;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.file.FileUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报价业务处理类
 *
 * @author PengLong
 */
@Transactional
@Service
public class OfferPriceService {
    private static final Logger log = LoggerFactory.getLogger(OfferPriceService.class);

    @Resource
    OfferPriceMapper priceMapper;

    @Autowired
    AskPriceMapper askPriceMapper;

    @Autowired
    ApiConstants ApiConstants;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    ZhbService zhbService;

    /**
     * 我要报价
     *
     * @param price
     * @return
     */
    public Response addOfferPrice(OfferPrice price) {
        Response response = new Response();
        try {
            if (price.getBillurl() != null && !price.getBillurl().equals("")) {
                String fileUrl = price.getBillurl();
                if (StringUtils.isNotBlank(price.getMode()) && price.getMode().equalsIgnoreCase("img")) {
                    handleDocOrImg(price, response, fileUrl, "img", "图片不存在");
                } else {
                    handleDocOrImg(price, response, fileUrl, "doc", "文件不存在");
                }
            } else if (price.getContent() != null && !price.getContent().equals("")) {
                priceMapper.insertSelective(price);
            }
        } catch (Exception e) {
            log.error("add offer price error!", e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, (MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    private void handleDocOrImg(OfferPrice price, Response response, String fileUrl, String doc, String 文件不存在) throws Exception {
        if (fileUtil.isExistFile(fileUrl, doc, "price")) {
            boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.BJFB.toString());
            if (bool) {
                priceMapper.insertSelective(price);
                zhbService.payForGoods(price.getId(), ZhbPaymentConstant.goodsType.BJFB.toString());
            } else {// 支付失败稍后重试，联系客服
                throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
            }
        } else {
            response.setCode(400);
            response.setMessage(文件不存在);
            response.setMsgCode(MsgCodeConstant.file_not_exist);
        }
    }

    /**
     * 查询我的报价中的询价信息
     *
     * @param pager
     *            分页属性
     * @return product 报价信息
     */
    public List<AskPriceSimpleBean> findAllAskingPriceInfo(Paging<AskPriceSimpleBean> pager, AskPrice price) {
        if (null != price) {
            String publishTimeOrder = StringUtils.trimToEmpty(price.getPublishTimeOrder());
            String endTimeOrder = StringUtils.trimToEmpty(price.getEndTimeOrder());
            if (!ArrayUtils.contains(Constants.ORDER_TYPE_KEYWORD, publishTimeOrder.toUpperCase())) {
                price.setPublishTimeOrder(null);
            }
            if (!ArrayUtils.contains(Constants.ORDER_TYPE_KEYWORD, endTimeOrder.toUpperCase())) {
                price.setEndTimeOrder(null);
            }

        }

        return priceMapper.findAllAskingPriceInfo(pager.getRowBounds(), price);
    }

    /**
     * 根据询价ID查询该询价对应的报价信息
     * 
     * @param pager
     * @param askId
     * @return
     */
    public List<Map<String, Object>> findAllOfferPriceByAskId(Paging<Map<String, Object>> pager, Long askId) {

        return priceMapper.findAllOfferPriceByAskId(pager.getRowBounds(), askId);
    }

    /**
     * 查询我的报价中的询价信息
     *
     * @param pager
     *            分页属性
     * @return product 报价信息
     */
    public List<AskPriceSimpleBean> findAllOfferedPriceInfo(Paging<AskPriceSimpleBean> pager, Map<String, String> priceMap) {
        log.debug("分页查询我已报价的信息");
        return priceMapper.findAllOfferedPriceInfo(pager.getRowBounds(), priceMap);
    }

    /**
     * 报价查询 询价信息+报价信息
     *
     * @param id
     * @return
     */
    public Response queryOfferPriceInfoByID(Long id) {
        Response response = new Response();
        try {
            OfferAskPrice price = priceMapper.queryOfferPriceInfoByID(id);
            response.setData(price);
        } catch (Exception e) {
            log.error("add offer price error!", e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, (MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    /**
     * 下载清单
     *
     * @param id
     * @return
     */
    public String downloadBill(Long id, String type) {
        String fileurl = "";
        try {
            // 报价单
            if (type.equals("2")) {
                OfferPrice price = priceMapper.selectByPrimaryKey(id);
                if (price != null && price.getBillurl() != null) {
                    fileurl = price.getBillurl();
                }
            } // 询价单
            else if (type.equals("1")) {
                AskPriceBean askPrice = askPriceMapper.queryAskPriceByID(String.valueOf(id));
                if (askPrice != null && askPrice.getBillurl() != null) {
                    fileurl = askPrice.getBillurl();
                }
            }
        } catch (Exception e) {
            log.error("执行异常>>>", e);
            throw e;
        }
        return fileurl;
    }

    /**
     * 查看某条询价信息的所有报价信息
     *
     * @param id
     * @return
     */
    public Response queryAllOfferPriceByAskID(Long id) {
        Response response = new Response();
        try {
            List<AskPriceSimpleBean> priceList = priceMapper.queryAllOfferPriceByAskID(id);
            response.setData(priceList);
        } catch (Exception e) {
            log.error("add offer price error!", e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, (MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    /**
     * 查看回复的具体某条报价信息(清单,单一产品)
     *
     * @param id
     * @return
     */
    public Response queryOfferPriceByID(Long id) {
        Response response = new Response();
        try {
            OfferPrice price = priceMapper.selectByPrimaryKey(id);
            response.setData(price);
        } catch (Exception e) {
            log.error("add offer price error!", e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, (MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure))));
        }
        return response;
    }

    /**
     * 根据ID查询报价详情
     * 
     * @param id
     * @return
     */
    public OfferPrice getOfferPriceByID(Long id) {

        try {
            return priceMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error("getOfferPriceByID error,id=" + id, e);
            throw e;
        }

    }

    /**
     * 会员中心首页询报价相关信息
     *
     * @param createId
     * @return
     */
    public Map<String, Object> queryEnqueryQuoteCount(Long createId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("createId", createId);
            // 收到的报价
            Integer recQuoteCount = priceMapper.queryRecQuoteCount(map);
            resultMap.put("recQuoteCount", recQuoteCount);
            // 等我报价
            Integer quoteCount = priceMapper.queryQuoteCount(map);
            resultMap.put("quoteCount", quoteCount);
        } catch (Exception e) {
            log.error("query enquery quote count error!", e);
            throw new BusinessException(MsgCodeConstant.mcode_common_failure, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.mcode_common_failure)));
        }
        return resultMap;
    }
}
