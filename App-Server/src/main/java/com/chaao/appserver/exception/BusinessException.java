package com.chaao.appserver.exception;

//自定义异常类，继承运行时异常类

import lombok.Getter;

@Getter
public class BusinessException   extends RuntimeException{

    // 错误码
    private Integer code;
    // 错误信息
    private String msg;
    //traceId
    private String traceId;

    public BusinessException(Integer code, String msg, String traceId) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.traceId = traceId;

    }


}
