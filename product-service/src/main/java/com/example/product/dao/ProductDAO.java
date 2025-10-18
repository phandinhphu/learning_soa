package com.example.product.dao;

import java.util.List;

import com.example.product.dto.ProductDTO;
import com.example.product.model.Product;

public interface ProductDAO {
	List<ProductDTO> getAllProducts();
	ProductDTO getProductById(String id);
	void addProduct(Product product);
	void updateProduct(Product product);
	void deleteProduct(String id);
	void updateProductStock(String id, int newStock);
}
