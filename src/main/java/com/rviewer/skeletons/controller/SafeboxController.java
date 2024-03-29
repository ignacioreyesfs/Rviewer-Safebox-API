package com.rviewer.skeletons.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.service.InvalidCredentialsException;
import com.rviewer.skeletons.service.SafeboxService;
import com.rviewer.skeletons.service.dto.JwtToken;
import com.rviewer.skeletons.service.dto.NewSafeboxReqDTO;
import com.rviewer.skeletons.service.dto.NewSafeboxResponseDTO;
import com.rviewer.skeletons.service.dto.OpenRequestDTO;
import com.rviewer.skeletons.service.dto.SafeboxItems;
import com.rviewer.skeletons.utils.jwt.TokenManager;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/safebox")
@AllArgsConstructor
@Log
public class SafeboxController {
	private SafeboxService safeboxService;
	private AuthenticationManager authManager;
	private TokenManager tokenManager;
	
	@PostMapping
	public NewSafeboxResponseDTO create(@Valid @RequestBody NewSafeboxReqDTO safebox) {
		return safeboxService.create(safebox);
	}
	
	@PostMapping("{id}/open")
	public ResponseEntity<JwtToken> open(@NotBlank @PathVariable String id, 
			@Valid @RequestBody OpenRequestDTO openReq) {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(id,
							openReq.getPassword()));
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new InvalidCredentialsException(APIError.INVALID_CREDENTIALS);
		}
		
		UserDetails safeboxDetails = safeboxService.loadUserByUsername(id);
		String jwtToken = tokenManager.generateJwtToken(safeboxDetails);
		return ResponseEntity.ok(new JwtToken(jwtToken));
	}
	
	@GetMapping("{id}/items")
	@PreAuthorize("@allowedResource.allowedResourceById(authentication, #id)")
	public ResponseEntity<SafeboxItems> getItems(@NotBlank @PathVariable String id){
		return ResponseEntity.ok(safeboxService.getSafeboxItems(id));
	}
	
	@PutMapping("{id}/items")
	@PreAuthorize("@allowedResource.allowedResourceById(authentication, #id)")
	public void updateItems(@NotBlank @PathVariable String id, @RequestBody SafeboxItems content) {
		safeboxService.updateItems(id, content);
	}
	
	
}
