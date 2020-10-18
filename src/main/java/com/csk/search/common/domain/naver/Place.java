package com.csk.search.common.domain.naver;

import lombok.Data;

@Data
public class Place {
	private String title;
	private String link;
	private String category;
	private String description;
	private String telephone;
	private String address;
	private String roadAddress;
	private String mapx;
	private String mapy;
}
