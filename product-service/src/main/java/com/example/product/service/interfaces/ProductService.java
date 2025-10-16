package com.example.product.service.interfaces;

import java.util.List;

import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductRequest;
import com.example.product.model.Product;

public interface ProductService {
	List<ProductDTO> getAllProducts();
	ProductDTO getProductById(String id);
	void addProduct(ProductRequest product);
	void updateProduct(Product product);
	void deleteProduct(String id);
}
