package com.csk.search.common.domain.kakao;

import lombok.Data;

@Data
public class ImagesMeta {
	private Integer total_count;
	private Integer pageable_count;
	private Boolean is_end;
}
