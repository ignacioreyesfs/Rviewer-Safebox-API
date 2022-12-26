package com.rviewer.skeletons.utils.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.checkerframework.framework.qual.SubtypeOf;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rviewer.skeletons.exception.ErrorDTO;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
	private ObjectMapper jsonMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String auth = request.getHeader("Authorization");
		ErrorDTO error;
		if(auth != null && auth.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			error = new ErrorDTO("You don't have permission to access this resource", null);
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			error = new ErrorDTO("You aren't authenticated", null);	
		}
		
		response.getWriter().write(jsonMapper.writeValueAsString(error));
		response.flushBuffer();
	}

}
