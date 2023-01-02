package com.rviewer.skeletons.exception;

import org.springframework.http.HttpStatus;

public enum APIError {
	VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "There are attributes with wrong values"),
	SAFEBOX_NOT_FOUND(HttpStatus.NOT_FOUND, "Safebox not found"),
	INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid Token"),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token expired"),
	SAFEBOX_ALREADY_EXISTS(HttpStatus.CONFLICT, "Safebox already exists");
	
	private final HttpStatus httpStatus;
	private final String message;
	
	APIError(HttpStatus httpStatus, String message){
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public String getMessage() {
		return message;
	}
}
