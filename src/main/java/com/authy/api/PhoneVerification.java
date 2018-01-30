package com.authy.api;

import com.authy.AuthyException;

/**
 * @author Authy Inc
 */
public class PhoneVerification extends Resource {
    public static final String PHONE_VERIFICATION_API_PATH = "/protected/json/phones/verification/";

    public PhoneVerification(String uri, String key) {
        super(uri, key, Resource.JSON_CONTENT_TYPE);
    }

    public PhoneVerification(String uri, String key, boolean testFlag) {
        super(uri, key, testFlag, Resource.JSON_CONTENT_TYPE);
    }

    public Verification start(String phoneNumber, String countryCode, String via, Params params) throws AuthyException {
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        params.setAttribute("via", via);

        Verification verification = new Verification();
        StringBuilder path = new StringBuilder(PHONE_VERIFICATION_API_PATH);
        String response;
        path.append("start");
        response = this.post(path.toString(), params);

        verification.setStatus(this.getStatus());
        verification.setResponse(response);
        return verification;
    }

    public Verification check(String phoneNumber, String countryCode, String code) throws AuthyException {
        Params params = new Params();
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        params.setAttribute("verification_code", code);

        return verificationCheck(params);
    }

    public Verification check(String phoneNumber, String countryCode, String code, Params params) throws AuthyException {
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        params.setAttribute("verification_code", code);

        return verificationCheck(params);
    }

    private Verification verificationCheck(Params params) throws AuthyException {
        Verification verification = new Verification();
        StringBuilder path = new StringBuilder(PHONE_VERIFICATION_API_PATH);
        String response;

        path.append("check");
        response = this.get(path.toString(), params);

        verification.setStatus(this.getStatus());
        verification.setResponse(response);
        return verification;
    }

}
