package com.csk.search.place.dto;

import java.util.List;

import com.csk.search.common.domain.Response;

import lombok.Data;

@Data
public class PlaceResponse implements Response {
	private MetaDTO meta;
	private List<PlaceDTO> places;
}