package com.authy.api;

import com.authy.OneTouchException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Map;

/**
 * @author hansospina
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class OneTouch extends Resource {

    public static final String APPROVAL_REQUEST_PRE = "/onetouch/json/users/";
    public static final String APPROVAL_REQUEST_POS = "/approval_requests";
    public static final String APPROVAL_REQUEST_STATUS = "/onetouch/json/approval_requests/";


    public OneTouch(String uri, String key) {
        super(uri, key, Resource.JSON_CONTENT_TYPE);

    }

    public OneTouch(String uri, String apiKey, boolean testFlag) {
        super(uri, apiKey, testFlag, Resource.JSON_CONTENT_TYPE);
    }


    /**
     * Sends the OneTouch's approval request to the Authy servers and returns the OneTouchResponse that comes back.
     *
     * @param approvalRequestParams The bean wrapping the user's Authy approval request built using the ApprovalRequest.Builder
     * @return The bean wrapping the response from Authy's service.
     */
    public OneTouchResponse sendApprovalRequest(ApprovalRequestParams approvalRequestParams) throws OneTouchException {//Integer userId, String message, HashMap<String, Object> options, Integer secondsToExpire) throws OneTouchException {


        JSONObject params = new JSONObject();
        params.put("message", approvalRequestParams.getMessage());


        if (approvalRequestParams.getSecondsToExpire() != null) {
            params.put("seconds_to_expire", approvalRequestParams.getSecondsToExpire());
        }

        if (approvalRequestParams.getDetails().size() > 0) {
            params.put("details", mapToJSONObject(approvalRequestParams.getDetails()));
        }


        if (approvalRequestParams.getHidden().size() > 0) {
            params.put("hidden_details", mapToJSONObject(approvalRequestParams.getDetails()));
        }


        if (!approvalRequestParams.getLogos().isEmpty()) {
            JSONArray jSONArray = new JSONArray();

            for (Logo logo : approvalRequestParams.getLogos()) {
                logo.addToMap(jSONArray);
            }

            params.put("logos", jSONArray);
        }


        return new OneTouchResponse(this.post(APPROVAL_REQUEST_PRE + approvalRequestParams.getAuthyId() + APPROVAL_REQUEST_POS, new JSONBody(params)));
    }

    public OneTouchResponse getApprovalRequestStatus(String uuid) throws OneTouchException {

        try {
            return new OneTouchResponse(this.get(APPROVAL_REQUEST_STATUS + URLEncoder.encode(uuid, ENCODE), new Params()));
        } catch (Exception e) {
            throw new OneTouchException("There was an error trying to process this request.", e);
        }

    }


    private JSONObject mapToJSONObject(Map<String, String> map) {

        JSONObject obj = new JSONObject();

        for (String key : map.keySet()) {
            obj.put(key, map.get(key));
        }

        return obj;
    }


}
