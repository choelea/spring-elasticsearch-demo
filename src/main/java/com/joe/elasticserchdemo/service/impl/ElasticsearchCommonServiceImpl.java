package com.joe.elasticserchdemo.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.elasticserchdemo.document.EsDoc;
import com.joe.elasticserchdemo.service.ElasticsearchCommonService;
@Service
public class ElasticsearchCommonServiceImpl<T extends EsDoc> implements ElasticsearchCommonService {

	@Autowired
	private RestHighLevelClient client;
	
	@Autowired
	private ObjectMapper objectMapper;
	@Override
	public boolean indexExist(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName); 
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}
	protected List<T> getSearchResult(SearchResponse response, Class<T> clazz) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<T> list = new ArrayList<>();

        for (SearchHit hit : searchHit){
        	T object = objectMapper.convertValue(hit.getSourceAsMap(), clazz);
        	object.setScore(hit.getScore());
        	populateHighLightedFields(object,hit.getHighlightFields());
        	list.add(object);
        }

        return list;
    }
	protected void populateHighLightedFields(T result, Map<String, HighlightField> highlightFields) {
		for (HighlightField field : highlightFields.values()) {
			try {
				PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));				
			} catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
				throw new ElasticsearchException("failed to set highlighted value for field: " + field.getName()
						+ " with value: " + field.getFragments(), e);
			} 
		}		
	}
	private String concat(Text[] texts) {
		StringBuffer sb = new StringBuffer();
		for (Text text : texts) {
			sb.append(text.toString());
		}
		return sb.toString();
	}
}
