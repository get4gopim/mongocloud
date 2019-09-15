package com.example.mongodemo.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { DuplicateKeyException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String errorMessage = "Duplicate key entry error";
		MovieException exceptionResponse = new MovieException(errorMessage, ex.getMessage());
		return handleExceptionInternal(ex, exceptionResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
}
