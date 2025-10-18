# Hướng dẫn Test Order Service

## 1. Chuẩn bị môi trường

### Cài đặt Database
```bash
# Chạy script tạo database
mysql -u root -p < database_setup.sql
```

### Đảm bảo các service khác đang chạy
- Auth Service (port 8081)
- Product Service (port 8082)
- Consul (port 8500)

## 2. Build và Deploy

```bash
# Build project
mvn clean package

# Deploy file WAR trong thư mục target/ vào Tomcat
# Hoặc chạy với embedded server
```

## 3. Test các API endpoints

### 3.1. Health Check
```bash
curl -X GET http://localhost:8083/OrderService/api/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "service": "order-service",
  "timestamp": 1729234567890
}
```

### 3.2. Get All Orders
```bash
curl -X GET http://localhost:8083/OrderService/api/orders
```

### 3.3. Get Order by ID
```bash
curl -X GET http://localhost:8083/OrderService/api/orders/ORD001
```

### 3.4. Get Orders by Customer ID
```bash
curl -X GET http://localhost:8083/OrderService/api/orders/customer/user001
```

### 3.5. Create Order (Cần JWT Token)

**Bước 1: Lấy JWT Token từ Auth Service**
```bash
curl -X POST http://localhost:8081/AuthService/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123"
  }'
```

**Bước 2: Tạo Order với JWT Token**
```bash
curl -X POST http://localhost:8083/OrderService/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -d '{
    "customerId": "user001",
    "orderDetails": [
      {
        "productId": "prod001",
        "quantity": 1
      },
      {
        "productId": "prod004",
        "quantity": 2
      }
    ]
  }'
```

## 4. Test Scenarios

### Scenario 1: Tạo đơn hàng thành công
1. Login để lấy JWT token
2. Tạo order với sản phẩm có sẵn trong kho
3. Kiểm tra response có orderId mới
4. Verify tồn kho sản phẩm đã giảm

### Scenario 2: Tạo đơn hàng với sản phẩm hết hàng
1. Tạo order với quantity > stock
2. Expected: Error message "Insufficient stock for product"

### Scenario 3: Tạo đơn hàng không có JWT token
1. Gọi POST /api/orders mà không có Authorization header
2. Expected: 401 Unauthorized

### Scenario 4: Xem lịch sử đơn hàng của khách hàng
1. Gọi GET /api/orders/customer/{customerId}
2. Verify danh sách đơn hàng sắp xếp theo ngày giảm dần

## 5. Kiểm tra Database

```sql
-- Xem tất cả đơn hàng
SELECT * FROM orders ORDER BY order_date DESC;

-- Xem chi tiết đơn hàng
SELECT o.order_id, o.ma_kh, o.order_date, o.total_amount,
       od.product_id, od.quantity, od.price
FROM orders o
JOIN order_details od ON o.order_id = od.order_id
WHERE o.order_id = 'ORD001';

-- Xem đơn hàng theo khách hàng
SELECT * FROM orders WHERE ma_kh = 'user001';

-- Xem tổng doanh thu
SELECT SUM(total_amount) as total_revenue FROM orders;

-- Xem sản phẩm bán chạy nhất
SELECT product_id, SUM(quantity) as total_sold
FROM order_details
GROUP BY product_id
ORDER BY total_sold DESC;
```

## 6. Import Postman Collection

1. Mở Postman
2. Import file `Postman_Collection.json`
3. Tạo environment với variable:
   - `base_url`: http://localhost:8083/OrderService
   - `jwt_token`: (lấy từ Auth Service)
4. Chạy các request trong collection

## 7. Troubleshooting

### Lỗi: "Product service not available"
- Kiểm tra Product Service có đang chạy không
- Kiểm tra Consul có đang chạy không
- Verify service đã register với Consul

### Lỗi: "Auth service not available"
- Kiểm tra Auth Service có đang chạy không
- Kiểm tra cấu hình trong application.properties

### Lỗi: Database connection failed
- Kiểm tra MySQL có đang chạy không
- Verify thông tin kết nối trong db.properties
- Kiểm tra database đã được tạo chưa

### Lỗi: 401 Unauthorized
- Kiểm tra JWT token có hợp lệ không
- Kiểm tra token có đúng format "Bearer {token}" không
- Token có thể đã hết hạn, cần login lại

## 8. Monitoring

### Kiểm tra logs
```bash
# Xem Tomcat logs
tail -f catalina.out

# Xem logs của Order Service
grep "order" catalina.out
```

### Kiểm tra Consul
```bash
# Truy cập Consul UI
http://localhost:8500/ui

# Kiểm tra service health
http://localhost:8500/v1/health/service/order-service
```

## 9. Expected Behavior

### Khi tạo đơn hàng thành công:
1. Order được tạo với orderId duy nhất
2. Order details được lưu vào database
3. Tồn kho sản phẩm được cập nhật (giảm đi số lượng đã đặt)
4. Total amount được tính toán chính xác
5. Order date được set là ngày hiện tại

### Validation Rules:
- Customer ID không được rỗng
- Order details không được rỗng
- Quantity phải > 0
- Sản phẩm phải tồn tại trong Product Service
- Tồn kho phải đủ để đáp ứng đơn hàng

## 10. Performance Testing

### Test với nhiều orders đồng thời
```bash
# Sử dụng Apache Bench
ab -n 100 -c 10 -H "Authorization: Bearer YOUR_TOKEN" \
   -p order_data.json -T "application/json" \
   http://localhost:8083/OrderService/api/orders
```

### Expected Metrics:
- Response time < 500ms cho GET requests
- Response time < 1000ms cho POST requests
- Throughput > 100 requests/second
