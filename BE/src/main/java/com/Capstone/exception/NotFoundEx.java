package com.Capstone.exception;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundEx extends Exception {

	 public NotFoundEx(String message) {
		    super(message);
		  }
}
