package com.joe.elasticserchdemo.controller;

import com.joe.elasticserchdemo.document.CompanyNameDoc;
import com.joe.elasticserchdemo.document.StoreDoc;
import com.joe.elasticserchdemo.service.CompanyNameService;
import com.joe.elasticserchdemo.service.StoreDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/comp")
public class CompanyNamesPageController {
	@Autowired
	private CompanyNameService companyNameService;
	@Autowired
	private StoreDocService storeDocService;

	@GetMapping("/names")
	public String searchStoreNames(Model model, String keyword) throws IOException {
		if (!StringUtils.isEmpty(keyword)) {
			List<CompanyNameDoc> page = companyNameService.searchInName(keyword, 0, 20);
			model.addAttribute("keyword", keyword);
			model.addAttribute("page", page);
		}
		return "company/companyNames";
	}

	@GetMapping("/all")
	public String all(Model model, String keyword) throws IOException {
		List<StoreDoc> page = storeDocService.findAll(0, 50);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		return "store/stores";
	}

}
