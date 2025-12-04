# Mini Ecommerce UI

Giao diện đơn giản cho hệ thống Mini Ecommerce, được xây dựng bằng HTML, CSS, JavaScript và Bootstrap.

## Cấu trúc File

```
ui/
├── index.html      # Trang chính với layout chia đôi
├── styles.css      # CSS styling cho responsive và UI
├── api.js         # API utility functions
├── app.js         # Logic chính của ứng dụng
└── README.md      # Tài liệu này
```

## Tính năng

### 🛒 Giỏ hàng (bên trái - 20%)
- Hiển thị danh sách sản phẩm trong giỏ
- Điều khiển số lượng (+/-) với giới hạn kho
- Xóa sản phẩm khỏi giỏ
- Nút "Đặt hàng" khi có sản phẩm trong giỏ

### 📦 Sản phẩm (bên phải - 80%)
- Ô tìm kiếm sản phẩm theo tên
- Hiển thị sản phẩm dạng grid với thông tin:
  - Tên sản phẩm
  - Mô tả
  - Giá (VNĐ)
  - Số lượng tồn kho
- Nút "Thêm vào giỏ" với kiểm tra tồn kho
- Phân trang với thông tin tổng số sản phẩm

### 🔐 Xác thực (giả lập)
- Giả lập người dùng đăng nhập (userId = 1)
- Gửi userId trong header khi đặt hàng

## API Endpoints

### Sản phẩm
```
GET /api/products?page={page}&size={size}&keyword={keyword}
```
- `page`: Trang hiện tại (bắt đầu từ 0)
- `size`: Số sản phẩm mỗi trang
- `keyword`: Từ khóa tìm kiếm

### Đặt hàng
```
POST /api/orders
Headers: userId={userId}
Body: {
  "items": [
    {
      "productId": {id},
      "quantity": {số lượng}
    }
  ]
}
```

## Cách chạy

### 1. Khởi động Backend
```bash
# Chạy Spring Boot backend trên port 8080
./mvnw spring-boot:run
```

### 2. Chạy Frontend
Mở file `ui/index.html` trực tiếp trong trình duyệt hoặc sử dụng web server đơn giản:

```bash
# Python 3
python -m http.server 3000

# Node.js
npx http-server ui -p 3000

# PHP
php -S localhost:3000 -t ui
```

Sau đó truy cập: `http://localhost:3000`

### 3. Cấu hình Backend URL
Backend đã được cấu hình chạy trên port 8081, UI đã được cập nhật tương ứng:
```javascript
BASE_URL: 'http://localhost:8081' // Đã được cấu hình đúng
```

**Lưu ý CORS**: Backend đã được cấu hình để cho phép frontend từ `localhost:*` gọi API, nên không cần lo lắng về lỗi CORS.

## Giao diện Responsive

- **Desktop**: Layout chia đôi 20:80
- **Mobile**: Layout xếp chồng, giỏ hàng ở dưới

## Mở rộng

Giao diện được thiết kế đơn giản và dễ mở rộng cho các bước tiếp theo:

1. **Thanh toán**: Thêm form thanh toán sau khi đặt hàng
2. **Địa chỉ giao hàng**: Thêm form nhập địa chỉ
3. **Lịch sử đơn hàng**: Trang xem đơn hàng đã đặt
4. **Đăng nhập/Đăng ký**: Thay thế giả lập bằng form thật
5. **Toast notifications**: Thay alert bằng toast đẹp mắt
6. **Loading states**: Thêm skeleton loading cho UX tốt hơn

## Demo Workflow

1. **Tìm sản phẩm**: Nhập từ khóa vào ô search
2. **Thêm vào giỏ**: Click "Thêm vào giỏ" trên sản phẩm
3. **Điều chỉnh số lượng**: Sử dụng +/- trong giỏ hàng
4. **Đặt hàng**: Click "Đặt hàng" để tạo đơn hàng

Dữ liệu sẽ được gửi về backend theo đúng API endpoints đã phân tích.
