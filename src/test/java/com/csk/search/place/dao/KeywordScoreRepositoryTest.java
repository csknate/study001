package com.csk.search.place.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
	public void updateScore() {
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
	public void insert() {
		String keyword = "미용실";
		KeywordScore orgData = new KeywordScore(keyword,1);

		keywordScoreRepo.save(orgData);
		
		KeywordScore afterData = keywordScoreRepo.getOne(keyword);

		assertEquals(keyword,afterData.getKeyword());
		assertEquals(orgData.getScore(),afterData.getScore());
	}
}
