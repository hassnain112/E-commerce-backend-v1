package com.example.demo.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String>handleUserNotFound(UserNotFoundException ex){
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}
	@ExceptionHandler(DuplicateException.class)
	public ResponseEntity<String>HandleDuplicateException(DuplicateException ex){
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ex.getMessage());
	}

	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<String>handleProductNotFound(ProductNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			
				.body(ex.getMessage());
	}
	@ExceptionHandler(OrderNotFoundException.class)
	
	public ResponseEntity<String>handleOrderNotFound(OrderNotFoundException ex){
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(ex.getMessage());
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	
	public ResponseEntity<ErrorResponse>handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
		 ex.getBindingResult().getFieldErrors()
         .forEach(error ->
                 System.out.println(error.getField() + " : " + error.getDefaultMessage())); 
		return ResponseEntity.badRequest().body(
				  new ErrorResponse(
				 400,
				 "Validation failed",
				 LocalDateTime.now()
				 ));
	}
	

}
