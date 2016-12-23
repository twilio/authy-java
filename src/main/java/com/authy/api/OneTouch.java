package com.authy.api;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author hansospina
 */
public class OneTouch extends Resource{

    public static final String APPROVAL_REQUEST_PRE = "/onetouch/json/users/";
    public static final String APPROVAL_REQUEST_POS = "/approval_requests";
    public static final String APPROVAL_REQUEST_STATUS = "/onetouch/json/approval_requests/";

    public OneTouch(String uri, String key) {
        super(uri, key, Resource.JSON_CONTENT_TYPE);

    }

    public OneTouch(String uri, String key, boolean testFlag) {
        super(uri, key, testFlag, Resource.JSON_CONTENT_TYPE);
    }


    public OneTouchResponse sendApprovalRequest(Integer userId, String message, HashMap<String,String> details, HashMap<String,String> hiddenDetails, HashMap<String,String> logos, Integer seconds_to_expire){

        JSONObject params = new JSONObject();
        params.put("message",message);

        for(String key: details.keySet()){
            params.put("details["+key+"]",details.get(key));
        }

        for(String key: hiddenDetails.keySet()){
            params.put("hidden_details["+key+"]",hiddenDetails.get(key));
        }

        for(String key: logos.keySet()){
            params.put("logos["+key+"]",logos.get(key));
        }

        params.put("message",message);


        return new OneTouchResponse(this.post(APPROVAL_REQUEST_PRE+userId+APPROVAL_REQUEST_POS, new JSONBody(params)));
    }


    public OneTouchResponse getApprovalRequestStatus(String udid){

        try {
            return new OneTouchResponse(this.get(APPROVAL_REQUEST_STATUS+URLEncoder.encode(udid, ENCODE), new Params()));
        }catch(Exception e) {
            e.printStackTrace();
            JSONObject object = new JSONObject();
            object.put("success",false);
            object.put("message",e.getMessage());
            object.put("error_code",e.getClass().getName());
            return new OneTouchResponse("{\"success\":false, \"message\":\""+e.getMessage()+"\"}");
        }


    }



}
