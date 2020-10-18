package com.csk.search.common;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.csk.search.common.client.NaverRestClient;
import com.csk.search.common.domain.naver.NaverImages;
import com.csk.search.common.domain.naver.NaverPlaces;

import kong.unirest.HttpResponse;
import kong.unirest.HttpStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NaverRestClientTest {

    @Autowired
    private NaverRestClient client;

    @Test
    public void testPlaceClient() {
    	CompletableFuture<HttpResponse<NaverPlaces>> future = client.getPlaces("이순신", 3);
    	
    	try {
    		HttpResponse<NaverPlaces> response = future.get();
    		if( response.getStatus() == HttpStatus.OK ) {
    			NaverPlaces places = response.getBody();
    			Assert.assertNull(places.getErrorCode());
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
    	CompletableFuture<HttpResponse<NaverImages>> future = client.getImages("오리", 1, 3);
    	
    	try {
    		HttpResponse<NaverImages> response = future.get();
    		if( response.getStatus() == HttpStatus.OK ) {
    			NaverImages images = response.getBody();
    			Assert.assertNull(images.getErrorCode());
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
    	CompletableFuture<HttpResponse<NaverPlaces>> future = client.getPlaces("", 3);
    	
    	try {
    		HttpResponse<NaverPlaces> response = future.get();
    		if( response.getStatus() != HttpStatus.OK ) {
    			NaverPlaces places = response.getBody();
    			Assert.assertNotNull(places.getErrorCode());
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
    	CompletableFuture<HttpResponse<NaverImages>> future = client.getImages("", 1, 3);
    	
    	try {
    		HttpResponse<NaverImages> response = future.get();
    		if( response.getStatus() != HttpStatus.OK ) {
    			NaverImages images = response.getBody();
    			Assert.assertNotNull(images.getErrorCode());
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

