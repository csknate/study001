package com.csk.search.place.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="KeywordScore", uniqueConstraints = {@UniqueConstraint(columnNames={"keyword"})}, indexes = {@Index(columnList="score")})
public class KeywordScore {
	@Id
	@Column(columnDefinition = "varchar(50)", unique = true, nullable = false)
	private String keyword;
	@Column(columnDefinition = "int(11)")
	private Integer score;
}
