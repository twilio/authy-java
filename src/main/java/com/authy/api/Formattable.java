package com.authy.api;

import java.util.Map;

/**
 * Interface to represent objects as XML or Java's Map
 * @author Authy Inc
 *
 */
public interface Formattable {
  public String toXML();
  public Map<String, String> toMap();
  public String toJSON();
}
