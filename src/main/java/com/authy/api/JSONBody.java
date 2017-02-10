package com.authy.api;

import com.authy.AuthyUtil;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hansospina
 *
 * Copyright © 2016 Twilio, Inc. All Rights Reserved.
 */
public class JSONBody implements Formattable {

    private JSONObject obj;

    public JSONBody(JSONObject obj) {
        this.obj = obj != null ? obj : new JSONObject();
    }

    public String toXML() {
        return null;
    }

    public Map<String, String> toMap() {

        HashMap<String,String> map = new HashMap<>();
        AuthyUtil.extract("",obj, map);

        return map;
    }

    public String toJSON() {
        return obj.toString();
    }
}
