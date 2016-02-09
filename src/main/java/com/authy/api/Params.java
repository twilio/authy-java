package com.authy.api;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author Authy Inc
 *
 */

public class Params extends Request {
  @JsonIgnore
  private Map<String, String> additionalProperties = new HashMap<String, String>();

  public Params() {
  }

  public void setAttribute(String key, String value){
    this.additionalProperties.put(key, value);
  }


  public Map<String, String> toMap() {
    return this.additionalProperties;
  }

  @JsonAnyGetter
  public Map<String, String> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, String value) {
    this.additionalProperties.put(name, value);
  }

  @Override
  public Serialization preferredSerialization() {
    return Serialization.JSON;
  }
}
