package com.zhuhuibao.service.payment;

import com.zhuhuibao.common.Response;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.common.constant.ZhbPaymentConstant;
import com.zhuhuibao.common.util.ShiroUtil;
import com.zhuhuibao.exception.AuthException;
import com.zhuhuibao.mybatis.memCenter.entity.Resume;
import com.zhuhuibao.mybatis.memCenter.service.ResumeService;
import com.zhuhuibao.mybatis.payment.service.PaymentGoodsService;
import com.zhuhuibao.utils.MsgPropertiesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 平台服务使用筑慧币支付
 *
 * @author pl
 * @version 2016/6/16 0016
 */
@Service
@Transactional
public class PaymentService {

    private final static Logger log = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    PaymentGoodsService goodsService;

    @Autowired
    ResumeService resume;

    /**
     * 查看已消费的商品信息
     * @param GoodsID  商品ID
     * @return
     * @throws Exception
     */
    public Response viewGoodsRecord(Long GoodsID, Object goodsInfo,String type){
        Response response = new Response();
        Long createId = ShiroUtil.getCreateID();
        Long companyId = ShiroUtil.getCompanyID();
        if(createId != null) {
            Map<String,Object> dataMap = new HashMap<String,Object>();
            Map<String,Object> con = new HashMap<String,Object>();
            //商品ID
            con.put("goodsId",GoodsID);
            con.put("companyId",companyId);
            con.put("type",type);
            //项目是否已经被同企业账号查看过
            int viewNumber = goodsService.checkIsViewGoods(con);
            if(viewNumber == 0) {
//                map  = projectService.queryProjectDetail(GoodsID);
//                goodsService.insertViewProject(GoodsID, createId,companyId,type);
//                response.setData(goodsInfo);
//                response.setMsgCode(MsgCodeConstant.ZHB_PAYMENT_TRUE);
                dataMap.put("payment", ZhbPaymentConstant.PAY_ZHB_NON_PURCHASE);
                response.setData(dataMap);

            }else
            {
                Resume resume2 = resume.previewResume(String.valueOf(GoodsID));
                dataMap.put("info",resume2);
                dataMap.put("payment", ZhbPaymentConstant.PAY_ZHB_PURCHASE);
                response.setData(resume2);
//                map  = projectService.queryProjectDetail(GoodsID);
            }
        }else{
            throw new AuthException(MsgCodeConstant.un_login, MsgPropertiesUtils.getValue(String.valueOf(MsgCodeConstant.un_login)));
        }
        return response;
    }
}
