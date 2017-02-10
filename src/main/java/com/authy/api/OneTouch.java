package com.authy.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hansospina
 */
public class OneTouch extends Resource {

    public static final String APPROVAL_REQUEST_PRE = "/onetouch/json/users/";
    public static final String APPROVAL_REQUEST_POS = "/approval_requests";
    public static final String APPROVAL_REQUEST_STATUS = "/onetouch/json/approval_requests/";
    private static final int MAXSIZE=201;

    public OneTouch(String uri, String key) {
        super(uri, key, Resource.JSON_CONTENT_TYPE);

    }

    public OneTouch(String uri, String key, boolean testFlag) {
        super(uri, key, testFlag, Resource.JSON_CONTENT_TYPE);
    }

    
 
    /**
     * 
     * @param userId User's authy id stored in your database
     * @param message the message shown to the user when the approval request arrives.
     * @param options  details, hidden_details and logos
     * @param seconds_to_expire Optional, defaults to 120 (two minutes).
     * @return 
     */    
    public OneTouchResponse sendApprovalRequest(Integer userId, String message, HashMap<String, Object> options, Integer seconds_to_expire) {

        if(userId==null)
            return new OneTouchResponse(false, "user_id is missing.");

        if (message==null || message.isEmpty())
            return new OneTouchResponse("false, Message is missing.");
        message = validateMaxSize(message);
        
        if (options==null || options.isEmpty())
            return new OneTouchResponse(false, "Sender's account details are missing.");
        
        if (!options.containsKey("details"))
            return new OneTouchResponse(false,"Sender's account details are missing.");
        
        if (!options.containsKey("hidden_details"))
            return new OneTouchResponse(false, "Hidden details can't blank.");
        
        if (!(options.get("details") instanceof Map) )
            return new OneTouchResponse(false, "HashMap expected on key details.");
        
        HashMap<String,String> details = (HashMap)options.get("details");
        if (details.isEmpty() )
            return new OneTouchResponse(false, "Sender's account details are missing.");
        
        
        if (!(options.get("hidden_details") instanceof Map) )
            return new OneTouchResponse(false, "HashMap expected on key hidden_details.");
        HashMap<String,String> hiddenDetails = (HashMap)options.get("hidden_details");

        if (hiddenDetails==null || hiddenDetails.isEmpty())
            return new OneTouchResponse(false, "Hidden details can't blank.");
        
        JSONObject params = new JSONObject();
        
        List<Logo> logos  = new ArrayList<>();
        if(options.containsKey("logos")){
            if(!(options.get("logos") instanceof List))
                return new OneTouchResponse(false,"List expected on key logos.");
            logos=(List)options.get("logos");
            JSONArray jSONArray = new JSONArray();
                for (Object logo: logos) {
                    if(!(logo instanceof Logo))
                       return new OneTouchResponse(false,"class Logo expected on list logos.");
                    ((Logo)logo).addToMap(jSONArray);
                }
            params.put("logos", jSONArray);
        }
        
        params.put("message",message);
        
        JSONObject detailsJ=new JSONObject();
        for(String key: details.keySet()){
            detailsJ.put(validateMaxSize(key),validateMaxSize(details.get(key)));
        }
        params.put("details", detailsJ);

        JSONObject hDetailsJ=new JSONObject();
        for(String key: hiddenDetails.keySet()){
            hDetailsJ.put(validateMaxSize(key),validateMaxSize(hiddenDetails.get(key)));
        }
        params.put("hidden_details", hDetailsJ);
        
        
        


        return new OneTouchResponse(this.post(APPROVAL_REQUEST_PRE+userId+APPROVAL_REQUEST_POS, new JSONBody(params)));
    }  
    
    private String validateMaxSize(String data){
        return data.substring(0, Math.min(data.length(), MAXSIZE));
    }

    public OneTouchResponse getApprovalRequestStatus(String uuid) {

        try {
            return new OneTouchResponse(this.get(APPROVAL_REQUEST_STATUS + URLEncoder.encode(uuid, ENCODE), new Params()));
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
