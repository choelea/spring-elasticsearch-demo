package com.joe.elasticserchdemo.listner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joe.elasticserchdemo.document.CompanyNameDoc;
import com.joe.elasticserchdemo.service.CompanyNameExcelService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.joe.elasticserchdemo.document.StoreDoc;
import com.joe.elasticserchdemo.document.StoreDocBuilder;
import com.joe.elasticserchdemo.document.Type;
 
@Component
public class ContextRefreshedListener implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextRefreshedListener.class);
	
	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private CompanyNameExcelService companyNameExcelService;

	private static ObjectMapper objectMapper = new ObjectMapper();

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
					.put("index.number_of_shards", 1)
					.put("index.number_of_replicas", 0)
					.put("index.analysis.analyzer.chem_analyzer_en.type","pattern")
					.put("index.analysis.analyzer.chem_analyzer_en.pattern","[^\\w^-]")
					.put("index.analysis.analyzer.chem_analyzer_en.lowercase",true)
					.put("index.analysis.analyzer.chem_analyzer_en.stopwords","_english_")
					);
			client.indices().create(request, RequestOptions.DEFAULT);
			putMapping(StoreDoc.INDEX_NAME,StoreDoc.getMapping());
			createStoreDocs();
		}
		if(!indexExist(CompanyNameDoc.INDEX_NAME)) {
			CreateIndexRequest request = new CreateIndexRequest(CompanyNameDoc.INDEX_NAME);
			request.settings(Settings.builder()
					.put("index.number_of_shards", 1)
					.put("index.number_of_replicas", 0)
//					.put("index.analysis.analyzer.ik_max_word.type","pattern")
//					.put("index.analysis.analyzer.ik_max_word.pattern","[^\\w^-]")
//					.put("index.analysis.analyzer.ik_max_word.lowercase",true)
//					.put("index.analysis.analyzer.ik_max_word.stopwords","_chinese_")
			);
			client.indices().create(request, RequestOptions.DEFAULT);
			putMapping(CompanyNameDoc.INDEX_NAME,CompanyNameDoc.getMapping());
			createCompanyNameData();
		}
	}

	private void createCompanyNameData() throws IOException {
		List<CompanyNameDoc> companyNameDocs = companyNameExcelService.readExcel("docs/companyNames.xlsx");

		BulkRequest request = new BulkRequest();
		companyNameDocs.forEach(companyNameDoc -> {
                    IndexRequest indexRequest = new IndexRequest(CompanyNameDoc.INDEX_NAME, CompanyNameDoc.INDEX_TYPE);
                    indexRequest.id(UUID.randomUUID().toString());
                    indexRequest.source(objectMapper.convertValue(companyNameDoc, Map.class));
					request.add(indexRequest);
                });
		BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
		LOGGER.info("-------Finished Company Name Bulk: {}------------", bulkResponse.status().name());
	}

	private boolean indexExist(String indexName) throws IOException {
		GetIndexRequest request = new GetIndexRequest(indexName); 
		return client.indices().exists(request, RequestOptions.DEFAULT);
	}

	private void putMapping(String indexName,Map<String, Object> map) throws IOException {
		PutMappingRequest request = new PutMappingRequest(indexName);
		
		request.source(map);
		AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
		System.out.println(putMappingResponse);
	}
	
	private void createStoreDocs() throws IOException {
		BulkRequest request = new BulkRequest(); 
		request.add(new StoreDocBuilder(0l).rating(4).type(Type.Normal.name()).name("XiaoMi Authorized Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(1l).rating(4).type(Type.Normal.name()).name("Oppo Authorized Owned by Joe in Wuhan Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(2l).rating(4).type(Type.Normal.name()).name("Meizu Authorized Shop Double Authorized").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(3l).rating(4).type(Type.Normal.name()).name("Sung Authorized Shop").mainProducts("Smart Phone , Stupid Phone; 85% is a 10279-57-9").fullText().buildIndex());
		request.add(new StoreDocBuilder(4l).rating(5).type(Type.Golden.name()).name("Vivo Authorized Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(5l).rating(4).type(Type.Normal.name()).name("Lenovo Authorized  Shop").mainProducts("Stupid Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(6l).rating(5).type(Type.Normal.name()).name("Sony Authorized VIP Shop").mainProducts("Stupid Phone,  85% is a 10279-57-9").fullText().buildIndex());
		request.add(new StoreDocBuilder(7l).rating(4).type(Type.Golden.name()).name("Apple Store").mainProducts("Ipad, Mac Pro").fullText().buildIndex());
		request.add(new StoreDocBuilder(8l).rating(5).type(Type.Golden.name()).name("Samsung Authorized Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(9l).rating(4).type(Type.Golden.name()).name("Smart Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(10l).rating(5).type(Type.Golden.name()).name("Mark's Mobile Shop").mainProducts("Smart Phone, Stupid Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(11l).rating(4).type(Type.Golden.name()).name("Charlice Mobile Shop").mainProducts("Apple Phone, Old Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(12l).rating(4).type(Type.Golden.name()).name("Charlice Mobile Shop").mainProducts("Apple Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(13l).rating(5).type(Type.Golden.name()).name("Charlice Mobile Shop").mainProducts("Apple Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(14l).rating(4).type(Type.Golden.name()).name("Oppo Authorized Shop Owned by Joe in Wuhan").mainProducts("Apple Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(15l).rating(5).type(Type.Golden.name()).name("Charlice Fruit Shop").mainProducts("Pear, Watermelon").fullText().buildIndex());
		request.add(new StoreDocBuilder(16l).rating(4).type(Type.Golden.name()).name("Jane's Mobile Shop").mainProducts("Smart-Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(17l).rating(4).type(Type.Golden.name()).name("Charlise Shop").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(18l).rating(4).type(Type.Normal.name()).name("Authorized Owned by Joe Phone Meizu Shop").mainProducts("Smart Phone;Stupid Phone,  85% is a 10279-57-9").fullText().buildIndex());		
		request.add(new StoreDocBuilder(19l).rating(4).type(Type.Golden.name()).name("Meizu Authorized Phone Shop Located in Wuhan Optic Valley Software Park").mainProducts("Smart Phone").fullText().buildIndex());
		request.add(new StoreDocBuilder(20l).rating(4).type(Type.Golden.name()).name("Authorized Phone Meizu Shop").mainProducts("Smart Phone").fullText().buildIndex());
		for(int i=21; i<30; i++) {
			request.add(new StoreDocBuilder(Long.valueOf(i).longValue()).rating(4).type(Type.Golden.name()).name("Charlise Shop").mainProducts("Apple Phone").fullText().buildIndex());
		}
		BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
		LOGGER.info("-------Finished Bulk: {}------------", bulkResponse.status().name());
	}
}
