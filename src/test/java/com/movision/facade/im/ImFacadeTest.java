package com.movision.facade.im;

import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.test.SpringTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/3/7 10:10
 */
public class ImFacadeTest extends SpringTestCase {
    @Autowired
    private ImFacade imFacade;

    @Test
    public void testQueryImuserInfo() throws Exception {
        String[] arr = {"80aed026fefba277b74b49068f1acee0"};
        Map map = imFacade.queryImuserInfo(arr);
        System.out.println(map.toString());
    }



    /*@Test
    public void testsendMsg() throws Exception {
        ImMsg imMsg = new ImMsg();
        imMsg.setFrom("88ca46a578cd0dfd2b3bca70ec2ce52c");  //326
        imMsg.setOpe(0);
        imMsg.setTo("0cd48d260580fd8ff3ce5b4fed193ae0");    //314
        imMsg.setType(0);

        Gson gson1 = new Gson();
        Map body = new HashMap();
        body.put("msg", "测试普通消息的推送能否成功,时间戳：" + DateUtils.getCurrentDate());
        String bodyJsonStr = gson1.toJson(body);

        imMsg.setBody(bodyJsonStr);

        Gson gson = new Gson();
        Map optionMap = new HashMap();
        optionMap.put("push", true);
        String option = gson.toJson(optionMap);

        imMsg.setOption(option);
        imMsg.setPushcontent("测试普通消息的推送能否成功,时间戳：" + DateUtils.getCurrentDate());

        imFacade.sendMsg(imMsg);
    }*/

/*
    @Test
    public void imHttpPost() throws Exception {
        ImUser imUser = new ImUser();
        imUser.setAccid("test_7");

        Map res = imFacade.registerIM(imUser);
        //解析返回值
        if(res.get("code").equals(200)){
            System.out.println("进来了");

            String info = JsonUtils.getJsonStringFromObj(res.get("info"));
            Map infoMap = JsonUtils.getObjectMapFromJsonString(info);
            String token = String.valueOf(infoMap.get("token"));
            System.out.println(token);
        }
    }
*/

/*
    @Test
    public void imHttpPost() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("accid", "test_create_user");
        params.put("token", "123");
        params.put("props", "这是一个测试");
        imFacade.imHttpPost(ImConstant.UPDATE_USER_TOKEN_URL, params );
    }
*/


/*
    @Test
    public void imHttpPost() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("accid", "test_create_user");
        imFacade.imHttpPost(ImConstant.REFRESH_USER_TOKEN_URL, params );
    }
*/

/*
    @Test
    public void imHttpPost() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("accid", "test_create_user");
        params.put("name","我是一个测试");
        params.put("mobile","18051989558");
        params.put("email","747369066@qq.com");
        params.put("gender", 1);
        params.put("birth", "1991-07-11");

        imFacade.imHttpPost(ImConstant.UPDATE_USER_INFO_URL, params );
    }
*/

/*
    @Test
    public void imHttpPost() throws Exception {
        Map<String, Object> params = new HashMap<>();
        //需要把入参以字符串数组的形式，转化成json字符串
        Gson gson = new Gson();
        String[] arr = {"test_create_user"};
        String str = gson.toJson(arr);
        params.put("accids", str);
        imFacade.sendImHttpPost(ImConstant.GET_USER_INFO, params );
    }
*/


    @Test
    public void addIm() throws Exception {
        ImUser imUser = new ImUser();
        imUser.setAccid("test_7");

        imFacade.AddImUser(imUser);
    }


}