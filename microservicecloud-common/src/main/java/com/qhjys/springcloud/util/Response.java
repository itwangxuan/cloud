package com.qhjys.springcloud.util;

import com.qhjys.springcloud.enums.ErrorCode;

import java.util.LinkedHashMap;

public class Response extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 8542653644934668818L;

    public static Response create() {
        return new Response();
    }

    public Response body(Object body) {
        success();
        if (body != null) {
            put("data", body);
        }
        return this;
    }

    public Response success() {
        this.put("status", "success");
        return this;
    }

    public Response success(String message) {
        this.put("message", message);
        return success();
    }

    public Response error() {
        this.put("status", "fail");
        this.put("code", ErrorCode.ERROR_SYSTEM.getCode());
        this.put("message", ErrorCode.ERROR_SYSTEM.getMessage());
        this.remove("data");
        return this;
    }

    public Response error(ErrorCode errorCode) {
        this.put("status", "fail");
        this.put("code", errorCode.getCode());
        this.put("message", errorCode.getMessage());
        this.remove("data");
        return this;
    }

    public Response error(String message) {
        this.put("status", "fail");
        this.put("code", 0);
        this.put("message", message);
        this.remove("data");
        return this;
    }
}
