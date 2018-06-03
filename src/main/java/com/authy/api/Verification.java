package com.authy.api;

import org.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moisés Vargas
 */
@XmlRootElement(name = "verification")
public class Verification extends Instance implements Formattable {
    private boolean isPorted = false;
    private boolean isCellphone = false;

    public Verification() {
    }

    public Verification(int status, String response) {
        this(status, response, null);
    }

    public Verification(int status, String response, String message) {
        super(status, response, message);
        this.setResponse(response);
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    @XmlElement(name = "success")
    public String getSuccess() {
        return Boolean.toString(this.isOk());
    }

    @XmlElement(name = "is_ported")
    public String getIsPorted() {
        return Boolean.toString(this.isPorted);
    }

    @XmlElement(name = "is_cellphone")
    public String getIsCellphone() {
        return Boolean.toString(this.isCellphone);
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setResponse(String response) {
        this.content = response;
        JSONObject jsonResponse = new JSONObject(response);
        this.parseResponseToOjbect(jsonResponse);
    }

    /**
     * Map a Token instance to its Java's Map representation.
     *
     * @return a Java's Map with the description of this object.
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();

        map.put("message", this.getMessage());
        map.put("success", this.getSuccess());
        map.put("is_ported", this.getIsPorted());
        map.put("is_cellphone", this.getIsCellphone());

        return map;
    }

    private void parseResponseToOjbect(JSONObject json) {
        if (!json.isNull("message"))
            this.message = json.getString("message");

        if (!json.isNull("is_ported"))
            this.isPorted = json.getBoolean("is_ported");

        if (!json.isNull("is_cellphone"))
            this.isCellphone = json.getBoolean("is_cellphone");
    }
}
