package com.csk.search.place.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.csk.search.place.dto.KeywordScore;

public interface KeywordScoreRepository extends JpaRepository<KeywordScore,String> {
	@Transactional
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE KeywordScore SET score = score + ?1 WHERE keyword = ?2")
	Integer updateScore(Integer score, String keyword);
	
	@Query(value="SELECT keyword, score FROM Keyword_Score ORDER BY score DESC LIMIT 0,10", nativeQuery = true)
	List<KeywordScore> getKeywordRank10();
}
