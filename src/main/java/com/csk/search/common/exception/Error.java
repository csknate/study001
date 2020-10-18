package com.csk.search.common.exception;

import org.springframework.http.HttpStatus;

public enum Error {
	//common
	UNKNOWN_ERROR("10000", "Unknown Exception.", HttpStatus.INTERNAL_SERVER_ERROR),
	
	//place
	FAIL_GET_TOTALPAGENUMBER("10101", "Failed to get total page number.", HttpStatus.INTERNAL_SERVER_ERROR),
	FAIL_GET_PLACE("10102", "Failed to get place information.", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_PAGENUMBER("10103", "Invalid page number.", HttpStatus.BAD_REQUEST)
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
