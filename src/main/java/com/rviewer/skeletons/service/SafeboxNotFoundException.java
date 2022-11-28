package com.rviewer.skeletons.service;

import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.exception.AppException;

public class SafeboxNotFoundException extends AppException{

	public SafeboxNotFoundException() {
		super(APIError.SAFEBOX_NOT_FOUND.getHttpStatus(), 
				APIError.SAFEBOX_NOT_FOUND.getMessage(), null);
	}
	
}
