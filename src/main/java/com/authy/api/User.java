package com.authy.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Julian Camargo
 */
@XmlRootElement(name = "user")
public class User extends Instance implements Formattable {
    int id;

    public User() {
    }

    public User(int status, String content) {
        super(status, content);
    }

    public User(int status, String content, String message) {
        super(status, content, message);
    }

    @XmlElement(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("id", Integer.toString(id));
        map.put("status", Integer.toString(status));
        map.put("content", content);

        return map;
    }
}
