package com.csk.search.common.exception;

import javax.validation.ConstraintViolationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonExceptionController extends ResponseEntityExceptionHandler {
	private static final Log logger = LogFactory.getLog(CommonExceptionController.class);
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse runtimeExceptionHandler(RuntimeException e, WebRequest request) {
		logger.error(request.getParameterMap(), e);
		return new ErrorResponse(Error.UNKNOWN_ERROR);
	}
	
	@ExceptionHandler(CommonException.class)
	public ResponseEntity commonExceptionHandler(CommonException e, WebRequest request) {
		logger.warn(e.getError().getMessage());
		return new ResponseEntity(new ErrorResponse(e), e.getError().getHttpStatus());
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse constraintViolationException(ConstraintViolationException e, WebRequest request) {
		logger.error(request.getParameterMap(), e);
		java.util.Set<javax.validation.ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
		java.util.Set<String> messages = new java.util.HashSet<>(constraintViolations.size());
		messages.addAll(constraintViolations.stream()
		        .map(constraintViolation -> String.format("%s value '%s' %s", constraintViolation.getPropertyPath(),
		                constraintViolation.getInvalidValue(), constraintViolation.getMessage()))
		        .collect(java.util.stream.Collectors.toList()));
		ErrorResponse response = new ErrorResponse();
		response.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		response.setErrorMessage(messages.toString());
		return response;
	}
	
	@Override
	protected ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity(new ErrorResponse(status), status);
	}
}
