package com.authy.api;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * Generic class to instance a response from the API
 * @author Julian Camargo
 *
 */

public class Instance {
	int status;
	String content;
	Error error;
	
	public Instance() {
		content = "";
	}
	
	public Instance(int status, String content) {
		this.status = status;
		this.content = content;
	}
	
	/**
	 * Check if this is instance is correct. (i.e No error occurred)
	 * @return true if no error occurred else false.
	 */
	public boolean isOk() {
		return status == 200;
	}
	
	/**
	 * Return an Error object with the error that have occurred or null.
	 * @return an Error object
	 */
	public Error getError() {
		if(isOk())
			return error;
		
		try {
			JAXBContext context = JAXBContext.newInstance(Error.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			StringReader xml = new StringReader(content);
			if(!content.isEmpty())
				error = (Error)unmarshaller.unmarshal(new StreamSource(xml));
		}
		catch(JAXBException e) {
			e.printStackTrace();
		}
		
		return error;
	}
	
	/**
	 * Set an Error object.
	 * @param error
	 */
	public void setError(Error error) {
		this.error = error;
	}
}

