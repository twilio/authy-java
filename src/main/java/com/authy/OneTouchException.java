package com.authy;

/**
 * @author hansospina
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class OneTouchException extends AuthyException {

    public OneTouchException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public OneTouchException(String message) {
        super(message);
    }

}
