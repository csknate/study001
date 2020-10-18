package com.csk.search.place.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csk.search.common.client.KakaoRestClient;
import com.csk.search.common.client.NaverRestClient;
import com.csk.search.common.domain.kakao.KakaoImages;
import com.csk.search.common.domain.kakao.KakaoPlaces;
import com.csk.search.common.domain.naver.NaverImages;
import com.csk.search.common.domain.naver.NaverPlaces;
import com.csk.search.common.exception.CommonException;
import com.csk.search.common.exception.Error;
import com.csk.search.place.dao.KeywordScoreRepository;
import com.csk.search.place.dto.KeywordScore;
import com.csk.search.place.dto.MetaDTO;
import com.csk.search.place.dto.PlaceDTO;
import com.csk.search.place.dto.PlaceResponse;
import com.csk.search.place.dto.RankResponse;

import kong.unirest.HttpResponse;

@Service
public class PlaceService {
	private static final Log logger = LogFactory.getLog(PlaceService.class);
	
	private final static int PAGE_SIZE = 3;
	private final static int ASYNC_TIMEOUT = 300;
	
	@Autowired
	private KakaoRestClient kakaoClient;
	@Autowired
	private NaverRestClient naverClient;
	
	@Autowired
	KeywordScoreRepository keywordScoreRepo;
	
	public PlaceResponse getPlace(String keyword, int page) throws CommonException {
		PlaceResponse response = new PlaceResponse();
		
		try {
			increaseScoreByKeyword(keyword);
		}catch (RuntimeException e) {
			logger.warn("increase keyword fail.", e);
		}
		
		CompletableFuture<HttpResponse<KakaoPlaces>> kakaoPlaceFuture = kakaoClient.getPlaces(keyword, page, PAGE_SIZE);
		
		try {
			mergePlaces(response,kakaoPlaceFuture.get(ASYNC_TIMEOUT, TimeUnit.MILLISECONDS));
			response.getMeta().setPageNo(page);
			logger.info("kakao place api success.");
		} catch (InterruptedException | ExecutionException | TimeoutException | CommonException e) {
			logger.warn("kakao place api fail.", e);
			CompletableFuture<HttpResponse<NaverPlaces>> naverPlaceFuture = naverClient.getPlaces(keyword, PAGE_SIZE);
			try {
				mergePlaces(response,naverPlaceFuture.get(ASYNC_TIMEOUT, TimeUnit.MILLISECONDS));
				response.getMeta().setPageNo(1); //naver는 검색시작위치 1로 고정
				logger.info("naver place api success.");
			} catch (InterruptedException | ExecutionException | TimeoutException e1) {
				logger.warn("naver place api fail.", e);
				throw new CommonException(Error.FAIL_GET_PLACE);
			}
		}

		mergeImages(response);
		
		return response;
	}
	
	private void increaseScoreByKeyword(String keyword) {
		if( keywordScoreRepo.exists(keyword) ) {
			keywordScoreRepo.updateScore(1, keyword);
		} else {
			try {
				keywordScoreRepo.save(new KeywordScore(keyword,1));
			} catch(RuntimeException e) {
				logger.warn("increase keyword fail.(retry)", e);
				//최초 insert 요청이 중복되는 경우 첫번 째가 아닌 insert는 update로 재시도
				keywordScoreRepo.updateScore(1, keyword);
			}
		}
	}
	
	private void mergeImages(PlaceResponse response) throws CommonException {
		response.getPlaces()
		.parallelStream()
		.map(place->{
			Object placeArr[] = new Object[2]; 
			placeArr[0] = place;
			placeArr[1] = kakaoClient.getImages(place.getTitle(), 1, PAGE_SIZE);
			return placeArr;
		})
		.collect(Collectors.toList()) //place별 imageFuture들
		.parallelStream()
		.forEach(placeArr->{ //place별 imageFuture에서 결과를 받아서 place에 set~!
			PlaceDTO placeDTO = (PlaceDTO)placeArr[0];
			CompletableFuture<HttpResponse<KakaoImages>> kakaoImageFuture = (CompletableFuture<HttpResponse<KakaoImages>>)placeArr[1];  
			
			try {
				HttpResponse<KakaoImages> resKakaoImages = kakaoImageFuture.get(ASYNC_TIMEOUT, TimeUnit.MILLISECONDS);
				
				if( resKakaoImages != null && resKakaoImages.isSuccess() ) {
					KakaoImages kakaoImages = resKakaoImages.getBody();
					placeDTO.setImageUrls(kakaoImages.getDocuments()
													.stream()
													.map(doc->doc.getImage_url())
													.toArray(String[]::new));
				}
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				logger.warn("kakao image api fail.", e);
				CompletableFuture<HttpResponse<NaverImages>> naverImageFuture = naverClient.getImages(placeDTO.getTitle(), 1, PAGE_SIZE);
				
				try {
					HttpResponse<NaverImages> resNaverImages = naverImageFuture.get(ASYNC_TIMEOUT, TimeUnit.MILLISECONDS);
					
					if( resNaverImages != null && resNaverImages.isSuccess() ) {
						NaverImages naverImages = resNaverImages.getBody();
						placeDTO.setImageUrls(naverImages.getItems()
														.stream()
														.map(doc->doc.getLink())
														.toArray(String[]::new));
					}
				} catch(Exception e1) {
					logger.warn("naver image api fail.", e);
				}
			}
		});
	}
	
	private void mergePlaces(PlaceResponse response, HttpResponse<?> resPlaces) throws CommonException {
		MetaDTO meta = new MetaDTO();
		List<PlaceDTO> places = null;

		if( resPlaces != null && resPlaces.isSuccess() ) {
			if( resPlaces.getBody().getClass() == KakaoPlaces.class ) {
				KakaoPlaces kPlace = (KakaoPlaces)resPlaces.getBody();
				meta.setTotal(kPlace.getMeta().getTotal_count());
				meta.setPageSize(PAGE_SIZE);
				meta.calculateTotalPageNo();
				places = kPlace.getDocuments()
								.stream()
								.map(x -> new PlaceDTO(x.getPlace_name()))
								.collect(Collectors.toList());
				
			} else if( resPlaces.getBody().getClass() == NaverPlaces.class ) {
				NaverPlaces nPlace = (NaverPlaces)resPlaces.getBody();
				meta.setTotal(nPlace.getTotal());
				meta.setPageSize(PAGE_SIZE);
				meta.calculateTotalPageNo();
				places = nPlace.getItems()
						.stream()
						.map(x -> new PlaceDTO(x.getTitle()))
						.collect(Collectors.toList());
			}
		} else {
			throw new CommonException(Error.FAIL_GET_PLACE);
		}
		response.setMeta(meta);
		response.setPlaces(places);
	}

	public RankResponse getKeywordRank10() {
		RankResponse response = new RankResponse();
		response.setRanking(keywordScoreRepo.getKeywordRank10());
		return response;
	}
}
