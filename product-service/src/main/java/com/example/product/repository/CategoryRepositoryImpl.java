package com.example.product.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.product.dao.CategoryDAO;
import com.example.product.dto.CategoryDTO;
import com.example.product.model.Category;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {
	@Autowired
	private CategoryDAO categoryDAO;

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryDAO.getAllCategories();
	}

	@Override
	public CategoryDTO getCategoryById(String id) {
		return categoryDAO.getCategoryById(id);
	}

	@Override
	public void addCategory(Category category) {
		categoryDAO.addCategory(category);
	}

	@Override
	public void updateCategory(Category category) {
		categoryDAO.updateCategory(category);
	}

	@Override
	public void deleteCategory(String id) {
		categoryDAO.deleteCategory(id);
	}

	@Override
	public boolean existsById(String id) {
		return categoryDAO.existsById(id);
	}
}
