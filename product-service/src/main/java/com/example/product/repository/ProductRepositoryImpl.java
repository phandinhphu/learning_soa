package com.example.product.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.example.product.dao.ProductDAO;
import com.example.product.dto.ProductDTO;
import com.example.product.model.Product;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
	@Autowired
	private ProductDAO productDAO;

	@Override
	public List<ProductDTO> getAllProducts() {
		return productDAO.getAllProducts();
	}

	@Override
	public ProductDTO getProductById(String id) {
		return productDAO.getProductById(id);
	}

	@Override
	public void addProduct(Product product) {
		productDAO.addProduct(product);
	}

	@Override
	public void updateProduct(Product product) {
		productDAO.updateProduct(product);
	}

	@Override
	public void deleteProduct(String id) {
		productDAO.deleteProduct(id);
	}

	@Override
	public void updateProductStock(String id, int newStock) {
		productDAO.updateProductStock(id, newStock);
	}

}
