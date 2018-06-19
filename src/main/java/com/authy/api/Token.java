package com.authy.api;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Julian Camargo
 */
@XmlRootElement(name = "token")
public class Token extends Instance implements Formattable {

    public static final String VALID_TOKEN_MESSAGE = "Token is valid.";

    public Token() {
    }

    public Token(int status, String content){
        super(status, content);
    }

    public Token(int status, String content, String message) {
        super(status, content, message);
    }

    /**
     * Check if this is token is correct. (i.e No error occurred)
     *
     * @return true if no error occurred else false.
     */
    public boolean isOk() {
        if (super.isOk()) {
            return this.message.equals(VALID_TOKEN_MESSAGE);
        }
        return false;
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("status", Integer.toString(status));
        map.put("content", content);

        return map;
    }
}
