package com.movision.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import com.movision.common.constant.WePayConstant;
/**
 * 签名工具类，随机数工具类
 * @author zhuangyuhao
 * @time   2016年10月12日 下午2:42:30
 *
 */
public class SignUtil {
	
	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	 /** 
     * 返回一个定长的随机字符串(只包含大小写字母、数字) 
     *  
     * @param length 
     *            随机字符串长度 
     * @return 随机字符串 
     */  
    public static String generateString(int length) {  
        StringBuffer sb = new StringBuffer();  
        Random random = new Random();  
        for (int i = 0; i < length; i++) {  
            sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));  
        }  
        return sb.toString();  
    }  
    
    /**
     * 生成签名方法
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding, SortedMap<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + WePayConstant.SIGN_KEY);//最后加密时添加商户密钥，由于key值放在最后，所以不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }
    
    
    public static void main(String[] args) {
    	System.out.println(generateString(32));
	}
    
    
}
