package com.codechef.cah01.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "codechef_user")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private UserId userId;

	@JsonProperty("country")
	private String country;

	@JsonIgnore
	private List<String> contestCode;

	@JsonProperty("totalScore")
	private Double score;

	@JsonProperty("noOfContest")
	private Integer noOfContest;

	public User() {
		super();
	}

	public User(UserId userId, String country, List<String> contestCode, Double score, Integer noOfContest) {
		super();
		this.userId = userId;
		this.country = country;
		this.contestCode = contestCode;
		this.score = score;
		this.noOfContest = noOfContest;
	}

	public Integer getNoOfContest() {
		return noOfContest;
	}

	public void setNoOfContest(Integer noOfContest) {
		this.noOfContest = noOfContest;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getContestCode() {
		return contestCode;
	}

	public void setContestCode(List<String> contestCode) {
		this.contestCode = contestCode;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(UserId userId) {
		this.userId = userId;
	}

}
