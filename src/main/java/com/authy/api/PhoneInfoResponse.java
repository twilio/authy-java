package com.authy.api;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Moisés Vargas
 */

public class PhoneInfoResponse extends Instance implements Formattable {
    private String message = "Something went wrong!";
    private String provider = "";
    private String type = "";
    private boolean isPorted = false;

    public PhoneInfoResponse() {
    }

    public PhoneInfoResponse(int status, String response) {
        this(status, response, null);
    }

    public PhoneInfoResponse(int status, String response, String message) {
        this.status = status;
        this.content = response;
        this.message = message;
        this.setResponse(response);
    }

    public String getMessage() {
        return message;
    }

    public String getProvider() {
        return provider;
    }

    public String getType() {
        return type;
    }

    public String getSuccess() {
        return Boolean.toString(this.isOk());
    }

    public String getIsPorted() {
        return Boolean.toString(this.isPorted);
    }

    public void setResponse(String response) {
        this.content = response;
        JSONObject jsonResponse = new JSONObject(response);
        this.parseResponseToObject(jsonResponse);
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("message", this.getMessage());
        map.put("success", this.getSuccess());
        map.put("is_ported", this.getIsPorted());
        map.put("provider", this.getProvider());
        map.put("type", this.getType());


        return map;
    }

    private void parseResponseToObject(JSONObject json) {
        if (!json.isNull("message")) {
            this.message = json.getString("message");
        }

        if (!json.isNull("ported")) {
            this.isPorted = json.getBoolean("ported");
        }

        if (!json.isNull("provider")) {
            this.provider = json.getString("provider");
        }

        if (!json.isNull("type")) {
            this.type = json.getString("type");
        }
    }
}
