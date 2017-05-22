package com.authy.api;

import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
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
    private String message, token;
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
     * Map a Token instance to its XML representation.
     *
     * @return a String with the description of this object in XML.
     */
    public String toXML() {
        Error error = getError();

        if (error != null) {
            return error.toXML();
        }

        StringWriter sw = new StringWriter();
        String xml = "";

        try {
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(this, sw);
            xml = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    // required to satisfy Formattable interface
    // required to satisfy Formattable interface
    public String toJSON() {
        return new JSONObject(toMap()).toString();
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
