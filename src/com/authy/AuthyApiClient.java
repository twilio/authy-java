package com.authy;

import com.authy.api.*;

/**
 * 
 * @author Julian Camargo
 *
 */
public class AuthyApiClient {
	private Users users;
	private Tokens tokens;
	private String apiUri, apiKey;
	
	public static final String DEFAULT_API_URI = "https://api.authy.com";
	
	public AuthyApiClient(String apiKey) {
		this(apiKey, DEFAULT_API_URI, false);
	}
	
	public AuthyApiClient(String apiKey, String apiUri) {
		this(apiKey, apiUri, false);
	}
	
	public AuthyApiClient(String apiKey, String apiUri, boolean testFlag) {
		this.apiUri = apiUri;
		this.apiKey = apiKey;
		
		this.users = new Users(this.apiUri, this.apiKey, testFlag);
		this.tokens = new Tokens(this.apiUri, this.apiKey, testFlag);
	}
	
	public Users getUsers() {
		return this.users;
	}
	
	public Tokens getTokens() {
		return this.tokens;
	}
}
