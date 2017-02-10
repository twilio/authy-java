/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.authy.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author hansospina
 *
 * Copyright © 2016 Twilio, Inc. All Rights Reserved.
 */
public class Logo {
    
    
    private final int MAX=201;
    private String res;
    private String url;

    
    public Logo(Resolution res, String url) {
        setRes(res);
        setUrl(url);
    }
    
    public String getRes() {
        return res;
    }

    public void setRes(Resolution res){
        this.res = res.getRes();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if(url==null)
            this.url="";
        else
            this.url  = url.substring(0, Math.min(url.length(), MAX));
    }
    
    public void addToMap(JSONArray map){
        if(!getUrl().isEmpty()){
            JSONObject temp= new JSONObject();
            temp.put("res", getRes());
            temp.put("url", getUrl());
            map.put(temp);
        }
    }
    

     public enum Resolution {
        Default("default"),
        Low("low"),
        Medium("med"),
        High("high");
        
        private  String res;
        Resolution(String res){
            this.res=res;
        }
        
        public String getRes(){return res;};
        
    }
    
    
    
}
