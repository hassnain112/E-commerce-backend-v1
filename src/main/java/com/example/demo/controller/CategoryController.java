package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;
import com.example.demo.service.CategoryService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CategoryController {
	private final CategoryService service;
	public CategoryController(CategoryService service) {
		this.service=service;
	}
	@PostMapping("/category")
	public ResponseEntity<Category> createCategory(@RequestBody CategoryDTO category){
		return service.createCategory(category);
	}
	
	@GetMapping("/csrf")
	public CsrfToken csrf(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	}

	

}
