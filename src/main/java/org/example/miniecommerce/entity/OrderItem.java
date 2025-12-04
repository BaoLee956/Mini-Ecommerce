package org.example.miniecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "order_items")
@IdClass(OrderItem.OrderItemId.class)
@SQLDelete(sql = "UPDATE order_items SET deleted_at = NOW() WHERE order_id=? AND product_id=?")
@SQLRestriction("deleted_at IS NULL")
public class OrderItem {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // Composite key class
    public static class OrderItemId implements Serializable {
        private Long orderId;
        private Long productId;

        public OrderItemId() {}

        public OrderItemId(Long orderId, Long productId) {
            this.orderId = orderId;
            this.productId = productId;
        }

        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderItemId that = (OrderItemId) o;
            return orderId.equals(that.orderId) && productId.equals(that.productId);
        }

        @Override
        public int hashCode() {
            return orderId.hashCode() + productId.hashCode();
        }
    }
}
