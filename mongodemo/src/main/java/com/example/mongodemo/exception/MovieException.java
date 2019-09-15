package com.example.mongodemo.exception;

public class MovieException {
	
	private String errorMessage;
	private String errorInfo;

	public MovieException(String errorMessage, String errorInfo) {
		super();
		this.errorMessage = errorMessage;
		this.errorInfo = errorInfo;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
