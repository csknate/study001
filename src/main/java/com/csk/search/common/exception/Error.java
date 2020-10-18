package com.csk.search.common.exception;

import org.springframework.http.HttpStatus;

public enum Error {
	//common
	UNKNOWN_ERROR("10000", "Unknown Exception.", HttpStatus.INTERNAL_SERVER_ERROR),
	
	//place
	FAIL_GET_PAGENUMBER("10101", "Can't get the total page number.", HttpStatus.INTERNAL_SERVER_ERROR),
	FAIL_GET_PLACE("10102", "Can't get place.", HttpStatus.INTERNAL_SERVER_ERROR)	
	;
	
	private String code;
	private String message;
	private HttpStatus status;
	
	private Error(String code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public HttpStatus getHttpStatus() {
		return this.status;
	}
}
