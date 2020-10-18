package com.csk.search.common.exception;

import org.springframework.http.HttpStatus;

import com.csk.search.common.domain.Response;

import lombok.Data;

@Data
public class ErrorResponse implements Response {
	private String errorMessage;
	private String errorCode;
	
	public ErrorResponse() {}
	public ErrorResponse(CommonException ex) {
		this.errorMessage = ex.getError().getMessage();
		this.errorCode = ex.getError().getCode();
	}
	
	public ErrorResponse(Error error) {
		this.errorMessage = error.getMessage();
		this.errorCode = error.getCode();
	}
	
	public ErrorResponse(HttpStatus status) {
		this.errorMessage = status.getReasonPhrase();
		this.errorCode = String.valueOf(status.value());
	}
}
