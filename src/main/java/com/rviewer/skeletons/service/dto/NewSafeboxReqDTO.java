package com.rviewer.skeletons.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewSafeboxReqDTO {
	private String name;
	private String password;
}
