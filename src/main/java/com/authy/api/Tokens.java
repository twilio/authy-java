package com.authy.api;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Map;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Julian Camargo
 *
 */
public class Tokens extends Resource {
	public static final String TOKEN_VERIFICATION_PATH = "/protected/xml/verify/";
	public static final String VALID_TOKEN_MESSAGE = "is valid";
	
	public Tokens(String uri, String key) {
		super(uri, key);
	}

	public Tokens(String uri, String key, boolean testFlag) {
		super(uri, key, testFlag);
	}
	
	public Token verify(int userId, String token) {
		return verify(userId, token, null);
	}

	public Token verify(int userId, String token, Map<String, String> options) {
		InternalToken internalToken = new InternalToken();
		internalToken.setOption(options);
		
		StringBuffer path = new StringBuffer(TOKEN_VERIFICATION_PATH);
		
		try {
			path.append(URLEncoder.encode(token, ENCODE) + '/');
			path.append(URLEncoder.encode(Integer.toString(userId), ENCODE));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		String content = this.get(path.toString(), internalToken);
		
		return tokenFromXml(this.getStatus(), content);
	}
	
	private Token tokenFromXml(int status, String content) {
		Token token = new Token();
		try {
			Error error = errorFromXml(status, content);
			
			if(error != null) {
				token.setError(error);
				return token;
			}
			
			JAXBContext context = JAXBContext.newInstance(Hash.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			StringReader xml = new StringReader(content);
			Hash hash = (Hash)unmarshaller.unmarshal(new StreamSource(xml));

			token = new Token(status, content);
			token.setValid(token.isOk() && hash.getToken().equals(VALID_TOKEN_MESSAGE));
		}
		catch(JAXBException e) {
			e.printStackTrace();
		}
		return token;
	}
	
	private Error errorFromXml(int status, String content) {
		Error error = new Error();
		
		try {
			JAXBContext context = JAXBContext.newInstance(Error.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			StringReader xml = new StringReader(content);
			error = (Error)unmarshaller.unmarshal(new StreamSource(xml));
		}
		catch(JAXBException e) {
			return null;
		}
		return error;
	}
	
	class InternalToken implements Response {
		Map<String, String> options;
		
		public InternalToken() {
			options = new HashMap<String, String>();
		}
		
		public void setOption(Map<String, String> options) {
			if(options != null)
				this.options = options;
		}
		
		public String toXML() {
			return null;
		}
		
		public Map<String, String> toMap() {
			if(!options.containsKey("force"))
				options.put("force", "true");
			
			return options;
		}
	}
}
