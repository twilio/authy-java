package com.authy.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "user_status")
public class UserStatus extends Instance implements Formattable {

    @XmlElement(name = "userId")
    private int userId;
    @XmlElement(name = "success")
    private boolean success;
    @XmlElement(name = "confirmed")
    private boolean confirmed;
    @XmlElement(name = "registered")
    private boolean registered;
    @XmlElement(name = "country_code")
    private int countryCode;
    @XmlElementWrapper
    @XmlElement(name = "device")
    private List<String> devices;
    @XmlElement(name = "phone_number")
    private String phoneNumber;

    public UserStatus() {
        super();
    }

    public UserStatus(int status, String content) {
        super(status, content);
    }

    public UserStatus(int status, String content, String message) {
        super(status, content, message);
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("userId", Integer.toString(getUserId()));
        map.put("success", Boolean.toString(getSuccess()));
        map.put("confirmed", Boolean.toString(isConfirmed()));
        map.put("registered", Boolean.toString(isRegistered()));
        map.put("countryCode", Integer.toString(getCountryCode()));
        map.put("phoneNumber", getPhoneNumber());
        map.put("devices", getDevices().toString());

        return map;
    }

    public int getUserId() {
        return userId;
    }

    void setUserId(int userId) {
        this.userId = userId;
    }

    void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isRegistered() {
        return registered;
    }

    void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public int getCountryCode() {
        return countryCode;
    }

    void setCountryCode(int countryCode) {
        this.countryCode = countryCode;
    }

    public List<String> getDevices() {
        if (devices == null) {
            devices = new ArrayList<>();
        }
        return devices;
    }

    void setDevices(List<String> devices) {
        this.devices = devices;
    }

    void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
