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

        StringBuilder path = new StringBuilder(PHONE_VERIFICATION_API_PATH);
        path.append("start");
        final String response = this.post(path.toString(), params);

        Verification verification = new Verification(this.getStatus(), response);
        if (!verification.isOk())
            verification.setError(errorFromJson(this.getStatus(), response));

        return verification;
    }

    public Verification check(String phoneNumber, String countryCode, String code) throws AuthyException {
        return check(phoneNumber,countryCode, code, new Params());
    }

    public Verification check(String phoneNumber, String countryCode, String code, Params params) throws AuthyException {
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        params.setAttribute("verification_code", code);

        StringBuilder path = new StringBuilder(PHONE_VERIFICATION_API_PATH);

        path.append("check");
        final String response = this.get(path.toString(), params);

        Verification verification = new Verification(this.getStatus(), response);
        if (!verification.isOk())
            verification.setError(errorFromJson(this.getStatus(), response));
        return verification;

    }

}
