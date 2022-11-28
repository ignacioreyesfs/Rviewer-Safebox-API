package com.rviewer.skeletons.utils.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.service.InvalidCredentialsException;
import com.rviewer.skeletons.service.SafeboxService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Component
@AllArgsConstructor
@Log
public class JwtFilter extends OncePerRequestFilter{
	private SafeboxService safeboxService;
	private TokenManager tokenManager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String tokenHeader = request.getHeader("Authorization");
		String safeboxId = null;
		String token = null;
		
		log.info("Estoy aca");
		
		if(tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			token = tokenHeader.substring(7);
			try {
				safeboxId = tokenManager.getSubject(token);
			}catch(IllegalArgumentException e) {
				logger.info("Unable to get JWT Token");
				throw new InvalidCredentialsException(APIError.INVALID_TOKEN);
			}catch(ExpiredJwtException e) {
				logger.info(String.format("JWT Token expired: %s", token));
				throw new InvalidCredentialsException(APIError.TOKEN_EXPIRED);
			}
			
			if(safeboxId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails safeboxDetails = safeboxService.loadUserByUsername(safeboxId);
				if(tokenManager.validateJwtToken(token, safeboxDetails)) {
					UsernamePasswordAuthenticationToken authToken = 
							new UsernamePasswordAuthenticationToken(safeboxDetails, null, safeboxDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		
		filterChain.doFilter(request, response);
	}

}
