package com.authy;

import com.authy.api.Error;

/**
 * @author Julian Camargo
 * <p>
 * Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class AuthyException extends Exception {

    private final Integer status;
    private final Error.Code errorCode;

    public AuthyException(String message, Throwable throwable, Integer status) {
        super(message, throwable);
        this.status = status;
        this.errorCode = null;
    }

    public AuthyException(String message, Throwable throwable, Integer status, Error.Code errorCode) {
        super(message, throwable);
        this.status = status;
        this.errorCode = errorCode;
    }

    public AuthyException(String message, Integer status, Error.Code errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public AuthyException(String message) {
        super(message);
        this.status = null;
        this.errorCode = null;
    }

    public AuthyException(String message, Throwable throwable) {
        this(message, throwable, null, null);
    }

    public int getStatus() {
        return status;
    }

    public Error.Code getErrorCode() {
        return errorCode;
    }
}
