package com.authy.api;

import org.json.JSONObject;

import java.util.Map;

/**
 * Interface to represent objects as XML or Java's Map
 *
 * @author Authy Inc
 */
public interface Formattable {
    String toXML();

    Map<String, String> toMap();

    default String toJSON() {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : toMap().entrySet()) {
            json.put(entry.getKey(), entry.getValue());
        }

        return json.toString();
    }
}
