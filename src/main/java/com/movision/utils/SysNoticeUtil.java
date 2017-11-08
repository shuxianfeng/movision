package com.movision.utils;

import com.movision.common.Response;
import com.movision.common.constant.ImConstant;
import com.movision.facade.im.ImFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/11/6 15:55
 * 用于BOSS端各种管理员操作后，给单独用户发送的系统通知
 * 公共方法
 */
@Service
public class SysNoticeUtil {

    @Autowired
    private ImFacade imFacade;

    /**
     * 统一发送系统通知（单个用户发送）
     * @param type 发送类型：0 更改帖子的所属圈子 1 添加首页精选 2 添加圈子精选 3 删帖 4 加V 5 发现页推荐作者 6 投稿类中奖通知
     * @param circlename 圈子名称（type为0时不为空，帖子被移动至的圈子名称）
     * @param userid 通知的用户id
     * @param template 定向给用户发送任意内容：如中奖通知等（type为6是不为空）
     * @return
     */
    public Response sendSysNotice (Integer type, String circlename, int userid, String template){
        Response response = new Response();
        String title = "管理员通知";
        String content = "";
        try {
            Map<String, Object> parammap = new HashMap<>();
            parammap.put("type", type);
            if (type == 0) {
                String temp = imFacade.querySysNoticeTemp(parammap);
                content = temp.replace("XX", circlename);
            } else if (type == 1) {
                content = imFacade.querySysNoticeTemp(parammap);
            } else if (type == 2) {
                content = imFacade.querySysNoticeTemp(parammap);
            } else if (type == 3) {
                content = imFacade.querySysNoticeTemp(parammap);
            } else if (type == 4) {
                content = imFacade.querySysNoticeTemp(parammap);
            } else if (type == 5) {
                content = imFacade.querySysNoticeTemp(parammap);
            } else if (type == 6) {
                content = template;
            }
            imFacade.sendSystemInformForUser(content, title, content ,ImConstant.PUSH_MESSAGE.system_msg.getCode() , null, userid);
            response.setCode(200);
            response.setMessage("发送成功");
        }catch (Exception e){
            response.setCode(300);
            response.setMessage("发送失败");
        }
        return response;
    }
}
