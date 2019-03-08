package com.qhjys.springcloud.enums;

public enum ErrorCode {
    ERROR_SYSTEM(0, "系统错误"),
    ERROR_PARAM(1, "参数错误"),
    ERROR_NO_TOKEN(-1, "当前链接无token"),
    ERROR_INVALID_TOKEN(-2, "当前账户已失效，请重新登录"),
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
