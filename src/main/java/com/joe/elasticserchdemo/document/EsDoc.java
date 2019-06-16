package com.joe.elasticserchdemo.document;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class EsDoc {
	
	
	private float score;

	@JsonIgnore
	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}	
}
