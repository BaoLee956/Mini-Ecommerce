# Design Patterns Implementation in Order Processing Module

## Overview

This document describes the implementation of 4 design patterns in the Mini-Ecommerce order processing module:

1. **State Pattern** - Order status management
2. **Strategy Pattern** - Different order processing types
3. **Template Method Pattern** - Order workflow
4. **Decorator Pattern** - Order enhancements

## 1. State Pattern - Order Status Management

### Purpose
Manages complex order state transitions with validation rules for each state.

### Implementation

#### Core Components
- `OrderState` interface - defines state behavior
- `OrderStateContext` - manages state transitions
- Concrete states: `PendingOrderState`, `PaidOrderState`, `ShippedOrderState`, `CompletedOrderState`, `CancelledOrderState`

#### Key Features
- **State Validation**: Each state defines valid transitions
- **Action Execution**: State-specific actions (pay, ship, cancel, complete)
- **Error Prevention**: Invalid transitions throw exceptions

#### Usage Example
```java
// Transition order to PAID status
orderStateContext.transitionTo(order, OrderStatus.PAID);

// Execute payment action
orderStateContext.executeAction(order, OrderStateContext.OrderAction.PAY);
```

## 2. Strategy Pattern - Order Processing Strategies

### Purpose
Allows different processing logic for different types of orders (standard, wholesale, express).

### Implementation

#### Core Components
- `OrderProcessingStrategy` interface
- `OrderProcessingStrategyFactory` - selects appropriate strategy
- Concrete strategies: `StandardOrderStrategy`, `WholesaleOrderStrategy`, `ExpressOrderStrategy`

#### Strategy Types
- **StandardOrderStrategy**: Regular orders with basic discounts
- **WholesaleOrderStrategy**: Bulk orders with volume discounts
- **ExpressOrderStrategy**: Fast delivery with premium pricing

#### Usage Example
```java
// Factory selects appropriate strategy based on order
OrderProcessingStrategy strategy = strategyFactory.getStrategy(request);

// Apply strategy-specific logic
BigDecimal total = strategy.calculateTotal(order);
strategy.applyDiscounts(order);
```

## 3. Template Method Pattern - Order Processing Workflow

### Purpose
Defines a skeleton algorithm for order processing while allowing subclasses to customize specific steps.

### Implementation

#### Core Components
- `OrderProcessor` abstract class - defines template method
- `StandardOrderProcessor` concrete implementation
- Hook methods for customization

#### Workflow Steps
1. Validate request
2. Check inventory
3. Create order (customizable)
4. Calculate total (customizable)
5. Apply discounts (customizable)
6. Process special items (customizable)
7. Save order (customizable)
8. Send notifications

#### Usage Example
```java
// Process order using template method
Order order = orderProcessor.processOrder(request);
```

## 4. Decorator Pattern - Order Enhancements

### Purpose
Adds optional features to orders dynamically without modifying the core Order class.

### Implementation

#### Core Components
- `OrderDecorator` interface
- `OrderDecoratorManager` - manages decorator application
- Concrete decorators: `GiftWrappingDecorator`, `ExpressShippingDecorator`, `InsuranceDecorator`

#### Available Decorators
- **Gift Wrapping**: Adds $3.00 for gift packaging
- **Express Shipping**: Adds $10.00 for fast delivery
- **Insurance**: Adds 2% of order value for insurance

#### Usage Example
```java
// Apply multiple decorators
List<String> decorators = List.of("Gift Wrapping", "Express Shipping");
decoratorManager.applyDecorators(order, decorators);

// Calculate decorator costs
BigDecimal cost = decoratorManager.calculateTotalAdditionalCost(order, decorators);
```

## Integration with OrderService

### Enhanced OrderServiceImpl

The `OrderServiceImpl` now integrates all patterns:

```java
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderStateContext orderStateContext;
    private final StandardOrderProcessor orderProcessor;
    private final OrderDecoratorManager decoratorManager;

    // Enhanced createOrder uses Template Method + Strategy
    public OrderResponse createOrder(Long userId, CreateOrderRequest request) {
        Order order = orderProcessor.processOrder(request);
        return mapToResponse(order);
    }

    // New method with Decorator Pattern
    public OrderResponse createOrderWithDecorators(Long userId, CreateOrderRequest request, List<String> decorators) {
        Order order = orderProcessor.processOrder(request);
        decoratorManager.applyDecorators(order, decorators);
        return mapToResponse(order);
    }

    // Enhanced status update uses State Pattern
    public OrderStatusResponse updateStatus(Long id, UpdateOrderStatusRequest request, Long userId) {
        // ... validation ...
        orderStateContext.transitionTo(order, request.status());
        return response;
    }
}
```

## New API Endpoints

### Decorator Pattern Endpoints
- `POST /api/orders/with-decorators` - Create order with decorators
- `POST /api/orders/{id}/decorators` - Apply decorator to existing order
- `GET /api/orders/decorators` - Get available decorators
- `GET /api/orders/{id}/decorators/{name}/cost` - Calculate decorator cost
- `GET /api/orders/{id}/decorators/{name}/can-apply` - Check if decorator can be applied

## Benefits Achieved

### 1. **State Pattern Benefits**
- ✅ Prevents invalid status transitions
- ✅ Encapsulates state-specific logic
- ✅ Easy to add new states

### 2. **Strategy Pattern Benefits**
- ✅ Different pricing/discount logic for different order types
- ✅ Easy to add new order types
- ✅ Strategy selection based on order characteristics

### 3. **Template Method Pattern Benefits**
- ✅ Consistent order processing workflow
- ✅ Extensible processing steps
- ✅ Separation of common vs. specific logic

### 4. **Decorator Pattern Benefits**
- ✅ Optional features without core class modification
- ✅ Dynamic feature combination
- ✅ Cost calculation for optional features

## SOLID Principles Compliance

- **SRP**: Each pattern handles one responsibility
- **OCP**: New states/strategies/decorators can be added without modifying existing code
- **LSP**: All implementations are interchangeable
- **ISP**: Clients depend only on methods they use
- **DIP**: High-level modules don't depend on low-level details

## Testing the Implementation

### Sample API Calls

```bash
# Create order with decorators
POST /api/orders/with-decorators
{
  "orderRequest": {
    "items": [
      {"productId": 1, "quantity": 2}
    ]
  },
  "decorators": ["Gift Wrapping", "Express Shipping"]
}

# Check decorator cost
GET /api/orders/123/decorators/Gift%20Wrapping/cost

# Update order status (uses State Pattern)
PUT /api/orders/123/status
{
  "status": "PAID"
}
```

## Future Extensions

1. **State Pattern**: Add more order states (REFUNDED, ON_HOLD)
2. **Strategy Pattern**: Add B2B, International, Seasonal strategies
3. **Template Method**: Add different processors for different order sources
4. **Decorator Pattern**: Add Loyalty points, Coupons, Installation services

This implementation demonstrates how design patterns work together to create a flexible, maintainable, and extensible order processing system.
