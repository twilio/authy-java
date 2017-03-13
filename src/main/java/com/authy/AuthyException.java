package com.authy;

/**
 * @author Julian Camargo
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class AuthyException extends Exception {
    public AuthyException(String message) {
        super(message);
    }

    public AuthyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
