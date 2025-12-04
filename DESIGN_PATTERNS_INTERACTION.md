# Design Patterns Interaction in Order Processing Module

## Tổng Quan

Tài liệu này mô tả cách **4 design patterns** (State, Strategy, Template Method, Decorator) phối hợp chặt chẽ với nhau để tạo nên một hệ thống xử lý đơn hàng linh hoạt, dễ bảo trì và mở rộng trong Mini-Ecommerce project.

## Luồng Xử Lý Đơn Hàng Chính

```java
POST /api/orders/with-decorators
{
  "orderRequest": {"items": [...]},
  "decorators": ["Gift Wrapping", "Express Shipping"]
}
```

## Sơ Đồ Phối Hợp

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   OrderService  │────│ Template Method  │────│  Strategy       │
│                 │    │   (Workflow)     │    │  (Processing    │
│ - createOrder() │    │                  │    │   Logic)        │
│ - updateStatus()│    │ OrderProcessor   │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
         │                       │                       │
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   State         │    │  Decorator       │    │  Strategy       │
│   (Status       │────│  (Enhancements)  │────│  Factory        │
│    Management)  │    │                  │    │                 │
│ OrderStateContext│    │OrderDecoratorMgr│    │OrderProcessing │
└─────────────────┘    └──────────────────┘    │StrategyFactory  │
                                               └─────────────────┘
```

## Chi Tiết Phối Hợp Theo Bước

### 1. Tạo Đơn Hàng (Template Method + Strategy + Decorator)

```java
// OrderServiceImpl.createOrderWithDecorators()
public OrderResponse createOrderWithDecorators(Long userId, CreateOrderRequest request, List<String> decorators) {
    // 1️⃣ TEMPLATE METHOD: Sử dụng workflow chuẩn hóa
    Order order = orderProcessor.processOrder(request);
    order.setUserId(userId);

    // 2️⃣ DECORATOR: Áp dụng các tính năng bổ sung
    if (decorators != null && !decorators.isEmpty()) {
        decoratorManager.applyDecorators(order, decorators);
        order = orderRepository.save(order);
    }

    return mapToResponse(order);
}
```

**Trong Template Method (OrderProcessor.processOrder()):**
```java
public final Order processOrder(CreateOrderRequest request) {
    // Bước 1: Validate (abstract - subclass implement)
    validateRequest(request);

    // Bước 2: Check inventory (abstract - subclass implement)
    checkInventory(request);

    // Bước 3: STRATEGY - Chọn chiến lược xử lý phù hợp
    OrderProcessingStrategy strategy = strategyFactory.getStrategy(request);
    Order order = createOrder(request, strategy);

    // Bước 4-6: Sử dụng Strategy để xử lý
    calculateTotal(order, strategy);      // Hook method
    applyDiscounts(order, strategy);      // Hook method
    processSpecialItems(order, strategy); // Hook method

    // Bước 7-8: Save & Notify (abstract + hook)
    saveOrder(order);
    sendNotifications(order);

    return order;
}
```

### 2. Cập Nhật Trạng Thái (State Pattern)

```java
// OrderServiceImpl.updateStatus()
public OrderStatusResponse updateStatus(Long id, UpdateOrderStatusRequest request, Long userId) {
    Order order = orderRepository.findById(id).orElseThrow(/*...*/);

    // STATE PATTERN: Quản lý transitions và validation
    try {
        orderStateContext.transitionTo(order, request.status());
        order = orderRepository.save(order);

        // Thực hiện actions theo trạng thái mới
        if (request.status() == OrderStatus.PAID) {
            orderStateContext.executeAction(order, OrderStateContext.OrderAction.PAY);
        }

    } catch (IllegalStateException e) {
        throw new RuntimeException("Invalid status transition: " + e.getMessage());
    }

    return new OrderStatusResponse(order.getId(), order.getStatus(), LocalDateTime.now());
}
```

## Cách Các Patterns Phối Hợp

### Template Method ↔ Strategy Pattern

**Mối quan hệ:** Template Method định nghĩa skeleton workflow, Strategy cung cấp implementation chi tiết

**Cách thức phối hợp:**
- Template Method có workflow 8 bước chuẩn hóa
- Strategy Pattern cung cấp logic xử lý khác nhau cho từng bước
- Factory Pattern chọn Strategy phù hợp dựa trên request

**Ví dụ code:**
```java
// Trong Template Method
OrderProcessingStrategy strategy = strategyFactory.getStrategy(request);
calculateTotal(order, strategy);    // Template gọi Strategy
applyDiscounts(order, strategy);    // Template gọi Strategy
```

**Lợi ích:**
- Workflow nhất quán nhưng linh hoạt
- Dễ thêm Strategy mới mà không sửa Template
- Separation of concerns rõ ràng

### Template Method ↔ Decorator Pattern

**Mối quan hệ:** Template Method xử lý core logic, Decorator thêm optional features

**Cách thức phối hợp:**
- Template Method tạo ra order object hoàn chỉnh
- Decorator Pattern thêm các tính năng bổ sung (gift wrapping, shipping, insurance)
- Decorator được áp dụng sau khi Template Method hoàn thành

**Ví dụ code:**
```java
// Template tạo core order
Order order = orderProcessor.processOrder(request);

