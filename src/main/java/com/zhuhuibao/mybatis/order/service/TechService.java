package com.zhuhuibao.mybatis.order.service;

import com.zhuhuibao.alipay.service.direct.DirectService;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 *
 * 技术培训业务处理
 */
@Service
public class TechService {

    private static final Logger log = LoggerFactory.getLogger(TechService.class);


    @Autowired
    DirectService directService;
    /**
     * 立即支付
     * @param response   HttpServletResponse
     * @param paramMap   请求参数集合
     */
    public void doPay(HttpServletResponse response,Map<String,String> paramMap){
        log.debug("技术培训购买课程立即支付请求参数:{}", paramMap.toString());
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String  sHtmlText = directService.alipayRequst(paramMap);
            out.write(sHtmlText);
            out.flush();
            return;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取输出流异常：" + e.getMessage());
            throw new BusinessException(MsgCodeConstant.ALIPAY_PARAM_ERROR,e.getMessage());

        } finally {
            if (out != null)
                out.close();
        }
    }
}
