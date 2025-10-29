package com.example.product.mapper;

import org.springframework.stereotype.Component;

import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductRequest;
import com.example.product.model.Product;

@Component
public class ProductMapper {

	public ProductDTO toDTO(Product product) {
		if (product == null) {
			return null;
		}
		ProductDTO dto = new ProductDTO();
		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setDescription(product.getDescription());
		dto.setPrice(product.getPrice());
		dto.setStock(product.getStock());

		if (product.getCategoryId() != null) {
			try {
				dto.setCategoryId(Long.valueOf(product.getCategoryId()));
			} catch (NumberFormatException e) {
				dto.setCategoryId(null);
			}
		} else {
			dto.setCategoryId(null);
		}

		dto.setCategoryName(null);
		return dto;
	}

	public Product toEntity(ProductDTO productDTO) {
		if (productDTO == null) {
			return null;
		}
		Product entity = new Product();
		entity.setId(productDTO.getId());
		entity.setName(productDTO.getName());
		entity.setDescription(productDTO.getDescription());
		entity.setPrice(productDTO.getPrice());
		entity.setStock(productDTO.getStock());
		if (productDTO.getCategoryId() != null) {
			entity.setCategoryId(String.valueOf(productDTO.getCategoryId()));
		} else {
			entity.setCategoryId(null);
		}
		return entity;
	}

	public ProductRequest toRequest(Product product) {
		if (product == null) {
			return null;
		}
		ProductRequest req = new ProductRequest();
		req.setName(product.getName());
		req.setDescription(product.getDescription());
		req.setPrice(product.getPrice());
		req.setStock(product.getStock());
		req.setCategoryId(product.getCategoryId());
		return req;
	}

	public Product toEntity(ProductRequest productRequest) {
		if (productRequest == null) {
			return null;
		}
		Product entity = new Product();

		entity.setName(productRequest.getName());
		entity.setDescription(productRequest.getDescription());
		entity.setPrice(productRequest.getPrice());
		entity.setStock(productRequest.getStock());
		entity.setCategoryId(productRequest.getCategoryId());
		return entity;
	}
}
