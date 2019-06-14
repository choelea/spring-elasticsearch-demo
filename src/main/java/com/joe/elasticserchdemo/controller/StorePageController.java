package com.joe.elasticserchdemo.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String all(Model model,String keyword) throws IOException {
		List<StoreDoc> page =  storeDocService.findAll(0, 50);
		model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        return "store/stores";
    }
	
	

}
