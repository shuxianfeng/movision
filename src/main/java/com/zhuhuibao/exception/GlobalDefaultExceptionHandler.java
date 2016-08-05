package com.zhuhuibao.exception;

import com.zhuhuibao.common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
	public Response defaultErrorHandler(Exception ex) throws Exception {

		if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null)
			throw ex;

		Response result = new Response();

		String exceptionName = ex.getClass().getSimpleName();
		// log.error(exceptionName);
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
			case "BaseException":
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
			case "PageNotFoundException":
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
				result.setMessage("服务器繁忙");
				break;
			}

			throw ex;

		} catch (Exception e) {
			if (log.isWarnEnabled()) {
				log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", e);
			}
		}

		return result;
	}

}
