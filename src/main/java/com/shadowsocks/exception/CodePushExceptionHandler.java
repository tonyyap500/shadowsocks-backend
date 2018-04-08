package com.shadowsocks.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class CodePushExceptionHandler {

    private static final String LEVEL = "error";

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public VndErrors.VndError internalException(Exception e) {
        log.error("服务器内部错误 {}", e);
        return new VndErrors.VndError(LEVEL, e.getMessage());
    }
}
