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
        Params params = new Params();
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        return getInfo(params);
    }

    public PhoneInfoResponse info(String phoneNumber, String countryCode, Params params) throws AuthyException {
        params.setAttribute("phone_number", phoneNumber);
        params.setAttribute("country_code", countryCode);
        return getInfo(params);
    }

    private PhoneInfoResponse getInfo(Params params) throws AuthyException {
        PhoneInfoResponse info = new PhoneInfoResponse();
        StringBuilder path = new StringBuilder(PHONE_INFO_API_PATH);
        String response;

        path.append("info");
        response = this.get(path.toString(), params);

        info.setStatus(this.getStatus());
        info.setResponse(response);
        return info;
    }

}
