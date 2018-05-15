package com.authy.api;

import org.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moisés Vargas
 */
@XmlRootElement(name = "verification")
public class Verification implements Formattable {
    private int status = 503;
    private String response;
    private String message = "Something went wrong!";
    private boolean isPorted = false;
    private boolean isCellphone = false;

    public Verification() {
    }

    public Verification(int status, String response, String message) {
        this.status = status;
        this.response = response;
        this.message = message;
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
        this.response = response;
        JSONObject jsonResponse = new JSONObject(response);
        this.parseResponseToOjbect(jsonResponse);
    }

    public boolean isOk() {
        return status == 200;
    }

    /**
     * Map a Token instance to its XML representation.
     *
     * @return a String with the description of this object in XML.
     */
    public String toXML() {
        StringWriter sw = new StringWriter();
        String xml = "";

        try {
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(this, sw);
            xml = sw.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xml;
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
