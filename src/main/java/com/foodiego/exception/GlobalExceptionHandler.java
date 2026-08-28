package com.foodiego.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.foodiego.dto.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ResponseStructure<String>> HandleDuplicateEmailException(DuplicateEmailException exception){
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(exception.getMessage());
		res.setData("FAILURE");
		return new ResponseEntity<ResponseStructure<String>>(res, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler
	public ResponseEntity<ResponseStructure<String>> HandleDuplicateContactException(DuplicateContactException exception){
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(exception.getMessage());
		res.setData("FAILURE");
		return new ResponseEntity<ResponseStructure<String>>(res, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler
	public ResponseEntity<ResponseStructure<String>> HandleNoRecordException(NoRecordException exception){
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.NO_CONTENT.value());
		res.setMessage(exception.getMessage());
		res.setData("FAILURE");
		return new ResponseEntity<ResponseStructure<String>>(res, HttpStatus.NO_CONTENT);
	}
	@ExceptionHandler
	public ResponseEntity<ResponseStructure<String>> HandleCustomerNotFounfException(CustomerNotFoundException exception){
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(exception.getMessage());
		res.setData("FAILURE");
		return new ResponseEntity<ResponseStructure<String>>(res, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(CannotDeleteException.class)
	public ResponseEntity<ResponseStructure<String>> CannotDeleteException(CannotDeleteException exception){
		ResponseStructure<String> res = new ResponseStructure<String>();
		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage(exception.getMessage());
		res.setData("FAILURE");
		return new ResponseEntity<ResponseStructure<String>>(res, HttpStatus.NOT_FOUND);
	}
	
	
}
