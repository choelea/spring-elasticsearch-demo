package com.joe.elasticserchdemo.document;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class StoreDoc implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1523814535189878438L;
	public static final String INDEX_NAME="store";
	public static final String INDEX_TYPE="_doc";
	public StoreDoc() {
	} // mandatory for Json Mapping

	public StoreDoc(Long id, String name, String mainProducts, String type, Boolean isSelfRun, Date updated) {
		super();
		this.id = id;
		this.name = name;
		this.mainProducts = mainProducts;
	}
	private Long id;
	private String name;
	public static final String _name = "name";

	private String mainProducts;
	public static final String _mainProducts = "mainProducts";

	private String fullText;
	public static final String _fullText = "fullText";
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMainProducts() {
		return mainProducts;
	}

	public void setMainProducts(String mainProducts) {
		this.mainProducts = mainProducts;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}

	@Override
	public String toString() {
		return "StoreDoc [id=" + id + ", name=" + name + ", mainProducts=" + mainProducts + "]";
	}	
	
	@JsonIgnore
	public static Map<String, Object> getMapping(){
		Map<String, Object> jsonMap = new HashMap<>();
				
		Map<String, Object> properties = new HashMap<>();
		properties.put(_name, Collections.singletonMap("type", "text"));
		properties.put(_mainProducts, Collections.singletonMap("type", "text"));
		properties.put(_fullText, Collections.singletonMap("type", "text"));
		jsonMap.put("properties", properties);
		return jsonMap;
	}
}
