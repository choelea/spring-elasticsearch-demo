package com.joe.elasticserchdemo;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig  {
	@Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
    	final CredentialsProvider credentialsProvider =
    		    new BasicCredentialsProvider();
    		credentialsProvider.setCredentials(AuthScope.ANY,
    		    new UsernamePasswordCredentials("elastic", "joe2020"));
    	return new RestHighLevelClient(
	        RestClient.builder( new HttpHost(elasticsearchHost, 9200, "http"))
	        .setHttpClientConfigCallback(new HttpClientConfigCallback() {
		        @Override
		        public HttpAsyncClientBuilder customizeHttpClient(
		                HttpAsyncClientBuilder httpClientBuilder) {
		            return httpClientBuilder
		                .setDefaultCredentialsProvider(credentialsProvider);
		        }
		    }));

    }
}

