package io.infinite.datasync.common;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLException;

@ControllerAdvice
public class ExceptionHandlerBean {


    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public ApiResponse<Object> handleNoParamException(final Exception ex, final WebRequest req) {
        return ApiResponse.createFail("缺少相关参数-" + ex.getMessage());
    }


    @ExceptionHandler(value = {SQLException.class})
    @ResponseBody
    public ApiResponse<Object> handleSqlxception(final Exception ex, final WebRequest req) {
        return ApiResponse.createFail("sql执行出错-" + ex.getMessage());
    }

    

}

