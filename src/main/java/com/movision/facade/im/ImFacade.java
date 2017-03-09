package com.movision.facade.im;

import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.ImConstant;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.common.util.ShiroUtil;
import com.movision.exception.BusinessException;
import com.movision.mybatis.imFirstDialogue.entity.ImFirstDialogue;
import com.movision.mybatis.imFirstDialogue.entity.ImMsg;
import com.movision.mybatis.imFirstDialogue.service.ImFirstDialogueService;
import com.movision.mybatis.imSystemInform.entity.ImSystemInform;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.imuser.service.ImUserService;
import com.movision.utils.JsonUtils;
import com.movision.utils.SignUtil;
import com.movision.utils.convert.BeanUtil;
import com.movision.utils.im.CheckSumBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * 网易云通讯接口
 *
 * @Author zhuangyuhao
 * @Date 2017/3/6 17:07
 */
@Service
public class ImFacade {

    private static Logger log = LoggerFactory.getLogger(ImFacade.class);

    @Autowired
    private ImUserService imUserService;

    @Autowired
    private ImFirstDialogueService imFirstDialogueService;

    /**
     * 发起IM请求，获得响应
     *
     * @param url    向IM服务器发送的请求的url
     * @param params 接口的传参
     * @return
     * @throws IOException
     */
    public Map<String, Object> sendImHttpPost(String url, Map<String, Object> params) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(url);
            log.info("请求的云信url:" + url);

            String appKey = ImConstant.APP_KEY;
            String appSecret = ImConstant.APP_SECRET;
            String nonce = SignUtil.generateString(32);
            String curTime = String.valueOf((new Date()).getTime() / 1000L);
            String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

            // 设置请求的header
            httpPost.addHeader("AppKey", appKey);
            httpPost.addHeader("Nonce", nonce);
            httpPost.addHeader("CheckSum", checkSum);
            httpPost.addHeader("CurTime", curTime);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

            // 设置请求的参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            List<String> keyList = new ArrayList<String>(params.keySet());
            for (String key : keyList) {
                nvps.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
            }
            log.info("请求云信接口的传参params：" + params);
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));

            // 执行请求
            CloseableHttpResponse response = httpclient.execute(httpPost);

            // 打印执行结果
            // {"code":200,"info":{"token":"a967478ef49bd18cfaa369dec8b6a74f","accid":"test_create_user","name":""}}
