package com.authy.api;

import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;

/**
 * 
 * @author Julian Camargo
 *
 */
@XmlRootElement(name="hash")
public class Hash extends Instance implements Response {

	private User user = null;
	private String message, token;
	private boolean success;
	
	public Hash() {
	}
	
	public Hash(int status, String content) {
		super(status, content);
	}
	
	@XmlElement(type=User.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * Map a Token instance to its XML representation.
	 * @return a String with the description of this object in XML.
	 */
	public String toXML() {
		Error error = getError();
		
		if(error != null) {
			return error.toXML();
		}
		
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

	/**
	 * Map a Token instance to its Java's Map representation.
	 * @return a Java's Map with the description of this object.
	 */
	public Map<String, String> toMap() {
		return null;
	}
}
