-- ============================================
-- Auth Service Database Schema
-- ============================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS `auth-service` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `auth-service`;

-- ============================================
-- Users Table (Authentication)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(50) PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Sample Data - Users
-- ============================================
-- Password: admin123 (hashed with BCrypt)
INSERT INTO users (id, email, password) VALUES
('user001', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('user002', 'user1@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('user003', 'user2@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('user004', 'john.doe@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'),
('user005', 'jane.smith@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy')
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    password = VALUES(password);

-- ============================================
-- Useful Queries for Testing
-- ============================================

-- Get all users
-- SELECT id, email, created_at FROM users;

-- Check if email exists
-- SELECT * FROM users WHERE email = 'admin@example.com';

-- Count total users
-- SELECT COUNT(*) as total_users FROM users;
