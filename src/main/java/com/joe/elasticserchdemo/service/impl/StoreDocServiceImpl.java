package com.joe.elasticserchdemo.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.elasticserchdemo.document.StoreDoc;
import com.joe.elasticserchdemo.service.StoreDocService;

@Service
public class StoreDocServiceImpl extends ElasticsearchCommonServiceImpl<StoreDoc> implements StoreDocService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreDocServiceImpl.class);

	@Autowired
	private RestHighLevelClient client;
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
	public List<StoreDoc> findAll(int from, int size) throws IOException {
		SearchRequest searchRequest = new SearchRequest(StoreDoc.INDEX_NAME); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
		searchSourceBuilder.from(from).size(size);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
		return getSearchResult(searchResponse);
	}

	@Override
	public List<StoreDoc> searchInName(String keyword, int from, int size) throws IOException {
		SearchRequest searchRequest = new SearchRequest(StoreDoc.INDEX_NAME); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchQuery(StoreDoc._name, keyword)); 
		searchSourceBuilder.highlighter(new HighlightBuilder().field(StoreDoc._name,1).highlighterType("plain"));
		
		searchRequest.source(searchSourceBuilder);
		
		searchSourceBuilder.from(from).size(size);
		SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchSourceBuilder.query());
		return getSearchResult(searchResponse);
	}
//
//	@Override
//	public Page<StoreDoc> searchInNameCloserBetter(String keyword, Pageable pageable) {
//		Assert.notNull(keyword, "keyword cannot be null");
//		Assert.hasLength(keyword.trim(), "keyword cannot be null nor empty");
//		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//		queryBuilder.must(QueryBuilders.matchQuery(StoreDoc._name, keyword).boost(10));
//		queryBuilder.should(QueryBuilders.matchPhraseQuery(StoreDoc._name, keyword).slop(30).boost(10));
//		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
//				.withHighlightFields(new HighlightBuilder.Field(StoreDoc._name).numOfFragments(1)).build();
//		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//		Page<StoreDoc> page = elasticsearchTemplate.queryForPage(searchQuery, StoreDoc.class);
//		return page;
//	}
//
//	@Override
//	public Page<StoreDoc> search(String keyword, Pageable pageable) {
//		QueryBuilder queryBuilder = null;
//		if (StringUtils.isEmpty(keyword)) {
//			queryBuilder = QueryBuilders.matchAllQuery();
//		} else {
//			queryBuilder = QueryBuilders.multiMatchQuery(keyword, StoreDoc._name, StoreDoc._mainProducts);
//		}
//		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
//				.withHighlightFields(new HighlightBuilder.Field(StoreDoc._name).numOfFragments(1),
//						new HighlightBuilder.Field(StoreDoc._mainProducts).numOfFragments(1))
//				.build();
//
//		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//		Page<StoreDoc> page = elasticsearchTemplate.queryForPage(searchQuery, StoreDoc.class,  extResultMapper);
//		return page;
//	}
//
//	@Override
//	public Page<StoreDoc> searchCloserBetter(String keyword, Pageable pageable) {
//		Assert.notNull(keyword, "keyword cannot be null");
//		Assert.hasLength(keyword.trim(), "keyword cannot be null nor empty");
//		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//		queryBuilder.must(
//				QueryBuilders.multiMatchQuery(keyword).field(StoreDoc._name, 10).field(StoreDoc._mainProducts, 10));
//		queryBuilder.should(QueryBuilders.matchPhraseQuery(StoreDoc._name, keyword).slop(3).boost(50));
//
//		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
//				.withHighlightFields(new HighlightBuilder.Field(StoreDoc._name).numOfFragments(1),
//						new HighlightBuilder.Field(StoreDoc._mainProducts).numOfFragments(1))
//				.build();
//		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//		Page<StoreDoc> page = elasticsearchTemplate.queryForPage(searchQuery, StoreDoc.class);
//		return page;
//	}
//
//	@Override
//	public Page<StoreDoc> searchFuzzily(String keyword, PageRequest pageable) {
//		QueryBuilder queryBuilder = null;
//		if (StringUtils.isEmpty(keyword)) {
//			queryBuilder = QueryBuilders.matchAllQuery();
//		} else {
//			queryBuilder = QueryBuilders.matchQuery(StoreDoc._name, keyword).fuzziness(Fuzziness.AUTO);
//		}
//		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
//				.withHighlightFields(new HighlightBuilder.Field(StoreDoc._name).numOfFragments(1)).build();
//
//		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//		Page<StoreDoc> page = elasticsearchTemplate.queryForPage(searchQuery, StoreDoc.class);
//		return page;
//	}
//
//	 
//
//	@Override
//	public Page<StoreDoc> searchFulltext(String keyword, PageRequest pageable) {
//		QueryBuilder queryBuilder = null;
//		if (StringUtils.isEmpty(keyword)) {
//			queryBuilder = QueryBuilders.matchAllQuery();
//		} else {
//			queryBuilder = QueryBuilders.matchQuery(StoreDoc._fullText, keyword);
//		}
//		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
//				.withHighlightFields(new HighlightBuilder.Field(StoreDoc._name).numOfFragments(1),
//						new HighlightBuilder.Field(StoreDoc._mainProducts).numOfFragments(1))
//				.build();
//
//		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//		Page<StoreDoc> page = elasticsearchTemplate.queryForPage(searchQuery, StoreDoc.class);
//		return page;
//	}
//
//	@Override
//	public Page<StoreDoc> searchCorssFields(String keyword, PageRequest pageable) {
//		QueryBuilder queryBuilder = null;
//		if (StringUtils.isEmpty(keyword)) {
//			queryBuilder = QueryBuilders.matchAllQuery();
//		} else {
//			queryBuilder = QueryBuilders.multiMatchQuery(keyword, StoreDoc._name, StoreDoc._mainProducts)
//					.type(MultiMatchQueryBuilder.Type.BEST_FIELDS);
//		}
//		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
//				.withHighlightFields(new HighlightBuilder.Field(StoreDoc._name).numOfFragments(1),
//						new HighlightBuilder.Field(StoreDoc._mainProducts).numOfFragments(1))
//				.build();
//
//		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchQuery.getQuery().toString());
//		Page<StoreDoc> page = elasticsearchTemplate.queryForPage(searchQuery, StoreDoc.class);
//		return page;
//	}

	private List<StoreDoc> getSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<StoreDoc> storeDocs = new ArrayList<>();

        for (SearchHit hit : searchHit){
        	StoreDoc doc = objectMapper.convertValue(hit.getSourceAsMap(), StoreDoc.class);
        	populateHighLightedFields(doc,hit.getHighlightFields());
        	storeDocs.add(doc);
        }

        return storeDocs;
    }
}
