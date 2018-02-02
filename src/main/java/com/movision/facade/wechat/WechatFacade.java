package com.movision.facade.wechat;

import com.movision.common.Response;
import com.movision.utils.HttpClientUtils;
import com.movision.utils.propertiesLoader.PropertiesDBLoader;
import com.movision.utils.wechat.WechatUtils;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2018/2/1 16:55
 * 小程序使用的Facade层（租赁商城）
 */
@Service
public class WechatFacade {

    private static Logger log = LoggerFactory.getLogger(WechatFacade.class);

    @Autowired
    private PropertiesDBLoader propertiesDBLoader;

    /**
     * 使用登录凭证 code 获取 session_key 和 openid
     * @param code
     * @return
     */
    public Response getOpenid(String code){
        Response response = new Response();
        String appid = propertiesDBLoader.getValue("appid");//小程序的appid（参数配置表）
        String secret = propertiesDBLoader.getValue("secret");//小程序的appsecret（参数配置表）
        String grant_type = propertiesDBLoader.getValue("grant_type");//grant_type填写为 authorization_code（参数配置表）

        String requrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> params = new HashMap<>();
        params.put("appid", appid);
        params.put("secret", secret);
        params.put("js_code", code);
        params.put("grant_type", grant_type);

        Map<String, String> resmap = HttpClientUtils.doGet(requrl, params, "utf-8");

        log.debug(resmap.get("status"));
        log.debug(resmap.get("result"));
        JSONObject json = JSONObject.fromObject(resmap.get("result"));

        if (resmap.get("status").equals("200")){
            response.setCode(200);
            try {
                String key = WechatUtils.get3rdstr();
                String openid = json.getString("openid");
                String session_key = json.getString("session_key");

                //将session_key和openid存入session中
                Subject currentUser = SecurityUtils.getSubject();
                Session session = currentUser.getSession();
                session.setAttribute(key, session_key + "+" + openid);
                log.debug("key:" + key);
                log.debug("session_key+openid:" + session_key + "+" + openid);

                response.setData(key);
            }catch (Exception e){
                response.setCode(40029);
                response.setMessage("invalid code!");
            }
        }

        return response;
    }


}
