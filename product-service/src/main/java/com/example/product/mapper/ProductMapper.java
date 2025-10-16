package com.example.product.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductRequest;
import com.example.product.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);
	
	// Define mapping methods here
	@Mapping(target = "categoryName", ignore = true)
	ProductDTO toDTO(Product product);
	
	Product toEntity(ProductDTO productDTO);
	
	ProductRequest toRequest(Product product);
	
	@Mapping(target = "id", ignore = true)
	Product toEntity(ProductRequest productRequest);
}
