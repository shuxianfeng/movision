package com.zhuhuibao.utils.exception;

import com.zhuhuibao.common.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.AnnotationUtils;
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

        try {
            if (ex instanceof NoSuchRequestHandlingMethodException) {
                result.setCode(404);
            }

            if (ex instanceof HttpRequestMethodNotSupportedException) {
                result.setCode(405);
            }

            if (ex instanceof HttpMediaTypeNotSupportedException) {
                result.setCode(415);
            }

            if (ex instanceof HttpMediaTypeNotAcceptableException) {
                result.setCode(406);
            }

            if (ex instanceof MissingPathVariableException) {
                result.setCode(500);
            }

            if (ex instanceof MissingServletRequestParameterException) {
                result.setCode(400);
            }

            if (ex instanceof ServletRequestBindingException) {
                result.setCode(400);
            }

            if (ex instanceof ConversionNotSupportedException) {
                result.setCode(400);
            }

            if (ex instanceof TypeMismatchException) {
                result.setCode(400);
            }

            if (ex instanceof HttpMessageNotReadableException) {
                result.setCode(400);
            }

            if (ex instanceof HttpMessageNotWritableException) {
                result.setCode(400);
            }

            if (ex instanceof MethodArgumentNotValidException) {
                result.setCode(400);
            }

            if (ex instanceof MissingServletRequestPartException) {
                result.setCode(400);
            }

            if (ex instanceof BindException) {
                result.setCode(400);
            }

            if (ex instanceof NoHandlerFoundException) {
                result.setCode(404);
            }

            if (ex instanceof BusinessException) {
                result.setCode(500);
            }
        } catch (Exception var6) {
            if (log.isWarnEnabled()) {
                log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", var6);
            }
        }

        result.setMessage(ex.getMessage());

        return result;
    }

}
