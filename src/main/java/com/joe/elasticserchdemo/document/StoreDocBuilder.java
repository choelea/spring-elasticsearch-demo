package com.joe.elasticserchdemo.document;

import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class StoreDocBuilder {
	private static ObjectMapper objectMapper = new ObjectMapper();
	private StoreDoc storeDoc;
	
	public StoreDocBuilder(Long id) {
		storeDoc = new StoreDoc();
		storeDoc.setId(id);
	}
	
	public StoreDocBuilder name(String name) {
		storeDoc.setName(name);
		return this;
	}
	public StoreDocBuilder mainProducts(String mainProducts) {
		storeDoc.setMainProducts(mainProducts);
		return this;
	}
	
	public StoreDocBuilder fullText() {
		storeDoc.setFullText(storeDoc.getName()+" "+storeDoc.getMainProducts());
		return this;
	}
	public IndexRequest buildIndex() {
		IndexRequest indexRequest = new IndexRequest(StoreDoc.INDEX_NAME, StoreDoc.INDEX_TYPE);
		indexRequest.id(storeDoc.getId().toString());
		indexRequest.source(objectMapper.convertValue(storeDoc, Map.class));
		return indexRequest;
	}
}
