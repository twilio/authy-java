/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.authy.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author hansospina
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.
 */
public class Logo {


    private final int MAX = 201;
    private String res;
    private String url;


    public Logo(ApprovalRequestParams.Resolution res, String url) {
        this.res = res.getRes();
        if (url == null)
            this.url = "";
        else
            this.url = url.substring(0, Math.min(url.length(), MAX));
    }

    public String getRes() {
        return res;
    }

    public void setRes(ApprovalRequestParams.Resolution res) {
        this.res = res.getRes();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null)
            this.url = "";
        else
            this.url = url.substring(0, Math.min(url.length(), MAX));
    }

    public void addToMap(JSONArray map) {
        if (!getUrl().isEmpty()) {
            JSONObject temp = new JSONObject();
            temp.put("res", getRes());
            temp.put("url", getUrl());
            map.put(temp);
        }
    }


}
