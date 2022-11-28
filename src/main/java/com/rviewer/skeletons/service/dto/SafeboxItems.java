package com.rviewer.skeletons.service.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SafeboxItems {
	List<String> items;
	
	public SafeboxItems() {
		items = new ArrayList<>();
	}
}
