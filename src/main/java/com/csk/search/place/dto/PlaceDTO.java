package com.csk.search.place.dto;

import lombok.Data;

@Data
public class PlaceDTO {
	private String title;
	private String[] imageUrls;
	
	public PlaceDTO(String title) {
		this.title = title;
	}
}