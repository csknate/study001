package com.csk.search.common.domain.kakao;

import lombok.Data;

@Data
public class ImagesDocument {
	private String collection;
	private String thumbnail_url;
	private String image_url;
	private Integer width;
	private Integer height;
	private String display_sitename;
	private String doc_url;
	private String datetime;
}