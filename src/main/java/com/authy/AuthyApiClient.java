package com.authy;

import com.authy.api.*;

/**
 * @author Julian Camargo
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class AuthyApiClient {
    public static final String CLIENT_NAME = "AuthyJava";
    public static final String DEFAULT_API_URI = "https://api.authy.com";
    public static final String VERSION = "1.2.0";
    private Users users;
    private Tokens tokens;
    private String apiUri, apiKey;
    private PhoneVerification phoneVerification;
    private PhoneInfo phoneInfo;
    private OneTouch oneTouch;


    public AuthyApiClient(String apiKey, String apiUri) {
        init(apiKey, apiUri, false);
    }

    public AuthyApiClient(String apiKey) {
        init(apiKey, DEFAULT_API_URI, false);
    }

    public AuthyApiClient(String apiKey, String apiUri, boolean testFlag) {
        init(apiKey, apiUri, testFlag);
    }

    private void init(String apiKey, String apiUrl, boolean testFlag) {
        this.apiUri = apiUrl;
        this.apiKey = apiKey;

        this.phoneInfo = new PhoneInfo(this.apiUri, this.apiKey, testFlag);
        this.phoneVerification = new PhoneVerification(this.apiUri, this.apiKey, testFlag);
        this.users = new Users(this.apiUri, this.apiKey, testFlag);
        this.tokens = new Tokens(this.apiUri, this.apiKey, testFlag);
        this.oneTouch = new OneTouch(this.apiUri, this.apiKey, testFlag);
    }

    public Users getUsers() {
        return this.users;
    }

    public Tokens getTokens() {
        return this.tokens;
    }

    public PhoneVerification getPhoneVerification() {
        return this.phoneVerification;
    }

    public PhoneInfo getPhoneInfo() {
        return this.phoneInfo;
    }

    public OneTouch getOneTouch() {
        return oneTouch;
    }
}
