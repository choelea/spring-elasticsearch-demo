package com.joe.elasticserchdemo.utils;

import java.util.HashMap;

public class EsMappingProp  extends HashMap<String, Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6181836871221600691L;
	public EsMappingProp(String type){
		this.put("type", type);
	}
	public EsMappingProp parameter(String key, Object value){
		this.put(key, value);
		return this;
	}	
}
