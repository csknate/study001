package com.csk.search.common.domain.kakao;

import lombok.Data;

@Data
public class PlacesName {
	private String[] region;
	private String keyword;
	private String selected_region;
}
