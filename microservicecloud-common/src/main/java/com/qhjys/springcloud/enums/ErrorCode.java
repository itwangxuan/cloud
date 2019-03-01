package com.qhjys.springcloud.enums;

public enum ErrorCode {
    ERROR_SYSTEM(0, "系统错误"),
    ERROR_PARAM(1, "参数错误"),
    ERROR_NO_TOKEN(-1, "当前链接无token"),
    ERROR_INVALID_TOKEN(-2, "当前账户已失效，请重新登录"),

    CUSTOMER_CODE(40000, "WAP客户端，请以40000开始！"),
    CUSTOMER_ERROR_40001(40001, "暂未绑定微信，请先使用手机号绑定微信"),
    CUSTOMER_ERROR_40002(40002, "您的账户被禁用"),
    CUSTOMER_ERROR_40003(40003, "openid为空"),
    CUSTOMER_ERROR_40004(40004, "手机号不存在，不可发送验证码"),
    CUSTOMER_ERROR_40005(40005, "验证码有误"),
    CUSTOMER_ERROR_40006(40006, "当前账户还未购买过产品"),
    CUSTOMER_ERROR_40007(40007, "验证码发送频繁"),
    CUSTOMER_ERROR_40008(40008, "无效的openid"),
    CUSTOMER_ERROR_40009(40009, "验证码发送失败"),
    CUSTOMER_ERROR_40010(40010, "获取微信access_token失败"),
    CUSTOMER_ERROR_40011(40011, "获取微信用户信息失败"),
    CUSTOMER_ERROR_40012(40012, "获取验证码次数已上限，请明天再试"),
    CUSTOMER_ERROR_40013(40013, "该手机号已被绑定微信，请先解绑后操作"),
    CUSTOMER_ERROR_40014(40014, "您的微信号已绑定其他手机，请先解绑后操作"),
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
