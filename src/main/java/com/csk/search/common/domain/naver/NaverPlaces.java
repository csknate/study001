package com.csk.search.common.domain.naver;

import java.util.List;

import lombok.Data;

@Data
public class NaverPlaces {
	private String lastBuildDate;
	private Integer total;
	private Integer start;
	private Integer display;
	private List<Place> items;
	
	private String errorMessage;
	private String errorCode;
}
