package com.foodiego.exception;

public class RatingLimitException extends RuntimeException {

	public RatingLimitException(String message) {
		super(message);
	}

}
