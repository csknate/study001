package com.csk.search.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.csk.search.common.client.KakaoRestClient;
import com.csk.search.common.domain.kakao.KakaoImages;
import com.csk.search.common.domain.kakao.KakaoPlaces;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KakaoRestClientTest {

    @Autowired
    private KakaoRestClient client;

    @Test
    public void testPlaceClient() {
    	CompletableFuture<HttpResponse<KakaoPlaces>> future = client.getPlaces("이순신", 1, 3);
    	
    	try {
    		HttpResponse<KakaoPlaces> response = future.get();
    		if( response.getStatus() == HttpStatus.OK ) {
    			KakaoPlaces places = response.getBody();
    			Assert.assertNull(places.getErrorType());
    		} else {
    			Assert.fail();
    		}
		} catch (InterruptedException e) {
			Assert.fail();
		} catch (ExecutionException e) {
			Assert.fail();
		}
    }
    
    @Test
    public void testImageClient() {
    	CompletableFuture<HttpResponse<KakaoImages>> future = client.getImages("오리", 1, 3);
    	
    	try {
    		HttpResponse<KakaoImages> response = future.get();
    		if( response.getStatus() == HttpStatus.OK ) {
    			KakaoImages images = response.getBody();
    			Assert.assertNull(images.getErrorType());
    		} else {
    			Assert.fail();
    		}
		} catch (InterruptedException e) {
			Assert.fail();
		} catch (ExecutionException e) {
			Assert.fail();
		}
    }
    
    @Test
    public void testPlaceClient_fail() {
    	CompletableFuture<HttpResponse<KakaoPlaces>> future = client.getPlaces("", 1, 3);
    	
    	try {
    		HttpResponse<KakaoPlaces> response = future.get();
    		if( response.getStatus() != HttpStatus.OK ) {
    			KakaoPlaces places = response.getBody();
    			Assert.assertNotNull(places.getErrorType());
    		} else {
    			Assert.fail();
    		}
		} catch (InterruptedException e) {
			Assert.fail();
		} catch (ExecutionException e) {
			Assert.fail();
		}
    }
    
    @Test
    public void testImageClient_fail() {
    	CompletableFuture<HttpResponse<KakaoImages>> future = client.getImages("", 1, 3);
    	
    	try {
    		HttpResponse<KakaoImages> response = future.get();
    		if( response.getStatus() != HttpStatus.OK ) {
    			KakaoImages images = response.getBody();
    			Assert.assertNotNull(images.getErrorType());
    		} else {
    			Assert.fail();
    		}
		} catch (InterruptedException e) {
			Assert.fail();
		} catch (ExecutionException e) {
			Assert.fail();
		}
    }
}