// Decorator thêm features
decoratorManager.applyDecorators(order, decorators);
```

**Lợi ích:**
- Tách biệt core business logic với optional features
- Features có thể kết hợp linh hoạt
- Không ảnh hưởng đến core workflow

### State Pattern ↔ Template Method

**Mối quan hệ:** State quản lý lifecycle, Template xử lý creation workflow

**Cách thức phối hợp:**
- Template Method tạo order với trạng thái ban đầu
- State Pattern quản lý việc chuyển đổi trạng thái suốt vòng đời order
- State validation đảm bảo transitions hợp lệ

**Ví dụ code:**
```java
// Template tạo order với status PENDING
Order order = orderProcessor.processOrder(request);

// State quản lý transitions
orderStateContext.transitionTo(order, OrderStatus.PAID);
orderStateContext.executeAction(order, OrderAction.PAY);
```

**Lợi ích:**
- Hoàn thiện vòng đời order từ tạo đến hoàn thành
- Validation chặt chẽ các trạng thái
- State-specific actions

### Strategy Pattern ↔ Decorator Pattern

**Mối quan hệ:** Strategy xử lý business logic khác nhau, Decorator thêm features chung

**Cách thức phối hợp:**
- Strategy Pattern xử lý các loại order khác nhau (standard, wholesale, express)
- Decorator Pattern thêm các features giống nhau cho tất cả strategies
- Kết hợp linh hoạt: bất kỳ strategy nào cũng có thể có decorators

**Ví dụ:**
- WholesaleOrderStrategy (giảm 15%) + GiftWrappingDecorator (+$3)
- StandardOrderStrategy (giảm 5%) + ExpressShippingDecorator (+$10)

**Lợi ích:**
- Business logic + features độc lập nhau
- Kết hợp bất kỳ strategy với bất kỳ decorator
- Extensible cho cả hai chiều

## Luồng Dữ Liệu Hoàn Chỉnh

```
1. Client Request ──► OrderController
2. OrderController ──► OrderService.createOrderWithDecorators()
3. OrderService ──► StandardOrderProcessor.processOrder() [TEMPLATE METHOD]
4. OrderProcessor ──► OrderProcessingStrategyFactory.getStrategy() [STRATEGY SELECTION]
5. OrderProcessor ──► Strategy.calculateTotal(), applyDiscounts() [STRATEGY EXECUTION]
6. OrderService ──► OrderDecoratorManager.applyDecorators() [DECORATOR]
7. OrderService ──► OrderStateContext.transitionTo() [STATE - Initial Status]
8. Response ──► Client

