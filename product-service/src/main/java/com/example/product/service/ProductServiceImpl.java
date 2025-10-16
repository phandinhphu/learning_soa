package com.example.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductRequest;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.exception.ValidationException;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;
import com.example.product.service.interfaces.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductMapper productMapper;

	@Override
	@Transactional(readOnly = true)
	public List<ProductDTO> getAllProducts() {
		return productRepository.getAllProducts();
	}

	@Override
	@Transactional(readOnly = true)
	public ProductDTO getProductById(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID sản phẩm không được để trống");
		}
		ProductDTO product = productRepository.getProductById(id);
		if (product == null) {
			throw new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id);
		}
		return product;
	}

	@Override
	public void addProduct(ProductRequest productRequest) {
		if (productRequest == null) {
			throw new ValidationException("Thông tin sản phẩm không được null");
		}
		
		// Validate product request
		validateProductRequest(productRequest);
		
		// Generate unique ID
		String id = UUID.randomUUID().toString().substring(0, 8);

		Product newProduct = productMapper.toEntity(productRequest);
		newProduct.setId(id);
		
		productRepository.addProduct(newProduct);
	}

	@Override
	public void updateProduct(Product product) {
		if (product == null || product.getId() == null) {
			throw new ValidationException("Thông tin sản phẩm hoặc ID không được null");
		}
		
		// Check if product exists
		ProductDTO existingProduct = productRepository.getProductById(product.getId());
		if (existingProduct == null) {
			throw new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + product.getId());
		}
		
		// Validate product data
		if (product.getName() == null || product.getName().trim().isEmpty()) {
			throw new ValidationException("Tên sản phẩm không được để trống");
		}
		
		if (product.getPrice() == null || product.getPrice() <= 0) {
			throw new ValidationException("Giá sản phẩm phải lớn hơn 0");
		}
		
		productRepository.updateProduct(product);
	}

	@Override
	public void deleteProduct(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new ValidationException("ID sản phẩm không được để trống");
		}
		
		// Check if product exists
		ProductDTO existingProduct = productRepository.getProductById(id);
		if (existingProduct == null) {
			throw new ResourceNotFoundException("Không tìm thấy sản phẩm với ID: " + id);
		}
		
		productRepository.deleteProduct(id);
	}
	
	/**
	 * Validate product request data
	 */
	private void validateProductRequest(ProductRequest productRequest) {
		if (productRequest.getName() == null || productRequest.getName().trim().isEmpty()) {
			throw new ValidationException("Tên sản phẩm không được để trống");
		}
		
		if (productRequest.getPrice() == null || productRequest.getPrice() <= 0) {
			throw new ValidationException("Giá sản phẩm phải lớn hơn 0");
		}
		
		if (productRequest.getStock() != null && productRequest.getStock() < 0) {
			throw new ValidationException("Số lượng tồn kho không được âm");
		}
		
		if (productRequest.getCategoryId() == null || productRequest.getCategoryId().trim().isEmpty()) {
			throw new ValidationException("Category ID không được để trống");
		}
	}

}
