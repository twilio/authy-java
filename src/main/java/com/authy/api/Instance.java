package com.authy.api;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * Generic class to instance a response from the API
 *
 * @author Julian Camargo
 */

public class Instance {
    int status;
    String content;
    String message;
    Error error;

    public Instance() {
        content = "";
    }

    public Instance(int status, String content) {
        this.status = status;
        this.content = content;
    }

    public Instance(int status, String content, String message) {
        this.status = status;
        this.content = content;
        this.message = message;
    }

    /**
     * Check if this is instance is correct. (i.e No error occurred)
     *
     * @return true if no error occurred else false.
     */
    public boolean isOk() {
        return status == 200;
    }

    /**
     * Return an Error object with the error that have occurred or null.
     *
     * @return an Error object
     */
    public Error getError() {
            return error;
    }

    /**
     * Set an Error object.
     */
    public void setError(Error error) {
        this.error = error;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Map a Token instance to its XML representation.
     *
     * @return a String with the description of this object in XML.
     */
    public String toXML() {
        Error error = getError();

        if (error != null) {
            return error.toXML();
        }

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
}

