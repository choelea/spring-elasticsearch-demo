package com.joe.elasticserchdemo.service.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joe.elasticserchdemo.service.ElasticsearchCommonService;
@Service
public class ElasticsearchCommonServiceImpl<T> implements ElasticsearchCommonService {

	@Autowired
	private RestHighLevelClient client;
	@Override
	public boolean indexExist(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName); 
		return client.indices().exists(request, RequestOptions.DEFAULT);
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
