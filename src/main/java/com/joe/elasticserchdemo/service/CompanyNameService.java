package com.joe.elasticserchdemo.service;


import com.joe.elasticsearchdemo.dto.AggregatedStoreDocPage;
import com.joe.elasticsearchdemo.dto.Pageable;
import com.joe.elasticserchdemo.document.CompanyNameDoc;
import com.joe.elasticserchdemo.document.StoreDoc;

import java.io.IOException;
import java.util.List;

public interface CompanyNameService {

	List<CompanyNameDoc> searchInName(String keyword, int from, int size) throws IOException;

}
