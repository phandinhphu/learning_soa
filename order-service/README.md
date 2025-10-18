# Order Service

Service quản lý đơn hàng trong hệ thống Shopping SOA.

## Các tính năng chính

1. **Tạo đơn hàng mới** - Tự động tính toán tổng tiền và cập nhật tồn kho sản phẩm
2. **Xem chi tiết đơn hàng** - Lấy thông tin chi tiết của một đơn hàng
3. **Xem đơn hàng theo khách hàng** - Lấy danh sách tất cả đơn hàng của một khách hàng
4. **Xem tất cả đơn hàng** - Lấy danh sách tất cả các đơn hàng trong hệ thống

## Cấu trúc Database

### Bảng `orders`
- `order_id` (VARCHAR, PRIMARY KEY) - Mã đơn hàng
- `ma_kh` (VARCHAR) - Mã khách hàng
- `order_date` (DATE) - Ngày đặt hàng
- `total_amount` (DECIMAL) - Tổng tiền đơn hàng

### Bảng `order_details`
- `order_id` (VARCHAR, FOREIGN KEY) - Mã đơn hàng
- `product_id` (VARCHAR) - Mã sản phẩm
- `quantity` (INT) - Số lượng
- `price` (DECIMAL) - Giá tại thời điểm đặt hàng

## API Endpoints

### 1. Health Check
```
GET /api/health
```
Kiểm tra trạng thái service.

**Response:**
```json
{
  "status": "UP",
  "service": "order-service",
  "timestamp": 1729234567890
}
```

### 2. Tạo đơn hàng mới
```
POST /api/orders
```
**Yêu cầu JWT Authentication**

**Request Body:**
```json
{
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
}
```

**Response:**
```json
{
  "success": true,
  "message": "Tạo đơn hàng thành công",
  "data": {
    "orderId": "ORD12345678",
    "customerId": "user001",
    "orderDate": "2025-10-18",
    "totalAmount": 25300000.0,
    "orderDetails": [
      {
        "orderId": "ORD12345678",
        "productId": "prod001",
        "quantity": 1,
        "price": 25000000.0
      },
      {
        "orderId": "ORD12345678",
        "productId": "prod004",
        "quantity": 2,
        "price": 150000.0
      }
    ]
  }
}
```

### 3. Lấy chi tiết đơn hàng
```
GET /api/orders/{orderId}
```

**Response:**
```json
{
  "success": true,
  "message": "Lấy thông tin đơn hàng thành công",
  "data": {
    "orderId": "ORD001",
    "customerId": "user001",
    "orderDate": "2025-10-01",
    "totalAmount": 60000000.0,
    "orderDetails": [
      {
        "orderId": "ORD001",
        "productId": "prod001",
        "quantity": 1,
        "price": 25000000.0
      }
    ]
  }
}
```

### 4. Lấy đơn hàng theo khách hàng
```
GET /api/orders/customer/{customerId}
```

**Response:**
```json
{
  "success": true,
  "message": "Lấy danh sách đơn hàng thành công",
  "data": [
    {
      "orderId": "ORD003",
      "customerId": "user001",
      "orderDate": "2025-10-10",
      "totalAmount": 1200000.0,
      "orderDetails": [...]
    },
    {
      "orderId": "ORD001",
      "customerId": "user001",
      "orderDate": "2025-10-01",
      "totalAmount": 60000000.0,
      "orderDetails": [...]
    }
  ]
}
```

### 5. Lấy tất cả đơn hàng
```
GET /api/orders
```

**Response:**
```json
{
  "success": true,
  "message": "Lấy danh sách tất cả đơn hàng thành công",
  "data": [
    {
      "orderId": "ORD003",
      "customerId": "user001",
      "orderDate": "2025-10-10",
      "totalAmount": 1200000.0,
      "orderDetails": [...]
    },
    ...
  ]
}
```

## Tích hợp với các service khác

### Product Service
Order Service gọi đến Product Service để:
- Lấy thông tin sản phẩm (giá, tồn kho)
- Cập nhật số lượng tồn kho sau khi tạo đơn hàng

### Auth Service
Order Service sử dụng Auth Service để:
- Xác thực JWT token khi tạo đơn hàng mới

## Cấu hình

File `application.properties`:
```properties
service.name=order-service
service.contextPath=/OrderService
service.port=8083
service.healthCheckPath=/api/health

consul.auth.contextPath=/AuthService
consul.product.contextPath=/ProductService
consul.host=localhost
consul.port=8500
```

File `db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/order-service
db.username=root
db.password=
db.driverClassName=com.mysql.cj.jdbc.Driver
```

## Cài đặt và chạy

1. Tạo database:
```bash
mysql -u root -p < database_setup.sql
```

2. Build project:
```bash
mvn clean package
```

3. Deploy file WAR vào Tomcat hoặc chạy với embedded server.

4. Service sẽ chạy tại: `http://localhost:8083/OrderService`

## Dependencies

- Spring Framework 5.3.x
- MySQL Connector
- Jackson (JSON processing)
- Consul Client (Service Discovery)
- javax.servlet-api

## Lưu ý

- Endpoint `POST /api/orders` yêu cầu JWT token hợp lệ
- Khi tạo đơn hàng, service sẽ tự động:
  - Kiểm tra tồn kho sản phẩm
  - Tính toán tổng tiền đơn hàng
  - Cập nhật tồn kho sản phẩm
  - Lưu giá sản phẩm tại thời điểm đặt hàng
