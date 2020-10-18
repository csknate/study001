package com.csk.search.common.client;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.csk.search.common.domain.kakao.KakaoImages;
import com.csk.search.common.domain.kakao.KakaoPlaces;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class KakaoRestClient {
	
	@Value("${kakao.api.local.url}")
	private String localRestApi;
	@Value("${kakao.api.image.url}")
	private String imageRestApi;
	@Value("${kakao.api.authorization}")
	private String authorization;
	
	public CompletableFuture<HttpResponse<KakaoPlaces>> getPlaces(String keyword, Integer page, Integer size) {
		CompletableFuture<HttpResponse<KakaoPlaces>> places = Unirest.get(localRestApi)
																.header("Authorization", authorization)
																.queryString("query", keyword)
																.queryString("page", page)
																.queryString("size", size)
																.asObjectAsync(KakaoPlaces.class);
		return places;
	}
	
	public CompletableFuture<HttpResponse<KakaoImages>> getImages(String keyword, Integer page, Integer size) {
		CompletableFuture<HttpResponse<KakaoImages>> images = Unirest.get(imageRestApi)
																.header("Authorization", authorization)
																.queryString("query", keyword)
																.queryString("page", page)
																.queryString("size", size)
																.asObjectAsync(KakaoImages.class);
		return images;
	}
}
