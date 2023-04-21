package com.Capstone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class CapExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntityExistsException.class)
	protected ResponseEntity<String> manage_EntityExistsException(EntityExistsException e){
		return new ResponseEntity<String>("CapExceptionHandler " + e.getMessage(), HttpStatus.FOUND);
	}
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<String> manage_EntityNotFoundException(EntityNotFoundException e){
		return new ResponseEntity<String>("CapExceptionHandler " + e.getMessage(), HttpStatus.NOT_FOUND);
	}
}
