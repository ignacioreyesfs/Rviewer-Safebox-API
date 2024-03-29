package com.rviewer.skeletons.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.exception.AppException;
import com.rviewer.skeletons.exception.ErrorDTO;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(AppException.class)
	public ResponseEntity<ErrorDTO> handleAppException(AppException e, WebRequest request){
		return ResponseEntity.status(e.getHttpStatus())
				.body(new ErrorDTO(e.getDescription(), e.getReasons()));
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> reasons = new ArrayList<>();
		APIError apiError = APIError.VALIDATION_ERROR; 

		for(FieldError error: ex.getBindingResult().getFieldErrors()) {
			reasons.add(String.format("%s - %s", error.getField(), error.getDefaultMessage()));
		}
		
		return ResponseEntity.status(apiError.getHttpStatus())
				.body(new ErrorDTO(apiError.getMessage(), reasons));
	}
}
