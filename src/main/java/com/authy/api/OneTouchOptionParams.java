/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.authy.api;

import com.authy.api.Logo.Resolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author hansospina
 *
 * Copyright © 2016 Twilio, Inc. All Rights Reserved.
 */
public class OneTouchOptionParams {
        private HashMap<String, String> details = new HashMap<String, String>();
        private HashMap<String, String> hidden = new HashMap<String, String>();
        private List<Logo> logos = new ArrayList<>();
        private HashMap<String, Object> options=new HashMap<>();
        
        
        public void addLogo(Resolution res, String url){

            if(url!=null){
                logos.add(new Logo(res, url));
            }

        }
        
        public HashMap generateParams(){
            options.put("details", details);
            options.put("hidden_details", hidden);

            if(logos.size()>0){

                boolean hasDefault = false;

                for(Logo l:logos){
                    if(l.getRes().equals(Resolution.Default.getRes())){
                        hasDefault =true;
                    }
                }

                if(!hasDefault){
                    logos.add(new Logo(Resolution.Default, logos.get(0).getUrl()));
                }
            }

            options.put("logos", logos);
            return options;
        }
        
        public void addDetail(String key, String value){
            details.put(key, value);
        }
        
        public void addHiddenDetails(String Key, String value){
            hidden.put(Key, value);
        }
        
        
}
