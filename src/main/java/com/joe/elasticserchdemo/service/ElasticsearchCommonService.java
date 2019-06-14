package com.joe.elasticserchdemo.service;

import java.io.IOException;

public interface ElasticsearchCommonService {
	boolean indexExist(String indexName) throws IOException;
}
