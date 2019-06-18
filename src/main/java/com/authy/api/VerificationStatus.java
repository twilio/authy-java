package com.authy.api;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;

/**
 * @author Trina Castillo
 */
@XmlRootElement(name = "verificationStatus")
public class VerificationStatus extends Instance implements Formattable {
    private String verificationStatus;
    private Integer secondsToExpire;

    public VerificationStatus() {
    }

    public VerificationStatus(int status, String response) {
        this(status, response, null);
    }

    public VerificationStatus(int status, String response, String message) {
        super(status, response, message);
        this.setResponse(response);
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    @XmlElement(name = "success")
    public String getSuccess() {
        return Boolean.toString(this.isOk());
    }

    @XmlElement(name = "status")
    public String getVerificationStatus() {
        return verificationStatus;
    }

    @XmlElement(name = "seconds_to_expire")
    public Integer getSecondsToExpire() {
        return secondsToExpire;
    }

    public void setStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public void setResponse(String response) {
        this.content = response;
        JSONObject jsonResponse = new JSONObject(response);
        this.parseResponseToObject(jsonResponse);
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("message", this.getMessage());
        map.put("status", this.getVerificationStatus());
        map.put("seconds_to_expire", String.valueOf(this.getSecondsToExpire()));

        return map;
    }

    private void parseResponseToObject(JSONObject json) {
        if (!json.isNull("message")) {
            this.message = json.getString("message");
        }

        if (!json.isNull("status")) {
            this.verificationStatus = json.getString("status");
        }

        if (!json.isNull("seconds_to_expire")) {
            this.secondsToExpire = json.getInt("seconds_to_expire");
        }
    }
}
