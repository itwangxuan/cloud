package com.qhjys.springcloud.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -4901693304442376818L;

    public ValidationException() {
        super();
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}