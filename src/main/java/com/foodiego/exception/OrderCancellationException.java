package com.foodiego.exception;

public class OrderCancellationException extends RuntimeException{

	public OrderCancellationException(String msg) {
		super(msg);
	}

}
