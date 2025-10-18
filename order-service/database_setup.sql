-- ============================================
-- Order Service Database Schema
-- ============================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS `order-service` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `order-service`;

-- ============================================
-- Orders Table
-- ============================================
CREATE TABLE IF NOT EXISTS orders (
    order_id VARCHAR(50) PRIMARY KEY,
    ma_kh VARCHAR(50) NOT NULL,
    order_date DATE NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_ma_kh (ma_kh),
    INDEX idx_order_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Order Details Table
-- ============================================
CREATE TABLE IF NOT EXISTS order_details (
    order_id VARCHAR(50) NOT NULL,
    product_id VARCHAR(50) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price DECIMAL(15,2) NOT NULL CHECK (price >= 0),
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data - Orders
-- ============================================
INSERT INTO orders (order_id, ma_kh, order_date, total_amount) VALUES
('ORD001', 'user001', '2025-10-01', 60000000.00),
('ORD002', 'user002', '2025-10-05', 600000.00),
('ORD003', 'user001', '2025-10-10', 1200000.00)
ON DUPLICATE KEY UPDATE 
    ma_kh = VALUES(ma_kh),
    order_date = VALUES(order_date),
    total_amount = VALUES(total_amount);

-- ============================================
-- Sample Data - Order Details
-- ============================================
INSERT INTO order_details (order_id, product_id, quantity, price) VALUES
('ORD001', 'prod001', 1, 25000000.00),
('ORD001', 'prod002', 1, 35000000.00),
('ORD002', 'prod004', 4, 150000.00),
('ORD003', 'prod007', 1, 1200000.00)
ON DUPLICATE KEY UPDATE 
    quantity = VALUES(quantity),
    price = VALUES(price);
