package com.waitfall.framework.handle;


import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.util.SaResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jacky
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public SaResult validateException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<String> list = new ArrayList<>();
        for (FieldError error : fieldErrors) {
            list.add(error.getDefaultMessage());
        }
        return SaResult.error("参数有误！" + list.getFirst());
    }

    @ExceptionHandler(value = Exception.class)
    public SaResult exceptionHandle(Exception e) {
        log.error("捕获异常：", e);
        return SaResult.error("系统错误");
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public SaResult exceptionHandle(NoResourceFoundException e) {
        log.error("捕获异常：", e);
        return new SaResult(404, "资源不存在",null);
    }

    /*
        * 自定义异常，直接弹窗提示
     */
    @ExceptionHandler(SaTokenException.class)
    public SaResult exceptionHandler(SaTokenException ex) {
        return new SaResult(ex.getCode(), ex.getMessage(),null);
    }

    @ExceptionHandler(value = NotLoginException.class)
    public SaResult exceptionHandler(NotLoginException e) {
        log.error(e.getMessage());
        return new SaResult(SaResult.CODE_NOT_LOGIN, "请先登录",null);
    }

    @ExceptionHandler(value = NotPermissionException.class)
    public SaResult exceptionHandler(NotPermissionException e) {
        log.error(e.getMessage());
        return new SaResult(SaResult.CODE_NOT_LOGIN, "没有权限",null);
    }


}