package com.rviewer.skeletons.service.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewSafeboxReqDTO {
	@NotBlank(message="non empty name required")
	private String name;
	@NotBlank(message="non empty password required")
	private String password;
}
