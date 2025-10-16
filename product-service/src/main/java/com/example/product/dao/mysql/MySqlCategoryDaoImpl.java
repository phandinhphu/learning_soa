package com.example.product.dao.mysql;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.product.dao.CategoryDAO;
import com.example.product.dto.CategoryDTO;
import com.example.product.model.Category;

@Repository
public class MySqlCategoryDaoImpl implements CategoryDAO {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<CategoryDTO> getAllCategories() {
		String sql = "SELECT id, name, description FROM categories";
		
		return jdbcTemplate.query(sql, (rs, rowNum) -> {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setId(rs.getString("id"));
			categoryDTO.setName(rs.getString("name"));
			categoryDTO.setDescription(rs.getString("description"));
			return categoryDTO;
		});
	}

	@Override
	public CategoryDTO getCategoryById(String id) {
		String sql = "SELECT id, name, description FROM categories WHERE id = ?";
		
		try {
			return jdbcTemplate.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {
				CategoryDTO categoryDTO = new CategoryDTO();
				categoryDTO.setId(rs.getString("id"));
				categoryDTO.setName(rs.getString("name"));
				categoryDTO.setDescription(rs.getString("description"));
				return categoryDTO;
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public void addCategory(Category category) {
		String sql = "INSERT INTO categories (id, name, description) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, 
				category.getId(),
				category.getName(), 
				category.getDescription());
	}

	@Override
	public void updateCategory(Category category) {
		String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
		jdbcTemplate.update(sql, 
				category.getName(), 
				category.getDescription(),
				category.getId());
	}

	@Override
	public void deleteCategory(String id) {
		String sql = "DELETE FROM categories WHERE id = ?";
		jdbcTemplate.update(sql, id);
	}

	@Override
	public boolean existsById(String id) {
		String sql = "SELECT COUNT(*) FROM categories WHERE id = ?";
		Integer count = jdbcTemplate.queryForObject(sql, new Object[] { id }, Integer.class);
		return count != null && count > 0;
	}
}
