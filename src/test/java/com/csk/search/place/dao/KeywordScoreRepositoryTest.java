package com.csk.search.place.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.csk.search.place.dto.KeywordScore;

@RunWith(SpringRunner.class)
@SpringBootTest( properties = { "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE" } )
public class KeywordScoreRepositoryTest {
	@Autowired
	KeywordScoreRepository keywordScoreRepo;
	
	@Transactional
	@Test
	public void update_score() {
		KeywordScore orgData = new KeywordScore("테스트",1);
		keywordScoreRepo.save(orgData);
		assertNotNull(keywordScoreRepo.updateScore(1, "테스트"));
		assertNotNull(keywordScoreRepo.updateScore(1, "테스트"));
		assertNotNull(keywordScoreRepo.updateScore(1, "테스트"));
		assertNotNull(keywordScoreRepo.updateScore(1, "테스트"));
		KeywordScore afterData = keywordScoreRepo.getOne("테스트");
		assertEquals(Integer.valueOf(5), afterData.getScore());
	}
	
	@Transactional
	@Test
	public void insert_select_score() {
		String keyword = "미용실";
		KeywordScore orgData = new KeywordScore(keyword,1);

		keywordScoreRepo.save(orgData);
		
		KeywordScore afterData = keywordScoreRepo.getOne(keyword);

		assertEquals(keyword,afterData.getKeyword());
		assertEquals(orgData.getScore(),afterData.getScore());
	}
	
	@Transactional
	@Test
	public void get_keyword_rank() {
		keywordScoreRepo.save(new KeywordScore("삼겹살",30));
		keywordScoreRepo.save(new KeywordScore("차돌",26));
		keywordScoreRepo.save(new KeywordScore("곱창",24));
		keywordScoreRepo.save(new KeywordScore("샐러드",21));
		keywordScoreRepo.save(new KeywordScore("양고기",15));
		keywordScoreRepo.save(new KeywordScore("편의점",12));
		keywordScoreRepo.save(new KeywordScore("자전거",9));
		keywordScoreRepo.save(new KeywordScore("자동차",8));
		keywordScoreRepo.save(new KeywordScore("서점",4));
		keywordScoreRepo.save(new KeywordScore("마트",2));
		keywordScoreRepo.save(new KeywordScore("컴퓨터",1));
		
		List<KeywordScore> afterData = keywordScoreRepo.getKeywordRank10();

		assertEquals(10, afterData.size());
		assertEquals(Integer.valueOf(30), afterData.get(0).getScore());
		assertEquals("삼겹살", afterData.get(0).getKeyword());
		assertEquals("자동차", afterData.get(7).getKeyword());
	}
}
