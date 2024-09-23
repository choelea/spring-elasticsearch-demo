package com.joe.elasticserchdemo.service.impl;


import com.joe.elasticserchdemo.document.CompanyNameDoc;
import com.joe.elasticserchdemo.service.CompanyNameService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CompanyNameServiceImpl extends ElasticsearchCommonServiceImpl<CompanyNameDoc> implements CompanyNameService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompanyNameServiceImpl.class);

	@Autowired
	private RestHighLevelClient client;


	@Override
	public List<CompanyNameDoc> searchInName(String keyword, int from, int size) throws IOException {
		SearchRequest searchRequest = new SearchRequest(CompanyNameDoc.INDEX_NAME);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchQuery(CompanyNameDoc._name, keyword));
		searchSourceBuilder.highlighter(new HighlightBuilder().field(CompanyNameDoc._name).highlighterType("plain"));

		searchRequest.source(searchSourceBuilder);

		searchSourceBuilder.from(from).size(size);
		SearchResponse searchResponse =
				client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchSourceBuilder.query());
		return getContent(searchResponse,CompanyNameDoc.class);
	}
}
