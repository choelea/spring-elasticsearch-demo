package com.joe.elasticsearchdemo.dto;

import java.io.Serializable;


public class BucketData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8837185832164979987L;
	
	private String key;
	private String lable;
	private Long count;

	public BucketData(String key, String lable, Long docCount) {
		super();
		this.key = key;
		this.lable = lable;
		this.count = docCount;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}


}