package com.example.product.dao.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.product.dao.ProductDAO;
import com.example.product.dto.ProductDTO;
import com.example.product.mapper.ProductMapper;
import com.example.product.model.Product;

@Repository
public class MySqlProductDaoImpl implements ProductDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private ProductMapper productMapper;

	@Override
	public List<ProductDTO> getAllProducts() {
		String sql = "SELECT p.id, p.name, p.description, p.stock, p.price, c.name AS category_name "
				+ "FROM products p " + "JOIN categories c ON p.category_id = c.id";

		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			Product product = new Product();
			product.setId(rs.getString("id"));
			product.setName(rs.getString("name"));
			product.setDescription(rs.getString("description"));
			product.setStock(rs.getInt("stock"));
			product.setPrice(rs.getDouble("price"));
			// Assuming ProductDTO has a field for category name
			ProductDTO productDTO = productMapper.toDTO(product);
			productDTO.setCategoryName(rs.getString("category_name"));
			return productDTO;
		});
	}

	@Override
	public ProductDTO getProductById(String id) {
		String sql = "SELECT p.id, p.name, p.description, p.stock, p.price, c.name AS category_name "
				+ "FROM products p " + "JOIN categories c ON p.category_id = c.id " + "WHERE p.id = ?";
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
				Product product = new Product();
				product.setId(rs.getString("id"));
				product.setName(rs.getString("name"));
				product.setDescription(rs.getString("description"));
				product.setStock(rs.getInt("stock"));
				product.setPrice(rs.getDouble("price"));
				// Assuming ProductDTO has a field for category name
				ProductDTO productDTO = productMapper.toDTO(product);
				productDTO.setCategoryName(rs.getString("category_name"));
				return productDTO;
			});
		} catch (Exception e) {
			return null; // or throw a custom exception
		}
	}

	@Override
	public void addProduct(Product product) {
		String sql = "INSERT INTO products (id, name, description, price, stock, category_id) VALUES (?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, 
				product.getId(),
				product.getName(), 
				product.getDescription(), 
				product.getPrice(),
				product.getStock() != null ? product.getStock() : 0,
				product.getCategoryId());
	}

	@Override
	public void updateProduct(Product product) {
		String sql = "UPDATE products SET name = ?, description = ?, price = ?, stock = ?, category_id = ? WHERE id = ?";
		jdbcTemplate.update(sql, 
				product.getName(), 
				product.getDescription(), 
				product.getPrice(),
				product.getStock() != null ? product.getStock() : 0,
				product.getCategoryId(), 
				product.getId());
	}

	@Override
	public void deleteProduct(String id) {
		String sql = "DELETE FROM products WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public void updateProductStock(String id, int newStock) {
		String sql = "UPDATE products SET stock = ? WHERE id = ?";
		jdbcTemplate.update(sql, newStock, id);
	}

}
