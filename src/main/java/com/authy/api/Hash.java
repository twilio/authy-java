package com.authy.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Julian Camargo
 *         <p>
 *         Copyright © 2017 Twilio, Inc. All Rights Reserved.

 */
@XmlRootElement(name = "hash")
public class Hash extends Instance implements Formattable {

    private User user = null;
    private String token;
    private boolean success;

    public Hash() {
    }

    public Hash(int status, String content) {
        super(status, content);
    }

    @XmlElement(type = User.class)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {

        HashMap<String,String> map = new HashMap<>();

        if( user != null ) {

            Map<String,String> userMap = user.toMap();

            for(String st : userMap.keySet() ){
                map.put("user."+st,userMap.get(st));
            }

        }

        map.put("message",message);
        map.put("token",token);
        map.put("success",String.valueOf(success));
        return map;
    }
}
