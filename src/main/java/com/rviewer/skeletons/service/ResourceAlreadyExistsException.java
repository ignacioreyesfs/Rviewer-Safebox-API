package com.rviewer.skeletons.service;

import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.exception.AppException;

public class ResourceAlreadyExistsException extends AppException {
	
	public ResourceAlreadyExistsException(APIError error) {
		super(error.getHttpStatus(), error.getMessage(), null);
	}
	
}
