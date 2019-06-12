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

        final Response response = this.post(PHONE_VERIFICATION_API_PATH + "start", params);

        return createVerification(response);
    }

    private Verification createVerification(Response response) throws AuthyException {
        Verification verification = new Verification(response.getStatus(), response.getBody());
        if (!verification.isOk())
            verification.setError(errorFromJson(response.getBody()));

        return verification;
    }

    public Verification check(String phoneNumber, String countryCode, String code) throws AuthyException {
        return check(phoneNumber,countryCode, code, new Params());
    }

    public Verification check(String phoneNumber, String countryCode, String code, Params params) throws AuthyException {
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        params.setAttribute("verification_code", code);

        final Response response = this.get(PHONE_VERIFICATION_API_PATH + "check", params);

        return createVerification(response);

    }

    public Verification status(String phoneNumber, String countryCode) throws AuthyException {
        Params params = new Params();
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        return status(params);
    }

    public Verification status(String uuid) throws AuthyException {
        Params params = new Params();
        params.setAttribute("uuid", uuid);
        return status(params);
    }

    private Verification status(Params params) throws AuthyException {
        final Response response = this.get(PHONE_VERIFICATION_API_PATH + "status", params);

        return createVerification(response);
    }

}
