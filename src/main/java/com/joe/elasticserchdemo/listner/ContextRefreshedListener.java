package com.joe.elasticserchdemo.listner;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.joe.elasticserchdemo.document.StoreDoc;
import com.joe.elasticserchdemo.document.StoreDocBuilder;
 
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedListener.class);
	
	@Autowired
	private RestHighLevelClient client;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			LOGGER.info("-----------Initiating after context loaded---------");
			initiateOnMissing(StoreDoc.INDEX_NAME);		
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	private void initiateOnMissing(String indexName) throws IOException {
		if(!indexExist(StoreDoc.INDEX_NAME)) {
			CreateIndexRequest request = new CreateIndexRequest(StoreDoc.INDEX_NAME);
			request.settings(Settings.builder() 
					.put("index.number_of_shards", 3)
					.put("index.number_of_replicas", 2)
					);
			client.indices().create(request, RequestOptions.DEFAULT);
			
			putMapping(StoreDoc.INDEX_NAME,StoreDoc.getMapping());
			createTestData();
		}
	}
	private void createTestData() throws IOException{
		createStoreDocs();
	}
	
	
	private boolean indexExist(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName); 
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}

	

	private void putMapping(String type,Map<String, Object> map) throws IOException {
		PutMappingRequest request = new PutMappingRequest(StoreDoc.INDEX_NAME); 
		
		request.source(map);
		AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
		System.out.println(putMappingResponse);
	}
	
	private void createStoreDocs() throws IOException {
		BulkRequest request = new BulkRequest(); 
		request.add(new StoreDocBuilder(0l).name("XiaoMi Authorized Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(1l).name("Oppo Authorized Owned by Joe in Wuhan Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(2l).name("Meizu Authorized Shop Double Authorized").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(3l).name("Sung Authorized Shop").mainProducts("Smart Phone , Stupid Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(4l).name("Vivo Authorized Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(5l).name("Lenovo Authorized  Shop").mainProducts("Stupid Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(6l).name("Sony Authorized VIP Shop").mainProducts("Stupid Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(7l).name("Apple Store").mainProducts("Ipad, Mac Pro").fullText().buildIndex());
		request.add(new StoreDocBuilder(8l).name("Samsung Authorized Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(9l).name("Smart Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(10l).name("Mark's Mobile Shop").mainProducts("Smart Phone, Stupid Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(11l).name("Charlice Mobile Shop").mainProducts("Apple Phone, Old Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(12l).name("Charlice Mobile Shop").mainProducts("Apple Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(13l).name("Charlice Mobile Shop").mainProducts("Apple Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(14l).name("Oppo Authorized Shop Owned by Joe in Wuhan").mainProducts("Apple Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(15l).name("Charlice Fruit Shop").mainProducts("Pear, Watermelon").fullText().buildIndex());
		request.add(new StoreDocBuilder(16l).name("Jane's Mobile Shop").mainProducts("Smart-Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(17l).name("Charlise Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(18l).name("Authorized Owned by Joe Phone Meizu Shop").mainProducts("Smart Phone").fullText().buildIndex());		
		request.add(new StoreDocBuilder(19l).name("Meizu Authorized Phone Shop Located in Wuhan Optic Valley Software Park").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(20l).name("Authorized Phone Meizu Shop").mainProducts("Smart Phone").fullText().buildIndex());
		for(int i=21; i<30; i++) {
			request.add(new StoreDocBuilder(Long.valueOf(i).longValue()).name("Charlise Shop").mainProducts("Apple Phone").fullText().buildIndex());
		}
		BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
		LOGGER.info("-------Finished Bulk: {}------------", bulkResponse.status().name());
	}
}
