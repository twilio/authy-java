package com.authy;

/**
 * 
 * @author Julian Camargo
 *
 */
public class AuthyApiException extends AuthyException {
	private static final long serialVersionUID = 1L;
	String status, uri, message;

	public AuthyApiException(String status, String uri, String message) {
		super(String.format("HTTP ERROR %s: %s \n %s", status, message, uri));
		this.uri = uri;
		this.status = status;
		this.message = message;
	}

	public AuthyApiException(String status, String uri) {
		super(String.format("HTTP ERROR %s: \n %s", status, uri));
		this.uri = uri;
		this.status = status;
	}
}
