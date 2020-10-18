package com.csk.search.common.domain.naver;

import java.util.List;

import lombok.Data;

@Data
public class NaverImages {

	private String lastBuildDate;
	private Integer total;
	private Integer start;
	private Integer display;
	private List<Image> items;
	
	private String errorMessage;
	private String errorCode;
}
