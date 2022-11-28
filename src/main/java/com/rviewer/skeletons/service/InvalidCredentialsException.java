package com.rviewer.skeletons.service;

import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.exception.AppException;

public class InvalidCredentialsException extends AppException {
	public InvalidCredentialsException(APIError error) {
		super(error.getHttpStatus(), error.getMessage(), null);
	}
}
