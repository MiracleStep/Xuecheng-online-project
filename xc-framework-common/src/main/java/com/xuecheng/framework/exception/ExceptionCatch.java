package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    //驳货CustomException此类异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult exception(CustomException customException){
        customException.printStackTrace();
        //记录日志
        LOGGER.error("catch exception:{}",customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

    //补货Exception此类异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        exception.printStackTrace();
        //记录日志
        LOGGER.error("catch exception:{}",exception.getMessage());
        if(EXCEPTIONS == null){
            EXCEPTIONS = builder.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(Exception.class);
        if(resultCode!=null){
            return new ResponseResult(resultCode);
        }else {
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }

    }

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }
}
