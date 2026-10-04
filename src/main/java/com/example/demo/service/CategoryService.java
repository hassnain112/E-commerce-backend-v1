package com.example.demo.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CategoryDTO;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepo;
	
	public CategoryService(CategoryRepository categoryRepo){
		this.categoryRepo=categoryRepo;
	}
	public ResponseEntity<Category> createCategory(CategoryDTO category){
		
	 Category c = new Category();
	 c.setName(category.categoryName());
	 
	 Category savedCategory = categoryRepo.save(c);

	 return ResponseEntity.ok(savedCategory);
}}