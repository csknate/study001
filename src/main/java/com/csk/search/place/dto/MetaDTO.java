package com.csk.search.place.dto;

import com.csk.search.common.exception.CommonException;
import com.csk.search.common.exception.Error;

import lombok.Data;

@Data
public class MetaDTO {
	private Integer totalPageNo;
	private Integer total;
	private Integer pageNo;
	private Integer pageSize;
	
	public void calculateTotalPageNo() throws CommonException {
		if( total != null && pageSize != null ) {
			totalPageNo = total/pageSize + (total%pageSize > 0 ? 1 : 0);
		} else {
			throw new CommonException(Error.FAIL_GET_TOTALPAGENUMBER);
		}
	}
}
