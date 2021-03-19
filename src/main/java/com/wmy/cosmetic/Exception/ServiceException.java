package com.wmy.cosmetic.Exception;

public class ServiceException extends RuntimeException {
//    private static final long serialVersionUID = 8428864816994640969L;

    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
