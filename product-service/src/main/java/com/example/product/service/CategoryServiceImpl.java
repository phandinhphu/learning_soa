package com.example.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.product.dto.CategoryDTO;
import com.example.product.dto.CategoryRequest;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.exception.ValidationException;
import com.example.product.mapper.CategoryMapper;
import com.example.product.model.Category;
import com.example.product.repository.CategoryRepository;
import com.example.product.service.interfaces.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	@Transactional(readOnly = true)
	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.getAllCategories();
	}

	@Override
	@Transactional(readOnly = true)
	public CategoryDTO getCategoryById(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID danh mục không được để trống");
		}
		
		CategoryDTO category = categoryRepository.getCategoryById(id);
		if (category == null) {
			throw new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id);
		}
		
		return category;
	}

	@Override
	public void addCategory(CategoryRequest categoryRequest) {
		if (categoryRequest == null) {
			throw new ValidationException("Thông tin danh mục không được null");
		}
		
		// Validate category request
		validateCategoryRequest(categoryRequest);
		
		// Generate unique ID
		String id = UUID.randomUUID().toString().substring(0, 8);
		
		Category newCategory = categoryMapper.toEntity(categoryRequest);
		newCategory.setId(id);
		
		categoryRepository.addCategory(newCategory);
	}

	@Override
	public void updateCategory(String id, Category category) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID danh mục không được để trống");
		}
		
		if (category == null) {
			throw new ValidationException("Thông tin danh mục không được null");
		}
		
		// Check if category exists
		CategoryDTO existingCategory = categoryRepository.getCategoryById(id);
		if (existingCategory == null) {
			throw new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id);
		}
		
		// Validate category data
		if (category.getName() == null || category.getName().trim().isEmpty()) {
			throw new ValidationException("Tên danh mục không được để trống");
		}
		
		// Set ID to ensure consistency
		category.setId(id);
		
		categoryRepository.updateCategory(category);
	}

	@Override
	public void deleteCategory(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID danh mục không được để trống");
		}
		
		// Check if category exists
		CategoryDTO existingCategory = categoryRepository.getCategoryById(id);
		if (existingCategory == null) {
			throw new ResourceNotFoundException("Không tìm thấy danh mục với ID: " + id);
		}
		
		// TODO: Check if category has products before deleting
		// You might want to prevent deletion if there are products in this category
		
		categoryRepository.deleteCategory(id);
	}
	
	/**
	 * Validate category request data
	 */
	private void validateCategoryRequest(CategoryRequest categoryRequest) {
		if (categoryRequest.getName() == null || categoryRequest.getName().trim().isEmpty()) {
			throw new ValidationException("Tên danh mục không được để trống");
		}
		
		// You can add more validation rules here
		if (categoryRequest.getName().length() > 100) {
			throw new ValidationException("Tên danh mục không được vượt quá 100 ký tự");
		}
	}
}
