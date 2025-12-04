//package org.example.miniecommerce.service;
//
//import org.example.miniecommerce.dto.order.*;
//import org.example.miniecommerce.entity.Order;
//import org.example.miniecommerce.entity.OrderItem;
//import org.example.miniecommerce.entity.OrderStatus;
//import org.example.miniecommerce.factory.OrderFactory;
//import org.example.miniecommerce.repository.OrderRepository;
//import org.example.miniecommerce.service.order.OrderServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceImplTest {
//
//    @Mock
//    private OrderRepository orderRepository;
//
//    @Mock
//    private OrderFactory orderFactory;
//
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    private Order sampleOrder;
//    private Long userId = 1L;
//    private Long orderId = 100L;
//
//    @BeforeEach
//    void setUp() {
//        sampleOrder = new Order();
//        sampleOrder.setId(orderId);
//        sampleOrder.setUserId(userId);
//        sampleOrder.setStatus(OrderStatus.CREATED);
//        sampleOrder.setTotalAmount(new BigDecimal("200.00"));
//        sampleOrder.setCreatedAt(LocalDateTime.now());
//
//        OrderItem item = new OrderItem();
//        item.setProductId(1L);
//        item.setQuantity(2);
//        item.setPrice(new BigDecimal("100.00"));
//        item.setOrder(sampleOrder);
//
//        List<OrderItem> items = new ArrayList<>();
//        items.add(item);
//        sampleOrder.setItems(items);
//    }
//
//    @Test
//    void createOrder_ShouldReturnOrderResponse() {
//        // Arrange
//        CreateOrderRequest request = new CreateOrderRequest(
//                List.of(new CreateOrderItemDto(1L, 2))
//        );
//
//        when(orderFactory.createOrder(eq(userId), anyList())).thenReturn(sampleOrder);
//        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);
//
//        // Act
//        OrderResponse response = orderService.createOrder(userId, request);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(orderId, response.id());
//        assertEquals(userId, response.userId());
//        assertEquals(OrderStatus.PENDING, response.status());
//        verify(orderFactory).createOrder(eq(userId), anyList());
//        verify(orderRepository).save(any(Order.class));
//    }
//
//    @Test
//    void getMyOrders_ShouldReturnListOfOrders() {
//        // Arrange
//        when(orderRepository.findByUserId(userId)).thenReturn(List.of(sampleOrder));
//
//        // Act
//        List<OrderResponse> responses = orderService.getMyOrders(userId);
//
//        // Assert
//        assertFalse(responses.isEmpty());
//        assertEquals(1, responses.size());
//        assertEquals(orderId, responses.get(0).id());
//    }
//
//    @Test
//    void getOrderById_WhenOrderExistsAndUserMatches_ShouldReturnOrder() {
//        // Arrange
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(sampleOrder));
//
//        // Act
//        OrderResponse response = orderService.getOrderById(orderId, userId);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(orderId, response.id());
//    }
//
//    @Test
//    void getOrderById_WhenOrderDoesNotExist_ShouldThrowException() {
//        // Arrange
//        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            orderService.getOrderById(orderId, userId);
//        });
//        assertEquals("Order not found", exception.getMessage());
//    }
//
//    @Test
//    void getOrderById_WhenUserDoesNotMatch_ShouldThrowException() {
//        // Arrange
//        Long otherUserId = 999L;
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(sampleOrder));
//
//        // Act & Assert
//        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
//            orderService.getOrderById(orderId, otherUserId);
//        });
//        assertEquals("You can only view your own orders", exception.getMessage());
//    }
//
//    @Test
//    void updateStatus_WhenStatusIsPaid_ShouldPublishEvent() {
//        // Arrange
//        UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(OrderStatus.PAID);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(sampleOrder));
//        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);
//
//        // Act
//        OrderStatusResponse response = orderService.updateStatus(orderId, request, userId);
//
//        // Assert
//        assertEquals(OrderStatus.PAID, sampleOrder.getStatus());
//        verify(eventPublisher).publishEvent(any(OrderConfirmedEvent.class));
//    }
//
//    @Test
//    void deleteOrder_WhenOrderIsPending_ShouldDelete() {
//        // Arrange
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(sampleOrder));
//
//        // Act
//        DeleteResponse response = orderService.deleteOrder(orderId, userId);
//
//        // Assert
//        assertEquals("Order deleted", response.message());
//        verify(orderRepository).deleteById(orderId);
//    }
//
//    @Test
//    void deleteOrder_WhenOrderIsNotPending_ShouldThrowException() {
//        // Arrange
//        sampleOrder.setStatus(OrderStatus.SHIPPED);
//        when(orderRepository.findById(orderId)).thenReturn(Optional.of(sampleOrder));
//
//        // Act & Assert
//        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            orderService.deleteOrder(orderId, userId);
//        });
//        assertEquals("Only pending orders can be deleted", exception.getMessage());
//    }
//}
