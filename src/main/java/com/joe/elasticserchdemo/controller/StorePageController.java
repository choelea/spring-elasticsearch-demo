package com.joe.elasticserchdemo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joe.elasticserchdemo.document.StoreDoc;
import com.joe.elasticserchdemo.service.StoreDocService;

@Controller
@RequestMapping("/page")
public class StorePageController {
	@Autowired
	private StoreDocService storeDocService;

	@GetMapping("/all")
	public String all(Model model, String keyword) throws IOException {
		List<StoreDoc> page = storeDocService.findAll(0, 50);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		return "store/stores";
	}

	@GetMapping("/store-names")
	public String searchStoreNames(Model model, String keyword) throws IOException {
		List<StoreDoc> page;
		if (StringUtils.isEmpty(keyword)) {
			page = storeDocService.findAll(0, 50);
		} else {
			page = storeDocService.searchInName(keyword, 0, 20);
		}
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		return "store/storeNames";
	}

	@GetMapping("/search-stores-constant-scoring")
	public String searchStoreConstantScoring(Model model, String keyword) throws IOException {
		List<StoreDoc> page;
		if (StringUtils.isEmpty(keyword)) {
			page = storeDocService.findAll(0, 50);
		} else {
			page = storeDocService.searchConstantly(keyword, 0, 30);
		}
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		return "store/storeNamesConstantScore";
	}
}
