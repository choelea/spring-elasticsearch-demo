package com.joe.elasticserchdemo.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.joe.elasticserchdemo.document.CompanyNameDoc;

import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author joe
 * @date 2024/09/23
 */
@Service
public class CompanyNameExcelService {



    // 读取 Excel 文件并返回对象列表
    public  List<CompanyNameDoc> readExcel(String fileName) throws IOException {
        List<CompanyNameDoc> companyNameDocs = new ArrayList<>();

        try (InputStream inputStream = ExcelReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("文件未找到: " + fileName);
            }
            // 使用 EasyExcel 读取
            EasyExcel.read(inputStream, CompanyNameDoc.class, new AnalysisEventListener<CompanyNameDoc>() {
                @Override
                public void invoke(CompanyNameDoc doc, AnalysisContext context) {
                    // 每读取到一行数据，就将其添加到列表中
                    companyNameDocs.add(doc);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 读取完成后的操作
                    System.out.println("All data read complete.");
                }
            }).excelType(ExcelTypeEnum.XLSX).sheet(0).doRead(); // 读取第一个 sheet

            return companyNameDocs;
        }
    }
}
