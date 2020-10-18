package com.csk.search.place.controller;

import com.csk.search.place.dao.KeywordScoreRepository;
import com.csk.search.place.dto.KeywordScore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class PlaceControllerV1Test {

	@Autowired
    MockMvc mvc;
	
	@MockBean
	KeywordScoreRepository keywordScoreRepo;
	
    @Test
    public void searchPlace() throws Exception {
    	String keyword = "맛집";
    	
    	given(keywordScoreRepo.updateScore(1, keyword)).willReturn(1);
    	
    	mvc.perform(MockMvcRequestBuilders.get("/v1/place?keyword=맛집&pageNo=1")
    			.accept(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.meta.pageNo").value(1))
    	.andExpect(MockMvcResultMatchers.jsonPath("$.places").isNotEmpty());
    }
    
    @Test
    public void rank() throws Exception {
    	List<KeywordScore> datas = new ArrayList<KeywordScore>();
    	datas.add(new KeywordScore("삼겹살",10));
    	datas.add(new KeywordScore("곱창",8));
    	datas.add(new KeywordScore("피자",4));
    	datas.add(new KeywordScore("맥주",1));
    	
    	given(keywordScoreRepo.getKeywordRank10()).willReturn(datas);
    	
    	mvc.perform(MockMvcRequestBuilders.get("/v1/place/rank")
    			.accept(MediaType.APPLICATION_JSON_UTF8))
    	.andExpect(MockMvcResultMatchers.status().isOk())
    	.andExpect(MockMvcResultMatchers.jsonPath("$.ranking").isNotEmpty());
    }
}