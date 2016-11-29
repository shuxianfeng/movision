package com.zhuhuibao.service;

import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.PriceConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.pojo.AskPriceResultBean;
import com.zhuhuibao.common.pojo.AskPriceSearchBean;
import com.zhuhuibao.exception.BusinessException;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import com.zhuhuibao.mybatis.memCenter.service.PriceService;
import com.zhuhuibao.mybatis.zhb.service.ZhbService;
import com.zhuhuibao.shiro.realm.ShiroRealm;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import com.zhuhuibao.utils.file.FileUtil;
import com.zhuhuibao.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IDEA
 * User: zhuangyuhao
 * Date: 2016/11/24
 * Time: 13:51
 */
@Service
@Transactional
public class MobileAskAndOfferPriceService {

    private static final Logger log = LoggerFactory.getLogger(MobileAskAndOfferPriceService.class);

    @Autowired
    ZhbService zhbService;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    AskPriceMapper askPriceMapper;

    @Autowired
    private PriceService priceService;

    /**
     * 准备询价数据
     * @param askPrice
     * @return
     */
    public AskPrice  prepareAskPrice(AskPrice askPrice){
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        askPrice.setEndTime(askPrice.getEndTime() + " 23:59:59");

        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        askPrice.setCreateid(principal.getId().toString());
        return askPrice;
    }

    /**
     * 询价保存业务处理
     * @param askPrice
     * @return
     * @throws Exception
     */
    public void saveAskPrice(AskPrice askPrice) throws Exception {
        log.debug("询价保存业务处理");
        validateFileExist(askPrice);

        boolean bool = zhbService.canPayFor(ZhbPaymentConstant.goodsType.XJFB.toString());
        if (bool) {

            askPriceMapper.saveAskPrice(askPrice);
            zhbService.payForGoods(askPrice.getId(), ZhbPaymentConstant.goodsType.XJFB.toString());
        } else {
            // 支付失败稍后重试，联系客服
            throw new BusinessException(MsgCodeConstant.ZHB_PAYMENT_FAILURE, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.ZHB_PAYMENT_FAILURE)));
        }
    }

    /**
     * 校验询价单(图片)是否存在
     * @param askPrice
     */
    private void validateFileExist(AskPrice askPrice) {
        if (askPrice.getBillurl() != null && !askPrice.getBillurl().equals("")) {
            String fileUrl = askPrice.getBillurl();
            if (!fileUtil.isExistFile(fileUrl, "img", "price")) {
                throw new BusinessException(MsgCodeConstant.file_not_exist, "询价单图片不存在");
            }
        }
    }

    /**
     * 判断是否存在厂商或者供应商
     * @param supps
     * @param manu
     * @return
     */
    public Map<String,Object> isExistSupOrManu(String supps, String manu, String type){
        Map result = new HashMap();
        //如果是其他询价，并且处理向厂商询价和向经销商询价，二者有一必选
        if(!type.equals(PriceConstant.GKXJ)) {
            if(StringUtils.isEmpty(supps) && StringUtils.isEmpty(manu)){
                result.put("flag", false);
                result.put("msg", "询价中，向厂商询价和向代理商询价，两者都不存在");
                return result;
            }else{
                result.put("flag", true);
                result.put("msg", "可以进行询价");
                return  result;
            }
        }else{
            //公开询价
            result.put("flag", true);
            result.put("msg", "可以进行询价");
            return  result;
        }
    }

    /**
     * 获取我的联系方式
     * @return
     */
    public Map getMyLink(){
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);

        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        return priceService.getLinkInfo(principal.getId().toString());
    }

    /**
     * 查看我的询价单中的别人回复的报价
     * @param askPriceSearch
     * @param pageNo
     * @param pageSize
     * @return
     */
    public Paging<AskPriceResultBean>   getPager4ViewOtherOfferPrice(AskPriceSearchBean askPriceSearch,
                                                                     String pageNo,
                                                                     String pageSize){
        if (askPriceSearch.getTitle() != null) {
            if (askPriceSearch.getTitle().contains("_")) {
                askPriceSearch.setTitle(askPriceSearch.getTitle().replace("_", "\\_"));
            }
        }
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession(false);
        ShiroRealm.ShiroUser principal = (ShiroRealm.ShiroUser) session.getAttribute("member");
        askPriceSearch.setCreateid(principal.getId().toString());

        askPriceSearch.setEndTime(askPriceSearch.getEndTime() + " 23:59:59");
        Paging<AskPriceResultBean> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<AskPriceResultBean> askPriceList = priceService.findAllByPager(pager, askPriceSearch);
        pager.result(askPriceList);
        return pager;
    }
}
