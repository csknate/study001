package com.csk.search.common.exception;

import lombok.Getter;

public class CommonException extends Exception {
	@Getter
	private Error error;
	
	public CommonException(Error error) {
		this.error = error;
	}	
}
