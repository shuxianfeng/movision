package com.zhuhuibao.exception;

import com.zhuhuibao.common.pojo.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import java.sql.SQLException;


/**
 * @author jianglz
 * @version 2016-05-05
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult defaultErrorHandler(Exception ex) throws Exception {

        if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
            throw ex;

        JsonResult result = new JsonResult();

        String exceptionName = ex.getClass().getSimpleName();
        log.error(exceptionName);
        result.setMessage(ex.getMessage());

        try {

            switch (exceptionName) {
                case "AuthException":
                    result.setCode(401);
                    result.setMsgCode(((AuthException) ex).getMsgid());
                    break;
                case "BusinessException":
                    result.setCode(400);
                    result.setMsgCode(((BusinessException) ex).getMsgid());
                    break;
                case "DataAccessException":
                    result.setCode(400);
                    result.setMessage("数据库访问异常");
                    result.setMsgCode(10031);
                    break;
                case "SQLException":
                    result.setCode(400);
                    result.setMsgCode(((SQLException) ex).getErrorCode());
                    break;
                case "MissingServletRequestParameterException":
                case "ServletRequestBindingException":
                case "ConversionNotSupportedException":
                case "TypeMismatchException":
                case "HttpMessageNotReadableException":
                case "HttpMessageNotWritableException":
                case "MethodArgumentNotValidException":
                case "MissingServletRequestPartException":
                case "BindException":
                    result.setCode(400);
                    break;
                case "NoHandlerFoundException":
                case "NoSuchRequestHandlingMethodException":
                    result.setCode(404);
                    break;
                case "HttpRequestMethodNotSupportedException":
                    result.setCode(405);
                    break;
                case "HttpMediaTypeNotAcceptableException":
                    result.setCode(405);
                    break;
                case "HttpMediaTypeNotSupportedException":
                    result.setCode(415);
                    break;
                case "MissingPathVariableException":
                    result.setCode(500);
                    break;
                default:
                    result.setCode(500);
                    result.setMessage("服务器繁忙," + ex.getMessage());
                    break;
            }

        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", e);
            }
        }



        return result;
    }

}