//        System.out.println(EntityUtils.toString(response.getEntity(), "utf-8"));
            try {
                HttpEntity entity = response.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String result = EntityUtils.toString(entity, "utf-8");
                Map<String, Object> resultMap = JsonUtils.getObjectMapFromJsonString(result);
//                log.info("转换成map的结果："+resultMap);
                EntityUtils.consume(entity);
                log.info("返回的结果：" + resultMap);
                return resultMap;
            } finally {
                response.close();
            }

        } catch (ConnectTimeoutException e) {
            log.error("请求连接超时：" + e.getMessage());
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            log.error("返回超时：" + e.getMessage());
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
        return null;
    }


    /**
     * 向Im服务器注册IM用户
     *
     * @throws IOException
     */
    public Map<String, Object> registerIM(ImUser imUser) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("accid", imUser.getAccid());

        if (StringUtils.isNotEmpty(imUser.getName())) {
            params.put("name", imUser.getName());
        }

        if (StringUtils.isNotEmpty(imUser.getIcon())) {
            params.put("icon", imUser.getIcon());
        }

        if (StringUtils.isNotEmpty(imUser.getToken())) {
            params.put("token", imUser.getToken());
        }

        return this.sendImHttpPost(ImConstant.CREATE_USER_URL, params);
    }

    /**
     * 新增IM用户
     *
     * @param imUser
     * @return 把返回值给app端
     * @throws IOException
     */
    public ImUser AddImUser(ImUser imUser) throws IOException {

        Map res = this.registerIM(imUser);

        if (res.get("code").equals(200)) {

            String info = JsonUtils.getJsonStringFromObj(res.get("info"));
            Map infoMap = JsonUtils.getObjectMapFromJsonString(info);
            String token = String.valueOf(infoMap.get("token"));
            String accid = String.valueOf(infoMap.get("accid"));
            String name = String.valueOf(infoMap.get("name"));
            log.info("注册IM用户从服务器返回的值：token=" + token + ",accid=" + accid + ",name=" + name);

            //im用户信息入库
            ImUser finalImUser = new ImUser();
            finalImUser.setAccid(accid);
            finalImUser.setToken(token);
            finalImUser.setName(name);
            finalImUser.setUserid(ShiroUtil.getAppUserID());
            imUserService.addImUser(finalImUser);


        } else {
            throw new BusinessException(MsgCodeConstant.create_im_accid_fail, "创建云信id失败");
        }
        return imUserService.selectByUserid(ShiroUtil.getAppUserID());
    }


    /**
     * 查找当前用户的IM信息
     *
     * @return
     */
    public ImUser getImuserByCurrentAppuser() {
        return imUserService.selectByUserid(ShiroUtil.getAppUserID());
    }

    /**
     * 判断是否存在IM账号
     *
     * @return true:存在；  false:不存在
     */
    public Boolean isExistImuser() {
        ImUser imUser = imUserService.selectByUserid(ShiroUtil.getAppUserID());
        return null != imUser;
    }

    /**
     * 更新IM用户名片
     *
     * @param map accid 必填
     *            name，icon， sign， email， birth， mobile， gender， ex
     * @return {
     * "code":200
     * }
     * @throws IOException
     */
    public Map updateImUserInfo(Map map) throws IOException {
        return this.sendImHttpPost(ImConstant.UPDATE_USER_INFO_URL, map);
    }


    /**
     * 查询IM名片
     *
     * @param accids 对应的accid串，如：["zhangsan"]，一次最多200个
     * @return
     * @throws IOException
     */
    public Map queryImuserInfo(String[] accids) throws IOException {

        Map<String, Object> params = new HashMap<>();
        //需要把入参以字符串数组的形式，转化成json字符串
        Gson gson = new Gson();
        String str = gson.toJson(accids);
        params.put("accids", str);
        return this.sendImHttpPost(ImConstant.GET_USER_INFO, params);
    }


    /**
     * 加好友
     *
     * @param accid  加好友发起者accid
     * @param faccid 加好友接收者accid
     * @param type   1直接加好友，2请求加好友，3同意加好友，4拒绝加好友
     * @param msg    加好友对应的请求消息，第三方组装，最长256字符
     * @return
     * @throws IOException
     */
    public Map addFriend(String accid, String faccid, int type, String msg) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("accid", accid);
        params.put("faccid", faccid);
        params.put("type", type);
        if (StringUtils.isNotEmpty(msg)) {
            params.put("msg", msg);
        }
        return this.sendImHttpPost(ImConstant.ADD_FRIEND, params);
    }

    /**
     * 增加一条加好友的记录
     *
     * @param toAccid
     * @param msg
     * @return
     */
    public int addFirstDialogue(String toAccid, String msg) {
        ImFirstDialogue imFirstDialogue = new ImFirstDialogue();
        imFirstDialogue.setUserid(ShiroUtil.getAppUserID());
        imFirstDialogue.setFromid(ShiroUtil.getAccid());
        imFirstDialogue.setToid(toAccid);
        imFirstDialogue.setBody(msg);

        return imFirstDialogueService.addDialogue(imFirstDialogue);
    }


    /**
     * 发消息接口
     *
     * @param imMsg
     * @return
     * @throws IOException
     */
    public Map sendMsg(ImMsg imMsg) throws IOException {

        Map<String, Object> params = BeanUtil.ImBeanToMap(imMsg);

        return this.sendImHttpPost(ImConstant.SEND_MSG, params);
    }

    /**
     * 判断是否存在该次对话记录
     *
     * @param toAccid
     * @return
     */
    public Boolean isExistFirstDialog(String toAccid) {
        ImFirstDialogue imFirstDialogue = new ImFirstDialogue();
        imFirstDialogue.setFromid(ShiroUtil.getAccid());
        imFirstDialogue.setToid(toAccid);

        int result = imFirstDialogueService.isExistFirstDialogue(imFirstDialogue);
        return result == 1;
    }

    /**
     * 判断被我打招呼的人是否回复
     *
     * @param replyAccid
     * @return
     */
    public Boolean isExistReply(String replyAccid) {
        ImFirstDialogue imFirstDialogue = new ImFirstDialogue();
        imFirstDialogue.setFromid(replyAccid);
        imFirstDialogue.setToid(ShiroUtil.getAccid());

        int result = imFirstDialogueService.isExistReply(imFirstDialogue);
        return result == 1;
    }

    /**
     * 查询我和对方的对话记录
     *
     * @param toAccid
     * @return
     */
    public List<ImFirstDialogue> selectFirstDialog(String toAccid) {
        ImFirstDialogue imFirstDialogue = new ImFirstDialogue();
        imFirstDialogue.setFromid(ShiroUtil.getAccid());
        imFirstDialogue.setToid(toAccid);

        return imFirstDialogueService.selectFirstDialog(imFirstDialogue);
    }

    /**
     * @param imMsg
     * @param addFriendType
     * @param responseMsg
     * @return
     * @throws IOException
     */
    public Response doFirstCommunicate(ImMsg imMsg, int addFriendType, String responseMsg) throws IOException {
        Response response = new Response();
        //1 发消息
        Map sendMsgResult = this.sendMsg(imMsg);
        Object code_1 = sendMsgResult.get("code");

        /**
         *  addFriendType=2 请求加好友
         *  addFriendType=3 接受加好友
         */
        Map map = this.addFriend(ShiroUtil.getAccid(), imMsg.getTo(), addFriendType, imMsg.getBody());
        Object code_2 = map.get("code");

        if (code_2.equals(200) && code_1.equals(200)) {
            response.setCode(200);
            response.setMessage(responseMsg + "成功");
            //3 记录发送的消息
            this.addFirstDialogue(imMsg.getTo(), imMsg.getBody());
        } else {
            response.setCode(400);
            response.setMessage(responseMsg + "失败");
        }

        return response;
    }


    /*public int recordSystemInform(String body){

        ImSystemInform imSystemInform = new ImSystemInform();
        imSystemInform.setBody(body);
        imSystemInform.setFromAccid();


    }*/


}
