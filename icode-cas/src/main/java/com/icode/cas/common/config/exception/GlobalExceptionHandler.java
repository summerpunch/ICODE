package com.icode.cas.common.config.exception;

import com.icode.cas.common.constant.PathFinal;
import com.icode.cas.common.response.ResponseCodeEnum;
import com.icode.cas.common.response.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseData allExceptionHandler(HttpServletRequest request, Exception ex) {
        String url = request.getRequestURL().toString();
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(false);
        if (ex instanceof NullPointerException) {
            //空指针
            LOGGER.error("nullPointerExceptionHandler", ex);
            responseData.setCode(ResponseCodeEnum.NULL_EXCEOTION.getCode());
            responseData.setMsg(ex.getMessage());
            requestType(url, responseData, ex);
        } else if (ex instanceof MethodArgumentTypeMismatchException) {
            //方法参数校验异常
            responseData.setCode(ResponseCodeEnum.TYPE_MISMATCH_EXCEPTION.getCode());
            responseData.setMsg(ex.getMessage());
            requestType(url, responseData, ex);
        } else {
            //其余
            ex.printStackTrace();
            responseData.setCode(ResponseCodeEnum.GLOBAL_EXCEPTION.getCode());
            responseData.setMsg("GlobalExceptionHandler.exception");
            requestType(url, responseData, ex);
        }
        return responseData;
    }

    private void requestType(String url, ResponseData responseData, Exception ex) {
        if (url.endsWith(PathFinal.RES_JSON)) {
            recordLog(url, responseData, ex, PathFinal.JSON);
        } else if (url.endsWith(PathFinal.RES_PAGE)) {
            recordLog(url, responseData, ex, PathFinal.PAGE);
        } else {
            recordLog(url, responseData, ex, "");
        }
    }

    private void recordLog(String url, ResponseData data, Exception ex, String type) {
        LOGGER.error("unknown " + type + " exception, url:" + url, ex);
        LOGGER.error("unknown " + type + " exception, responseData:" + data.toString());
    }

}
