package com.thz.house_paint.utils.exceptions;

public class BusinessRuleViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public BusinessRuleViolationException(String message) {
		super(message);
	}
	
	public BusinessRuleViolationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public BusinessRuleViolationException(Throwable cause) {
		super(cause);
	}
}
