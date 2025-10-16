package com.example.product.repository;

import java.util.List;

import com.example.product.dto.CategoryDTO;
import com.example.product.model.Category;

public interface CategoryRepository {
	List<CategoryDTO> getAllCategories();
	CategoryDTO getCategoryById(String id);
	void addCategory(Category category);
	void updateCategory(Category category);
	void deleteCategory(String id);
	boolean existsById(String id);
}
