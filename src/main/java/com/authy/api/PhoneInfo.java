package com.authy.api;

import com.authy.AuthyException;

/**
 * @author Moisés Vargas
 */
public class PhoneInfo extends Resource {
    public static final String PHONE_INFO_API_PATH = "/protected/json/phones/";

    public PhoneInfo(String uri, String key) {
        super(uri, key, Resource.JSON_CONTENT_TYPE);
    }

    public PhoneInfo(String uri, String key, boolean testFlag) {
        super(uri, key, testFlag, Resource.JSON_CONTENT_TYPE);
    }

    public PhoneInfoResponse info(String phoneNumber, String countryCode) throws AuthyException {
        return info(phoneNumber, countryCode, new Params());
    }

    public PhoneInfoResponse info(String phoneNumber, String countryCode, Params params) throws AuthyException {
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        StringBuilder path = new StringBuilder(PHONE_INFO_API_PATH);
        path.append("info");
        String response = this.get(path.toString(), params);
        PhoneInfoResponse info = new PhoneInfoResponse(this.getStatus(), response);
        if (!info.isOk()) {
            info.setError(errorFromJson(getStatus(), response));
        }
        return info;
    }

}
