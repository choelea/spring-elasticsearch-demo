package com.joe.elasticserchdemo.service;


import java.io.IOException;
import java.util.List;

import com.joe.elasticsearchdemo.dto.AggregatedStoreDocPage;
import com.joe.elasticsearchdemo.dto.Pageable;
import com.joe.elasticserchdemo.document.StoreDoc;

public interface StoreDocService {

	
	List<StoreDoc> findAll(int from, int size) throws IOException;

	List<StoreDoc> searchInName(String keyword, int from, int size) throws IOException;

	List<StoreDoc> searchConstantly(String keyword, int from, int size) throws IOException;
	
	AggregatedStoreDocPage aggregationSearch(String keyword, Pageable pageable) throws IOException;
	
	
//	Page<StoreDoc> searchInName(String keyword, Pageable pageable);
	
	/**
	 * 查询name字段 越近越好
	 * @param keyword
	 * @param pageable
	 * @return
	 */
//	Page<StoreDoc> searchInNameCloserBetter(String keyword, Pageable pageable);
	
	/**
	 * Search in both name and mainproducts fields
	 * @param keyword
	 * @param pageable
	 * @return
	 */
//	Page<StoreDoc> search(String keyword, Pageable pageable);
	
	/**
	 * Search in both name and mainproducts fields; 拆词后两个词隔的越近分越高
	 * @param keyword
	 * @param pageable
	 * @return
	 */
//	Page<StoreDoc> searchCloserBetter(String keyword, Pageable pageable);
		
	
//	Page<StoreDoc> searchFuzzily(String keyword, PageRequest pageable);
	
	/**
	 * Search in full text to avoid cross field IDF impact; But you cannot highlight the field that are not in search fields. 
	 * @param keyword
	 * @param pageable
	 * @return
	 */
//	Page<StoreDoc> searchFulltext(String keyword, PageRequest pageable);
	
	/**
	 * Search cross fields: https://www.elastic.co/guide/en/elasticsearch/guide/current/_cross_fields_queries.html
	 * @param keyword
	 * @param pageable
	 * @return
	 */
//	Page<StoreDoc> searchCorssFields(String keyword, PageRequest pageable);


 
	
}
