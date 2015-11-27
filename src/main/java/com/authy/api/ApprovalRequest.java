package com.authy.api;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;

@XmlRootElement(name="approval_request")
public class ApprovalRequest extends Request {
    private String message;
    private Map<String, String> details;
    private Map<String, String> hiddenDetails;
    private Integer secondsToExpire;

    public ApprovalRequest() {
    }

    public ApprovalRequest(
        String message,
        Map<String, String> details,
        Map<String, String> hiddenDetails,
        Integer secondsToExpire) {
        this.message = message;
        this.details = details;
        this.hiddenDetails = hiddenDetails;
        this.secondsToExpire = secondsToExpire;
    }

    @XmlElement
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement
    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    // Need both of these because the name wasn't getting changed with just XmlElement, apparently we
    // need XmlElementWrapper for Maps, that changed it for xml serialization, but json serialization
    // still needed XmlElement. See: http://blog.bdoughan.com/2013/03/jaxb-and-javautilmap.html
    @XmlElementWrapper(name="hidden_details")
    @XmlElement(name="hidden_details")
    public Map<String, String> getHiddenDetails() {
        return hiddenDetails;
    }

    public void setHiddenDetails(Map<String, String> hiddenDetails) {
        this.hiddenDetails = hiddenDetails;
    }

    @XmlElement(name="seconds_to_expire")
    public Integer getSecondsToExpire() {
        return secondsToExpire;
    }

    public void setSecondsToExpire(Integer secondsToExpire) {
        this.secondsToExpire = secondsToExpire;
    }

    @Override
    public Serialization preferredSerialization() {
        return Request.Serialization.QueryString;
    }
}
