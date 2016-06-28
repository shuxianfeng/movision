package com.zhuhuibao.test;

import com.google.gson.Gson;
import com.zhuhuibao.common.pojo.RefundReqBean;
import com.zhuhuibao.fsearch.utils.StringUtil;
import com.zhuhuibao.mybatis.order.entity.Invoice;
import com.zhuhuibao.utils.IdGenerator;
import com.zhuhuibao.utils.oss.ImageUtil;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.awt.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author jianglz
 * @since 14/11/7.
 */
public class Test extends BaseSpringContext {


    @org.junit.Test
    public void testConstants() throws Exception {


    }

    public static void main(String[] args) {
//        for (int i = 0;i<10;i++) {
//            if(i > 3)  break;
//
//            System.out.println(i);
//        }

//        String s = "fs:/attachment/goods/201411121427199489.jpg";
//        s = s.replace("fs:","http://122.194.5.221/statics");
//        System.out.println(s);
//        Date d = new Date();
//
//        SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//12小时制
//
//        System.out.println(ss.format(d));
//
//        Date date = new Date();
//
//        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
//
//        String LgTime = sdformat.format(date);
//
//        System.out.println(LgTime);
//        String s = "localhost:8080/index";
//        int index = s.lastIndexOf("/");
//        System.out.print(index);
//
//        String json = "{\n" +
//                "    \"createid\": 1,\n" +
//                "    \"name\": \"1\",\n" +
//                "    \"unit\": \"1\",\n" +
//                "    \"number\": \"1\",\n" +
//                "    \"price\": \"1\",\n" +
//                "    \"repository\": \"1\",\n" +
//                "    \"imgUrl\": \"\",\n" +
//                "    \"detailDesc\": \"<p>11</p>\",\n" +
//                "    \"paras\": \"<p>22</p>\",\n" +
//                "    \"service\": \"<p>33</p>\",\n" +
//                "    \"params\": [{\n" +
//                "        \"pname\": \"A\",\n" +
//                "        \"pvalue\": \"A1，A2\"\n" +
//                "    }],\n" +
//                "    \"paramPrice\": [{\n" +
//                "        \"fname\": \"A\",\n" +
//                "        \"fvalue\": \"A1\",\n" +
//                "        \"sname\": \"\",\n" +
//                "        \"svalue\": \"\",\n" +
//                "        \"price\": \"1\",\n" +
//                "        \"repository\": \"11\",\n" +
//                "        \"imgUrl\": \"\"\n" +
//                "    }, {\n" +
//                "        \"fname\": \"A\",\n" +
//                "        \"fvalue\": \"A2\",\n" +
//                "        \"sname\": \"\",\n" +
//                "        \"svalue\": \"\",\n" +
//                "        \"price\": \"2\",\n" +
//                "        \"repository\": \"22\",\n" +
//                "        \"imgUrl\": \"\"\n" +
//                "    }]\n" +
//                "}";
//
//        Gson gson  = new Gson();
//        JsonBean bean = gson.fromJson(json,JsonBean.class);
//        System.out.println(bean.getCreateid());

//        String md5Pwd = new Md5Hash("1122aa",null,2).toString();
//        System.out.println(md5Pwd);
//        String s = "";
//        if(s){
//            String[] ss =s.split(",|，");
//            System.out.println(ss.length);
//        }else{
//
//        }
//        Map<String,Object> map1 = new HashMap<>();
//        Map<String,Object> partyB = new HashMap<>();
//        Map<String,String> a1 = new HashMap<>();
//        a1.put("a11","a11");
//        a1.put("a12","a12");
//        Map<String,String> a2 = new HashMap<>();
//        a2.put("a21","a21");
//        a2.put("a22","a22");
//        Map<String,String> a3 = new HashMap<>();
//        a3.put("a31","a31");
//        a3.put("a32","a32");
//        Map<String,String> a4 = new HashMap<>();
//        a4.put("a41","a41");
//        a4.put("a42","a42");
//        partyB.put("a1",a1);
//        partyB.put("a2",a2);
//        partyB.put("a3",a3);
//        partyB.put("a4",a4);
//        map1.put("partyB",partyB);
//
//
//        Gson gson = new Gson();
//        gson.toJson(map1);
//        System.out.println(gson.toJson(map1));
//        Long start = System.currentTimeMillis();
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Long end = System.currentTimeMillis();
//
//        System.out.println("use time :" + (end - start) + "ms");
//        System.out.println(StringUtils.isEmpty("null"));

//        String str = "\n" +
//                "{\n" +
//                "\titems:[{\n" +
//                "\t\t\"orderNo\": \"20987389302938\",\n" +
//                "\t\t\"payType\": \"1\", // \n" +
//                "\t\t\"fee\": \"100\"\n" +
//                "\t}]\n" +
//                "\n" +
//                "}";
//        RefundReqBean bean;
//        Gson gson = new Gson();
//        bean = gson.fromJson(str,RefundReqBean.class);
//        System.out.println(bean.getItems().get(0).getOrderNo());


//        System.out.println(IdGenerator.createSNcode());
//        String sms = "AAAA_{0}_BBB_{1}";

//        String ss = MessageFormat.format(sms,"11","22");

//        System.out.println(ss);

//        String s = "100.00";
//        System.out.println(Long.valueOf(s));
//        Invoice invoice = new Invoice();
//        invoice.setAddress("address1");
//        invoice.setArea("1");
//        invoice.setCity("2");
//        invoice.setCreateId((long) 1);
//        invoice.setCreateTime(new Date());
//        invoice.setInvoiceTitle("aaaaaa");
//        invoice.setInvoiceTitleType(2);
//        invoice.setMobile("18652093798");
//        invoice.setOrderNo("snddasdasdasda");
//        invoice.setProvince("232323");
//        invoice.setReceiveName("xxxxxxsd");
//
//        Gson gson = new Gson();
//        String json = gson.toJson(invoice);
//        System.out.println(json);

//        File file = new File("/Users/jianglz/Downloads/config.properties");
//        System.out.println(file.getName());

        String status = ImageUtil.syncImageDetection("http://sandbox.zhuhui8.com/upload/img/1926145411463400712827.jpg");
        System.out.println(status);

    }


}
