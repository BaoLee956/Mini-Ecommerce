package org.example.miniecommerce.service.order.decorator;

import org.example.miniecommerce.entity.Order;

import java.math.BigDecimal;

/**
 * Decorator Pattern: Interface cho các decorator thêm tính năng cho đơn hàng
 */
public abstract class OrderDecorator {
    protected OrderDecoratorName name;

    /**
     * Áp dụng decorator cho đơn hàng
     */
    abstract Order apply(Order order, BigDecimal fee);

    /**
     * Lấy mô tả của decorator
     */
    protected OrderDecoratorName getName() {
        return this.name;
    }

    protected String getDescription() {
        return this.name.getDescription();
    }

    /**
     * Kiểm tra decorator có thể áp dụng không
     */
    abstract boolean canApply(Order order);

}
