package com.joe.elasticserchdemo.service.impl;


import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joe.elasticsearchdemo.dto.AggregatedStoreDocPage;
import com.joe.elasticsearchdemo.dto.Pageable;
import com.joe.elasticserchdemo.document.StoreDoc;
import com.joe.elasticserchdemo.document.Type;
import com.joe.elasticserchdemo.service.StoreDocService;

@Service
public class StoreDocServiceImpl extends ElasticsearchCommonServiceImpl<StoreDoc> implements StoreDocService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StoreDocServiceImpl.class);

	@Autowired
	private RestHighLevelClient client;
	
	
	@Override
	public List<StoreDoc> findAll(int from, int size) throws IOException {
		SearchRequest searchRequest = new SearchRequest(StoreDoc.INDEX_NAME); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchAllQuery()); 
		searchSourceBuilder.from(from).size(size);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
		return getContent(searchResponse, StoreDoc.class);
	}

	@Override
	public List<StoreDoc> searchInName(String keyword, int from, int size) throws IOException {
		SearchRequest searchRequest = new SearchRequest(StoreDoc.INDEX_NAME); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchQuery(StoreDoc._name, keyword)); 
		searchSourceBuilder.highlighter(new HighlightBuilder().field(StoreDoc._name).highlighterType("plain"));
		
		searchRequest.source(searchSourceBuilder);
		
		searchSourceBuilder.from(from).size(size);
		SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchSourceBuilder.query());
		return getContent(searchResponse,StoreDoc.class);
	}

	@Override
	public List<StoreDoc> searchConstantly(String keyword, int from, int size) throws IOException {
		SearchRequest searchRequest = new SearchRequest(StoreDoc.INDEX_NAME); 
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		
		BoolQueryBuilder boolQueryBuilder= QueryBuilders.boolQuery();
		
		String[] terms = keyword.toLowerCase().split(StoreDoc.TOKENIZER_PATTERN);
		float nameBoost = 0.8f/terms.length;
		float mainBoost = 0.2f/terms.length;
		for (String term : terms) {			
			boolQueryBuilder.should(QueryBuilders.constantScoreQuery(QueryBuilders.termQuery(StoreDoc._name, term)).boost(nameBoost));
			boolQueryBuilder.should(QueryBuilders.constantScoreQuery(QueryBuilders.termQuery(StoreDoc._mainProducts, term)).boost(mainBoost));
		}
		
		boolQueryBuilder.must(QueryBuilders.termQuery(StoreDoc._storeType, Type.Golden));
		FunctionScoreQueryBuilder functionscoreBuilder =  QueryBuilders.functionScoreQuery(boolQueryBuilder, ScoreFunctionBuilders.fieldValueFactorFunction(StoreDoc._rating).setWeight(0.1f))
					.scoreMode(ScoreMode.SUM).boostMode(CombineFunction.SUM);
		searchSourceBuilder.query(functionscoreBuilder);
		
		searchSourceBuilder.highlighter(new HighlightBuilder().field(StoreDoc._name).field(StoreDoc._mainProducts).highlighterType("plain"));
		searchSourceBuilder.minScore(0.1f);
		
		searchRequest.source(searchSourceBuilder);
		
		searchSourceBuilder.from(from).size(size);
		SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchSourceBuilder.query());
		return getContent(searchResponse,StoreDoc.class);
	}
	
	@Override
	public AggregatedStoreDocPage aggregationSearch(String keyword, Pageable pageable) throws IOException {
		SearchRequest searchRequest = new SearchRequest(StoreDoc.INDEX_NAME); 
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder(); 
		searchSourceBuilder.query(QueryBuilders.matchQuery(StoreDoc._name, keyword)); 
		searchSourceBuilder.highlighter(new HighlightBuilder().field(StoreDoc._name).highlighterType("plain"));
		
		searchRequest.source(searchSourceBuilder);
		searchSourceBuilder.aggregation(AggregationBuilders.terms(StoreDoc._rating).field(StoreDoc._rating));
		searchSourceBuilder.aggregation(AggregationBuilders.terms(StoreDoc._storeType).field(StoreDoc._storeType));
		searchSourceBuilder.from((pageable.getPageNumber() - 1) * pageable.getPageSize()).size(pageable.getPageSize());
		SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
		LOGGER.info("\n search(): searchContent [" + keyword + "] \n DSL  = \n " + searchSourceBuilder.query());
		
		List<StoreDoc> list = getContent(searchResponse, StoreDoc.class);
		AggregatedStoreDocPage agg = new AggregatedStoreDocPage(list, searchResponse.getHits().getTotalHits().value, pageable);
		agg.setAggregations(searchResponse.getAggregations().asMap());		
		return agg;
	}

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

}
