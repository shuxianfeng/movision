package com.zhuhuibao.utils.sms;

import com.zhuhuibao.utils.PropertiesUtils;


/**
 * 发送短信接口
 * @author jianglz
 * @since 16/6/6.
 */
public class SDKSendSms {



    public static Boolean sendSMS(String mobile, String params, String templateCode){

        String gateWay = PropertiesUtils.getValue("sms_gateway");

        if("alidayu".equals(gateWay)){
            return SDKSendTaoBaoSMS.sendSMS(mobile,params,gateWay+"_"+ templateCode);
        } else if("yuntongxun".equals(gateWay)){
            return SDKSendTemplateSMS.sendSMS(mobile,params,gateWay+"_"+ templateCode);
        }

        return false;
    }
}
