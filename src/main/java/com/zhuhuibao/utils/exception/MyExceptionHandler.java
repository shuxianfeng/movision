package com.zhuhuibao.utils.exception;

import com.zhuhuibao.common.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import java.sql.SQLException;

/**
 *异常处理统一处理类
 *  @author pl
 */
//@ControllerAdvice
public class MyExceptionHandler{
    private final static Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult defaultErrorHandler(Exception e) throws Exception {
        JsonResult jsonResult = new JsonResult();
        try {
            if (e instanceof BusinessException) {
                jsonResult.setCode(400);
                jsonResult.setMessage(((BusinessException) e).getMessage());
                jsonResult.setMsgCode(((BusinessException) e).getMsgid());
            }
            if (e instanceof DataAccessException) {
                jsonResult.setCode(400);
                jsonResult.setMessage("数据库访问异常");
                jsonResult.setMsgCode(10031);
            }
            if (e instanceof SQLException) {
                jsonResult.setCode(400);
                jsonResult.setMessage(((SQLException) e).getMessage());
                jsonResult.setMsgCode(((SQLException) e).getErrorCode());
            }

        }
        catch(Exception ex)
        {
            if (log.isWarnEnabled()) {
                log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", ex);
            }
        }

        return jsonResult;
    }
}
