package com.movision.facade.route;

import com.movision.mybatis.imLoginRecord.entity.ImLoginRecord;
import com.movision.mybatis.imLoginRecord.service.ImLoginRecordService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author zhuangyuhao
 * @Date 2017/11/10 14:04
 */
@Service
public class RouteFacade {

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

}
