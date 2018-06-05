package com.authy.api;

import com.authy.AuthyException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hansospina
 * <p>
 * Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class OneTouchResponse extends Instance {

    private JSONObject obj;


    public OneTouchResponse(int status, String content) throws AuthyException {
        super(status, content);
        try {
            obj = new JSONObject(content);
        } catch (JSONException ex) {
            throw new AuthyException("Invalid JSON format, the given string is not a valid json object.", ex);
        }
    }

    public OneTouchResponse(String json) throws AuthyException {
        this(200, json);
    }

    public boolean isSuccess() {
        return obj.has("success") && obj.getBoolean("success");
    }

    public String getMessage() {
        return obj.has("message") ? obj.getString("message") : "";
    }

    public String getErrorCode() {
        return obj.has("error_code") ? obj.getString("error_code") : "";
    }


    public ApprovalRequest getApprovalRequest() {

        if (obj.has("approval_request")) {
            return new ApprovalRequest();
        }

        return null;
    }

    public class ApprovalRequest {

        private ApprovalRequest() {
        }

        public boolean isNotified() {
            return obj.getJSONObject("approval_request").has("notified") && obj.getJSONObject("approval_request").getBoolean("notified");
        }

        public String createdAt() {
            return obj.getJSONObject("approval_request").has("created_at") ? obj.getJSONObject("approval_request").getString("created_at") : null;
        }

        public String getUUID() {
            return obj.getJSONObject("approval_request").has("uuid") ? obj.getJSONObject("approval_request").getString("uuid") : null;
        }

        public String getStatus() {
            return obj.getJSONObject("approval_request").has("status") ? obj.getJSONObject("approval_request").getString("status") : null;
        }

        // if the user was a value that is not mapped previously
        public String getValue(String key) {
            return obj.getJSONObject("approval_request").has(key) ? obj.getJSONObject("approval_request").getString(key) : null;
        }
    }


}
