-- ============================================
-- Product Service Database Schema
-- ============================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS product_service CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE product_service;

-- ============================================
-- Categories Table
-- ============================================
CREATE TABLE IF NOT EXISTS categories (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    INDEX idx_name (name),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Products Table
-- ============================================
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(15,2) NOT NULL CHECK (price >= 0),
    stock INT DEFAULT 0 CHECK (stock >= 0),
    category_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL,
    INDEX idx_name (name),
    INDEX idx_category (category_id),
    INDEX idx_price (price),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data - Categories
-- ============================================
INSERT INTO categories (id, name, description) VALUES
('cat001', 'Electronics', 'Thiết bị điện tử và công nghệ'),
('cat002', 'Clothing', 'Quần áo và thời trang'),
('cat003', 'Books', 'Sách và tài liệu'),
('cat004', 'Home & Kitchen', 'Đồ gia dụng và nhà bếp'),
('cat005', 'Sports', 'Thể thao và dụng cụ tập luyện')
ON DUPLICATE KEY UPDATE 
    name = VALUES(name),
    description = VALUES(description);

-- ============================================
-- Sample Data - Products
-- ============================================
INSERT INTO products (id, name, description, price, stock, category_id) VALUES
('prod001', 'Laptop Dell XPS 13', 'Laptop cao cấp với màn hình 13 inch, CPU Intel Core i7', 25000000.00, 15, 'cat001'),
('prod002', 'iPhone 15 Pro Max', 'Điện thoại thông minh cao cấp từ Apple', 35000000.00, 20, 'cat001'),
('prod003', 'Samsung Galaxy S24 Ultra', 'Smartphone Android flagship', 28000000.00, 25, 'cat001'),
('prod004', 'Áo thun nam basic', 'Áo thun cotton 100% thoáng mát', 150000.00, 100, 'cat002'),
('prod005', 'Quần jeans nữ', 'Quần jeans skinny fit chất lượng cao', 450000.00, 50, 'cat002'),
('prod006', 'Sách Đắc Nhân Tâm', 'Sách self-help kinh điển', 85000.00, 200, 'cat003'),
('prod007', 'Bộ nồi inox cao cấp', 'Bộ nồi 5 món chất liệu inox 304', 1200000.00, 30, 'cat004'),
('prod008', 'Máy xay sinh tố', 'Máy xay công suất 500W', 850000.00, 40, 'cat004'),
('prod009', 'Giày chạy bộ Nike', 'Giày thể thao chuyên dụng chạy bộ', 2500000.00, 35, 'cat005'),
('prod010', 'Bóng đá Adidas', 'Bóng đá size 5 chính hãng', 350000.00, 60, 'cat005')
ON DUPLICATE KEY UPDATE 
    name = VALUES(name),
    description = VALUES(description),
    price = VALUES(price),
    stock = VALUES(stock),
    category_id = VALUES(category_id);

-- ============================================
-- Useful Queries for Testing
-- ============================================

-- Get all products with category names
-- SELECT p.id, p.name, p.description, p.price, p.stock, c.name AS category_name
-- FROM products p
-- JOIN categories c ON p.category_id = c.id
-- WHERE p.deleted = FALSE;

-- Get products by category
-- SELECT * FROM products WHERE category_id = 'cat001' AND deleted = FALSE;

-- Get product count by category
-- SELECT c.name, COUNT(p.id) as product_count
-- FROM categories c
-- LEFT JOIN products p ON c.id = p.category_id AND p.deleted = FALSE
-- WHERE c.deleted = FALSE
-- GROUP BY c.id, c.name;

-- Get low stock products (< 10)
-- SELECT * FROM products WHERE stock < 10 AND deleted = FALSE;

-- Search products by name
-- SELECT * FROM products WHERE name LIKE '%laptop%' AND deleted = FALSE;

COMMIT;
