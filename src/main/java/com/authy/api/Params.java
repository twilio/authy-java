package com.authy.api;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Authy Inc
 */

public class Params implements Formattable {
    private Map<String, String> data;

    public Params() {
        data = new HashMap<>();
    }

    public void setAttribute(String key, String value) {
        this.data.put(key, value);
    }

    // required to satisfy Formattable interface
    public String toXML() {
        return "";
    }

    public Map<String, String> toMap() {
        return this.data;
    }
}
