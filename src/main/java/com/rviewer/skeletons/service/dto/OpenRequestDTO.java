package com.rviewer.skeletons.service.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpenRequestDTO {
	@NotBlank
	private String password;
}
