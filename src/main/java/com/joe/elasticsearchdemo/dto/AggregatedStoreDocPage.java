package com.joe.elasticsearchdemo.dto;

import java.util.List;
import java.util.Map;

import org.elasticsearch.search.aggregations.Aggregation;

import com.joe.elasticserchdemo.document.StoreDoc;

public class AggregatedStoreDocPage extends Page<StoreDoc> {


    private static final long serialVersionUID = -7686121923506935325L;

    public static final String BY_COUNTRY = "BY_COUNTRY";

    private List<BucketData> bucketsByCountry;

    private Map<String, Aggregation> aggregations;
    
    public AggregatedStoreDocPage(List<StoreDoc> content, long total, Pageable pageable) {
        super(content,  total, pageable);
    }

    public static String getByCountry() {
        return BY_COUNTRY;
    }

    public List<BucketData> getBucketsByCountry() {
        return bucketsByCountry;
    }

    public void setBucketsByCountry(List<BucketData> bucketsByCountry) {
        this.bucketsByCountry = bucketsByCountry;
    }

	public Map<String, Aggregation> getAggregations() {
		return aggregations;
	}

	public void setAggregations(Map<String, Aggregation> aggregations) {
		this.aggregations = aggregations;
	}    
}
