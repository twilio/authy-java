package com.authy.api;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

public abstract class Request {
    public static enum Serialization {
        XML("application/xml"),
        JSON("application/json"),
        QueryString("application/x-www-form-urlencoded");

        private final String contentType;

        private Serialization(String contentType){
            this.contentType = contentType;
        }

        public String getContentType() {
            return contentType;
        }
    }

    public String toXML() {
        StringWriter sw = new StringWriter();
        String xml = "";

        try {
            JAXBContext context = JAXBContext.newInstance(this.getClass());
            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(this, sw);

            xml = sw.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return xml;
    }

    public String toJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        objectMapper.registerModule(module);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toQueryString() {
        ObjectMapper objectMapper = new ObjectMapper();
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        objectMapper.registerModule(module);

        StringBuilder qs = new StringBuilder();
        objToQueryString(objectMapper.valueToTree(this), qs, null);

        if(qs.length() > 0 && qs.charAt(0) == '&') {
            qs.deleteCharAt(0);
        }

        return qs.toString();
    }

    private static void nodeToQueryString(JsonNode node, StringBuilder qs, String context) {
        switch(node.getNodeType()){
            case OBJECT:
                objToQueryString(node, qs, context);
                break;
            case ARRAY:
                arrayToQueryString(node, qs, context);
                break;
            case NULL:
                break;
            case BOOLEAN:
            case NUMBER:
            case STRING:
                try {
                    qs.append("&")
                    .append(URLEncoder.encode(context, "UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(node.asText(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    // Impossible, UTF-8 is always supported
                    throw new RuntimeException(e);
                }
                break;
            default:
                throw new RuntimeException("Unsupported json nodeType: " + node.getNodeType());
        }
    }

    private static void objToQueryString(JsonNode obj, StringBuilder qs, String context) {
        Iterator<Map.Entry<String,JsonNode>> fields = obj.fields();
        while(fields.hasNext()) {
            Map.Entry<String,JsonNode> field = fields.next();
            nodeToQueryString(field.getValue(), qs, context != null ? context + "[" + field.getKey() + "]" : field.getKey());
        }
    }

    private static void arrayToQueryString(JsonNode arr, StringBuilder qs, String context) {
        for(JsonNode node : arr) {
            nodeToQueryString(node, qs, context != null ? context + "[]" : "[]");
        }
    }

    public abstract Serialization preferredSerialization();

    public String preferredSerializedContent() {
        switch(preferredSerialization()) {
            case JSON:
                return toJSON();
            case QueryString:
                return toQueryString();
            case XML:
                return toXML();
        }
        return null;
    }
}
