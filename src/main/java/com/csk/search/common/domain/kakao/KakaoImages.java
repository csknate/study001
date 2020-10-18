package com.csk.search.common.domain.kakao;

import java.util.List;

import lombok.Data;

@Data
public class KakaoImages {

	private ImagesMeta meta;
	private List<ImagesDocument> documents;
	
	private String errorType;
	private String message;
}
