package com.authy.api;

import org.json.JSONObject;

import java.util.Date;

/**
 * @author hansospina
 */
public class OneTouchResponse {

    private JSONObject obj;


    public OneTouchResponse(String json) {

        if (json == null) {
            json = "{}";
        }

        obj = new JSONObject(json);
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

    class ApprovalRequest {

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
    }


}
