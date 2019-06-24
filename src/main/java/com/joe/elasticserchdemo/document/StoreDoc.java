package com.joe.elasticserchdemo.document;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joe.elasticserchdemo.utils.EsMappingProp;

public class StoreDoc extends EsDoc implements Serializable {

	public static final String TOKENIZER_PATTERN = "[^\\w^-]";
	
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
	
	private String storeType;
	public static final String _storeType = "storeType";
	
	private int rating;
	public static final String _rating="rating";
	
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

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}	

	

	@Override
	public String toString() {
		return "StoreDoc [id=" + id + ", name=" + name + ", mainProducts=" + mainProducts + ", fullText=" + fullText
				+ ", storeType=" + storeType + ", rating=" + rating + "]";
	}

	@JsonIgnore
	public static Map<String, Object> getMapping(){
		Map<String, Object> jsonMap = new HashMap<>();
				
		Map<String, Object> properties = new HashMap<>();
		properties.put(_name, new EsMappingProp("text").parameter("analyzer", "chem_analyzer_en"));
		properties.put(_mainProducts, new EsMappingProp("text").parameter("analyzer", "chem_analyzer_en"));
		properties.put(_fullText, Collections.singletonMap("type", "text"));
		properties.put(_rating, new EsMappingProp("integer"));
		properties.put(_storeType, new EsMappingProp("keyword"));
		jsonMap.put("properties", properties);
		return jsonMap;
	}
}
