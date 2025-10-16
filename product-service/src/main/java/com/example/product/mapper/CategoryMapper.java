package com.example.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.product.dto.CategoryDTO;
import com.example.product.dto.CategoryRequest;
import com.example.product.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
	
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
	
	// Mapping methods
	CategoryDTO toDTO(Category category);
	
	Category toEntity(CategoryDTO categoryDTO);
	
	CategoryRequest toRequest(Category category);
	
	@Mapping(target = "id", ignore = true)
	Category toEntity(CategoryRequest categoryRequest);
}
