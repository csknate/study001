package com.csk.search.common.domain.kakao;

import java.util.List;

import lombok.Data;

@Data
public class KakaoPlaces {
	private PlacesMeta meta;
	private PlacesName same_name;
	private List<PlacesDocument> documents;
	
	private String errorType;
	private String message;
}
