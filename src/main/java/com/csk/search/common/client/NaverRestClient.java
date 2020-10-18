package com.csk.search.common.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.csk.search.common.domain.naver.NaverImages;
import com.csk.search.common.domain.naver.NaverPlaces;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class NaverRestClient {
	
	@Value("${naver.api.local.url}")
	private String localRestApi;
	@Value("${naver.api.image.url}")
	private String imageRestApi;
	@Value("${naver.api.x-naver-client-secret}")
	private String clientSecret;
	@Value("${naver.api.x-naver-client-id}")
	private String clientId;
	
	public CompletableFuture<HttpResponse<NaverPlaces>> getPlaces(String keyword, Integer size) {
		CompletableFuture<HttpResponse<NaverPlaces>> places = Unirest.get(localRestApi)
																.header("X-Naver-Client-Id", clientId)
																.header("X-Naver-Client-Secret", clientSecret)
																.queryString("query", keyword)
																.queryString("start", 1)
																.queryString("display", size)
																.asObjectAsync(NaverPlaces.class);
		return places;
	}
	
	public CompletableFuture<HttpResponse<NaverImages>> getImages(String keyword, Integer page, Integer size) {
		CompletableFuture<HttpResponse<NaverImages>> images = Unirest.get(imageRestApi)
																.header("X-Naver-Client-Id", clientId)
																.header("X-Naver-Client-Secret", clientSecret)
																.queryString("query", keyword)
																.queryString("page", page)
																.queryString("display", size)
																.queryString("filter", "medium")
																.asObjectAsync(NaverImages.class);
		return images;
	}
}
