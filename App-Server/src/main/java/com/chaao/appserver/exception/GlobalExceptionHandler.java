package com.chaao.appserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vo.Result;

import java.util.stream.Collectors;

//这个注解作用： 统一处理异常
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. 处理自定义业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        // 记录业务异常日志（包含 traceId 等上下文）
        //getTraceId : 获取 获取当前请求的全局唯一追踪标识（Trace ID）自定义的
        log.warn("业务异常: code={}, msg={}", e.getCode(), e.getMessage());
        return Result.error(e.getCode(), e.getMessage(), e.getTraceId());
    }

    // 2. 处理参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                      .map(FieldError::getDefaultMessage)
                      .collect(Collectors.joining("; "));
        return Result.error(400, "VALIDATION_FAILED", msg);
    }

    //BadCredentialsException 属于 SpringSecurity 内置认证异常，
    //你全局异常处理器 GlobalExceptionHandler 没有单独捕获这个异常，
    //走到了兜底未知异常，直接返回 500。

    // 专门捕获 用户名/密码错误
    @ExceptionHandler(BadCredentialsException.class)
    public Result<?> handleBadCredentialsException(BadCredentialsException e){
        log.error("登录账号密码错误：{}",e.getMessage());
        // 返回你前端能识别的格式
        return Result.error(400,"用户名或密码错误",null);
    }

    // 账号不存在
    public Result<?> handleUsernameNotFoundException(UsernameNotFoundException e){
        return Result.error(400,e.getMessage(),null);
        // return new ResultVO(400,e.getMessage(),null);
    }


    // 3. 兜底处理所有未知异常
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 记录完整堆栈到日志系统
        log.error("系统未知异常", e);
        // 不暴露技术细节给前端，返回通用提示
        return Result.error(500, "INTERNAL_ERROR", "服务暂不可用");
//                             .body(new ErrorResponse("INTERNAL_ERROR", "服务暂不可用"));
    }
}