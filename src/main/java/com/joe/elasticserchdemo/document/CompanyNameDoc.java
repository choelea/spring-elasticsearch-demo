package com.joe.elasticserchdemo.document;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.joe.elasticserchdemo.utils.EsMappingProp;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author joe
 * @date 2024/09/23
 */
public class CompanyNameDoc extends EsDoc implements Serializable {

    public static final String INDEX_NAME="company_name";
    public static final String INDEX_TYPE="_doc";

    @ExcelProperty("companyName") // Excel 中的列名
    private String name;
    public static final String _name = "name";

    public CompanyNameDoc() {
    }
    public CompanyNameDoc(String companyName) {
        this.name = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public static Map<String, Object> getMapping(){
        Map<String, Object> jsonMap = new HashMap<>();

        Map<String, Object> properties = new HashMap<>();
        properties.put(_name, new EsMappingProp("text").parameter("analyzer", "ik_max_word").parameter("search_analyzer", "ik_smart"));
        jsonMap.put("properties", properties);
        return jsonMap;
    }

}
