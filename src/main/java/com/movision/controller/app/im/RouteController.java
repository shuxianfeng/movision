package com.movision.controller.app.im;

import com.google.gson.Gson;
import com.movision.common.Response;
import com.movision.common.constant.ImConstant;
import com.movision.facade.route.RouteFacade;
import com.movision.mybatis.imLoginRecord.entity.ImLoginRecord;
import com.movision.utils.im.CheckSumBuilder;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/10 13:58
 */
@RestController
@RequestMapping("/app/im/route")
public class RouteController {

    public static final Logger log = LoggerFactory.getLogger(RouteController.class);

    @Autowired
    private RouteFacade routeFacade;

    @ApiOperation(value = "记录im登录事件消息抄送", notes = "记录im登录事件消息抄送", response = Response.class)
    @RequestMapping(value = "/add_im_login_record", method = RequestMethod.POST)
    public Response mockClient(HttpServletRequest request)
            throws Exception {
        Response result = new Response();
        try {
            // 获取请求体
            byte[] body = routeFacade.readBody(request);
            if (body == null) {
                log.warn("request wrong, empty body!");
                result.setCode(400);
                return result;
            }
            // 获取部分request header，并打印
            String ContentType = request.getContentType();
            String AppKey = request.getHeader("AppKey");
            String CurTime = request.getHeader("CurTime");
            String MD5 = request.getHeader("MD5");
            String CheckSum = request.getHeader("CheckSum");
            log.info("request headers: ContentType = [{}], AppKey = [{}], CurTime = [{}], MD5 = [{}], CheckSum = [{}]",
                    new Object[]{ContentType, AppKey, CurTime, MD5, CheckSum});
            // 将请求体转成String格式，并打印
            String requestBody = new String(body, "utf-8");
            log.info("request body = {}", requestBody);
            // 获取计算过的md5及checkSum
            String verifyMD5 = CheckSumBuilder.getMD5(requestBody);
            String verifyChecksum = CheckSumBuilder.getCheckSum(ImConstant.APP_SECRET, verifyMD5, CurTime);
            log.debug("verifyMD5 = {}, verifyChecksum = {}", verifyMD5, verifyChecksum);

            // 比较md5、checkSum是否一致，以及后续业务处理
            if (MD5.equals(verifyMD5) && CheckSum.equals(verifyChecksum)) {

                routeFacade.busiProcessWhenAddImLoginRecord(requestBody);
                result.setCode(200);
            } else {
                log.warn("md5、checkSum 验证不通过");
                result.setCode(400);
            }

            return result;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            result.setCode(400);
            return result;
        }
    }
}
