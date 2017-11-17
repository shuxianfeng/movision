package com.movision.facade.route;

import com.google.gson.Gson;
import com.movision.controller.app.im.RouteController;
import com.movision.mybatis.imLoginRecord.entity.ImLoginRecord;
import com.movision.mybatis.imLoginRecord.service.ImLoginRecordService;
import com.movision.utils.DateUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/10 14:04
 */
@Service
public class RouteFacade {

    public static final Logger log = LoggerFactory.getLogger(RouteFacade.class);

    @Autowired
    private ImLoginRecordService imLoginRecordService;

    public byte[] readBody(HttpServletRequest request) throws IOException {
        if (request.getContentLength() > 0) {
            byte[] body = new byte[request.getContentLength()];
            IOUtils.readFully(request.getInputStream(), body);
            return body;
        } else
            return null;
    }

    /**
     * 判断是否存在相同的im登录事件记录
     *
     * @param imLoginRecord
     * @return true 存在相同的记录
     * false 不存在
     */
    public Boolean isExistRecord(ImLoginRecord imLoginRecord) {
        ImLoginRecord record = imLoginRecordService.queryRecordByaccidAndTimestamp(imLoginRecord);
        return null != record;
    }

    public int addRecord(ImLoginRecord imLoginRecord) {
        return imLoginRecordService.addRecord(imLoginRecord);
    }

    /**
     * 云信im登录后的业务处理
     *
     * @param requestBody
     */
    public void busiProcessWhenAddImLoginRecord(String requestBody) {

        log.debug("md5、checkSum 验证通过");
        //业务处理
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(requestBody, Map.class);

        ImLoginRecord imLoginRecord = new ImLoginRecord();
        imLoginRecord.setAccid(map.get("accid"));
        imLoginRecord.setEventType(map.get("eventType"));
        imLoginRecord.setClientIp(map.get("clientIp"));
        imLoginRecord.setClientType(map.get("clientType"));
        imLoginRecord.setCode(map.get("code"));
        imLoginRecord.setSdkVersion(map.get("sdkVersion"));
        imLoginRecord.setTimestamp(DateUtils.stampToDate(map.get("timestamp")));

        //查看DB中是否存在相同的accid和时间戳
        if (isExistRecord(imLoginRecord)) {
            log.info("已经存在相同的登录事件记录");
        } else {
            //入库
            addRecord(imLoginRecord);
        }
    }

}
