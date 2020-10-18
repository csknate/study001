package com.csk.search.place.dto;

import java.util.List;

import com.csk.search.common.domain.Response;

import lombok.Data;

@Data
public class RankResponse implements Response {
	private List<KeywordScore> ranking;
}
