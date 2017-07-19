package com.movision.utils;

import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author zhanglei
 * @Date 2017/7/19 9:01
 */
@Service
@Transactional
public class MiPushUtils {


    private String ANDROID_APP_SECRET = "buBra5C0T8kt9DcvOh7zBw==";
    private String ANDROID_PACKAGE_NAME = "com.syjm.movision";
    private static int DEVELOPMENT_MODE = 0; // 1=正式环境/0=测试

    public final static int TYPE_ANDROID = 0;

    /**
     * 构造函数
     */
    /**public MiPushUtils(String androidAppSecret, String androidPackageName, int developmentMode) {
     this.ANDROID_APP_SECRET = androidAppSecret;
     this.ANDROID_PACKAGE_NAME = androidPackageName;
     MiPushUtils.DEVELOPMENT_MODE = developmentMode;
     }*/

    /**
     * 调用小米推送
     */
    public static void reStartPush(int deviceType) {
        // 如果为测试环境
        if (DEVELOPMENT_MODE == 0) {
            // 测试环境只提供对IOS支持，不支持Android
            Constants.useSandbox();
            if (deviceType == TYPE_ANDROID) {
                Constants.useOfficial();
            }
        } else {
            // 正式环境
            Constants.useOfficial();
        }
    }

    /**
     * 构建android推送信息
     *
     * @param title
     * @param content
     * @param jsonObjectPayload
     * @param timeToSend
     * @return
     */
    /**
     * 系统推送
     *
     * @param body
     * @param title
     * @return
     */
    public Message buildMessage2Android(String body, String title, JSONObject jsonObjectPayload) throws Exception {
        String PACKAGENAME = ANDROID_PACKAGE_NAME;
        Message message = new Message.Builder()
                .title(title)
                .description(body).payload(body)
                .restrictedPackageName(PACKAGENAME)
                .passThrough(1)  //消息使用透传方式
                .notifyType(1)     // 使用默认提示音提示
                .extra("flow_control", "4000")     // 设置平滑推送, 推送速度4000每秒(qps=4000)
                .build();
        return message;
    }


    /**
     * 构建发送信息
     *
     * @param title
     * @param body
     * @param jsonObjectPayload
     * @param deviceType
     * @param
     * @return Message
     */
    public Message buildMessage(String title, String body, JSONObject jsonObjectPayload, int deviceType) throws Exception {
        Message message = null;
        if (deviceType == TYPE_ANDROID) {
            message = buildMessage2Android(title, body, jsonObjectPayload);
        }
        return message;
    }

    /**
     * 向所有设备发送推送信息
     *
     * @param title
     * @param content
     * @param jsonObjectPayload
     * @param deviceType
     * @param
     * @throws Exception
     */
    public Result sendBroadcastAll(String title, String content, JSONObject jsonObjectPayload, int deviceType) throws Exception {
        reStartPush(deviceType);// 准备小米推送

        Sender sender = null;
        if (deviceType == TYPE_ANDROID) {
            sender = new Sender(ANDROID_APP_SECRET); // 需要根据appSecert来发送
        }
        Message message = buildMessage(title, content, jsonObjectPayload, deviceType);
        Result result = sender.broadcastAll(message, 0);// 推送消息给所有设备，不重试

        return result;
    }


}
