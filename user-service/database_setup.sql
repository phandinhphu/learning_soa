-- ============================================
-- User Service Database Schema
-- ============================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS `user-service` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `user-service`;

-- ============================================
-- Users Table (User Profile)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data - Users
-- ============================================
INSERT INTO users (id, username, email, phone) VALUES
('user001', 'Admin User', 'admin@example.com', '0901234567'),
('user002', 'Nguyen Van A', 'user1@example.com', '0912345678'),
('user003', 'Tran Thi B', 'user2@example.com', '0923456789'),
('user004', 'John Doe', 'john.doe@example.com', '0934567890'),
('user005', 'Jane Smith', 'jane.smith@example.com', '0945678901'),
('user006', 'Le Van C', 'levanc@example.com', '0956789012'),
('user007', 'Pham Thi D', 'phamthid@example.com', '0967890123'),
('user008', 'Hoang Van E', 'hoangvane@example.com', '0978901234'),
('user009', 'Vo Thi F', 'vothif@example.com', '0989012345'),
('user010', 'Dang Van G', 'dangvang@example.com', '0990123456')
ON DUPLICATE KEY UPDATE 
    username = VALUES(username),
    email = VALUES(email),
    phone = VALUES(phone);

-- ============================================
-- Useful Queries for Testing
-- ============================================

-- Get all users
-- SELECT * FROM users;

-- Search users by keyword
-- SELECT * FROM users WHERE username LIKE '%nguyen%' OR email LIKE '%nguyen%' OR phone LIKE '%nguyen%';

-- Get user by email
-- SELECT * FROM users WHERE email = 'admin@example.com';

-- Count total users
-- SELECT COUNT(*) as total_users FROM users;
