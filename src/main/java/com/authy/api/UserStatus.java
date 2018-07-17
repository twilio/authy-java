package com.authy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserStatus extends Instance implements Formattable {

    private int userId;
    private boolean success;
    private boolean confirmed;
    private boolean registered;
    private int countryCode;
    private List<String> devices;
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
        return null;
    }

    @Override
    public String toJSON() {
        return null;
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
