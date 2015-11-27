package com.authy.api;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Authy Inc
 *
 */

public class Params implements Formattable {
  private Map<String, String> data;

  public Params() {
    data = new HashMap<String, String>();
  }

  public void setAttribute(String key, String value){
    this.data.put(key, value);
  }

  public String toJSON(){
    org.json.JSONObject json = new org.json.JSONObject(this.data);
    for (Map.Entry<String, String> entry : this.data.entrySet()) {
      json.put(entry.getKey(), entry.getValue());
    }

    return json.toString();
  }

  // required to satisfy Formattable interface
  public String toXML() {
    return "";
  }

  public Map<String, String> toMap() {
    return this.data;
  }
}
