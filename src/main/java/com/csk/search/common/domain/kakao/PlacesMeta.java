package com.csk.search.common.domain.kakao;

import lombok.Data;

@Data
public class PlacesMeta {
	private PlacesName same_name;
	private Integer pageable_count;
	private Integer total_count;
	private Boolean is_end;
}
