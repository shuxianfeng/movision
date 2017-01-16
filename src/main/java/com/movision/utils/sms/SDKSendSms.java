package com.movision.utils.sms;

import com.google.gson.Gson;
import com.movision.utils.PropertiesUtils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 发送短信接口
 * @author zhuangyuhao
 * @since 16/6/6.
 */
public class SDKSendSms {



    public static Boolean sendSMS(String mobile, String params, String templateCode){

        String gateWay = PropertiesUtils.getValue("sms_gateway");

        if("alidayu".equals(gateWay)){
            return SDKSendTaoBaoSMS.sendSMS(mobile,params,templateCode);
        } else if("yuntongxun".equals(gateWay)){
            return SDKSendTemplateSMS.sendSMS(mobile,params,templateCode);
        }

        return false;
    }


    //测试
    public static void main(String[] args) throws IOException {

        Map<String,String> map = new LinkedHashMap<>();
        map.put("name","SB是怎么炼成的");
        map.put("time","2016-06-09 12:00:33");
        map.put("code","04sb250,094sb0");
        Gson gson = new Gson();
        String json = gson.toJson(map);
        SDKSendSms.sendSMS("18652093798",json,PropertiesUtils.getValue("course_begin_sms_template_code"));

    }
}
