package com.rviewer.skeletons.utils.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.java.Log;

@Component
@PropertySources({@PropertySource("classpath:secret.properties"),
	@PropertySource("classpath:application.properties")})
@Log
public class TokenManager {
	@Value("${token.validity.millis}")
	private String tokenValidity;
	@Value("${secret.jwt}")
	private String jwtSecret;
	
	public String generateJwtToken(UserDetails userDetails) {
		log.info("validity: " + tokenValidity);
		log.info("secret: " + jwtSecret);
		Map<String, Object> claims = new HashMap<>();
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenValidity)))
				.signWith(getSigningKey())
				.compact();
	}
	
	private Key getSigningKey() {
		byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public boolean validateJwtToken(String token, UserDetails userDetails) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
		String username = claims.getSubject();
		Boolean isTokenExpired = claims.getExpiration().before(new Date());
		return username.equals(userDetails.getUsername()) && !isTokenExpired;
	}
	
	public String getSubject(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey())
				.build().parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
}
