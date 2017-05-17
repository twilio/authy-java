package com.authy.api;

import java.util.Map;

/**
 * Interface to represent objects as XML or Java's Map
 *
 * @author Authy Inc
 */
public interface Formattable {
    String toXML();

    Map<String, String> toMap();

    String toJSON();

}
