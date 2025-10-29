package com.example.product.mapper;

import org.springframework.stereotype.Component;

import com.example.product.dto.CategoryDTO;
import com.example.product.dto.CategoryRequest;
import com.example.product.model.Category;

@Component
public class CategoryMapper {

	public CategoryDTO toDTO(Category category) {
		if (category == null) {
			return null;
		}
		CategoryDTO dto = new CategoryDTO();
		dto.setId(category.getId());
		dto.setName(category.getName());
		dto.setDescription(category.getDescription());
		return dto;
	}

	public Category toEntity(CategoryDTO categoryDTO) {
		if (categoryDTO == null) {
			return null;
		}
		Category entity = new Category();
		entity.setId(categoryDTO.getId());
		entity.setName(categoryDTO.getName());
		entity.setDescription(categoryDTO.getDescription());
		return entity;
	}

	public CategoryRequest toRequest(Category category) {
		if (category == null) {
			return null;
		}
		CategoryRequest req = new CategoryRequest();
		req.setName(category.getName());
		req.setDescription(category.getDescription());
		return req;
	}

	public Category toEntity(CategoryRequest categoryRequest) {
		if (categoryRequest == null) {
			return null;
		}
		Category entity = new Category();

		entity.setName(categoryRequest.getName());
		entity.setDescription(categoryRequest.getDescription());
		return entity;
	}
}
