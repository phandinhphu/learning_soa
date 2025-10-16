package com.example.product.service.interfaces;

import java.util.List;

import com.example.product.dto.CategoryDTO;
import com.example.product.dto.CategoryRequest;
import com.example.product.model.Category;

public interface CategoryService {
	List<CategoryDTO> getAllCategories();
	CategoryDTO getCategoryById(String id);
	void addCategory(CategoryRequest categoryRequest);
	void updateCategory(String id, Category category);
	void deleteCategory(String id);
}
