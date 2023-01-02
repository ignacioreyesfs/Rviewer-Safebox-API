package com.rviewer.skeletons.validation;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AllowedResource {
	public boolean allowedResourceById(Authentication auth, String id) {
		return auth.getName().equals(id);
	}
}
