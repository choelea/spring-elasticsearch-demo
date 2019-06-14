package com.joe.elasticserchdemo.service.impl;

import java.io.IOException;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joe.elasticserchdemo.service.ElasticsearchCommonService;
@Service
public class ElasticsearchCommonServiceImpl implements ElasticsearchCommonService {

	@Autowired
	private RestHighLevelClient client;
	@Override
	public boolean indexExist(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName); 
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}

}