Later Updates:
9. Client ──► OrderController.updateStatus()
10. OrderController ──► OrderService.updateStatus()
11. OrderService ──► OrderStateContext.transitionTo() [STATE MANAGEMENT]
12. OrderStateContext ──► ConcreteState.handlePayment() etc. [STATE ACTIONS]
```

## Ví Dụ Thực Tế

### Đơn Hàng Bán Buôn Với Gói Quà và Giao Nhanh

**Request:**
```json
POST /api/orders/with-decorators
{
  "orderRequest": {
    "items": [
      {"productId": 1, "quantity": 15}
    ]
  },
  "decorators": ["Gift Wrapping", "Express Shipping"]
}
```

**Quy trình xử lý:**

1. **Strategy Pattern**: Phát hiện 15 items → chọn `WholesaleOrderStrategy`
2. **Template Method**: Thực hiện workflow 8 bước với Wholesale strategy
3. **Decorator Pattern**: Áp dụng Gift Wrapping (+$3) và Express Shipping (+$10)
4. **State Pattern**: Đặt trạng thái ban đầu là PENDING

**Kết quả:**
- Giảm giá 15% (Wholesale)
- Phí ship $2 (thay vì $5 standard)
- Gift wrapping +$3
- Express shipping +$10
- Tổng cộng: (giá gốc - 15%) + $2 + $3 + $10

## Lợi Ích Của Sự Phối Hợp

### 1. Separation of Concerns
- **Template Method**: Workflow orchestration
- **Strategy**: Business logic variations
- **State**: Lifecycle management
- **Decorator**: Feature extensions

### 2. Flexibility & Extensibility
```java
// Dễ dàng thêm strategy mới
@Service
public class PremiumOrderStrategy implements OrderProcessingStrategy {
    // Logic cho đơn hàng premium
}

// Dễ dàng thêm decorator mới
@Service
public class LoyaltyPointsDecorator implements OrderDecorator {
    // Thêm tính năng tích điểm
}

// Dễ dàng thêm state mới
@Component
public class RefundedOrderState implements OrderState {
    // Xử lý đơn hàng hoàn tiền
}
```

### 3. Testability
- Mỗi pattern test độc lập
- Mock strategy trong Template Method testing
- Test state transitions riêng biệt
- Verify decorator applications

### 4. Maintainability
- Thay đổi logic ở một nơi không ảnh hưởng nơi khác
- Code reuse cao
- Clear responsibilities
- SOLID principles compliance

## SOLID Principles Compliance

### Single Responsibility Principle (SRP)
- Mỗi pattern chịu trách nhiệm riêng biệt
- Template Method: workflow orchestration
- Strategy: business logic variations
- State: status management
- Decorator: feature additions

### Open-Closed Principle (OCP)
- Thêm strategy mới: chỉ tạo class mới implement interface
- Thêm decorator mới: chỉ tạo class mới implement interface
- Thêm state mới: chỉ tạo class mới implement interface
- Code cũ không cần sửa đổi

### Liskov Substitution Principle (LSP)
- Tất cả implementations đều có thể thay thế lẫn nhau
- OrderProcessingStrategy implementations interchangeable
- OrderDecorator implementations interchangeable
- OrderState implementations interchangeable

### Interface Segregation Principle (ISP)
- Interfaces nhỏ, focused
- Clients chỉ phụ thuộc vào methods họ cần
- Không có "fat interfaces"

### Dependency Inversion Principle (DIP)
- High-level modules không phụ thuộc low-level modules
- Cả hai phụ thuộc vào abstractions
- Spring IoC container quản lý dependencies

## Kết Luận

Sự phối hợp giữa 4 design patterns tạo nên một kiến trúc **modular, flexible, maintainable, và scalable**:

- **Template Method** cung cấp **framework** xử lý đơn hàng
- **Strategy** cung cấp **variations** trong logic xử lý
- **Decorator** cung cấp **extensions** cho tính năng
- **State** cung cấp **lifecycle management**

Kết hợp lại, chúng tạo nên một hệ thống có thể xử lý đa dạng loại đơn hàng với nhiều tùy chọn, đồng thời đảm bảo tính nhất quán, bảo trì dễ dàng và mở rộng thuận tiện.

Đây là một ví dụ điển hình về việc áp dụng design patterns một cách hiệu quả trong thực tế! 🎯
