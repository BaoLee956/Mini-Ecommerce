package org.example.miniecommerce.service.order.decorator;

import org.example.miniecommerce.entity.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Decorator Pattern: Manager để áp dụng các decorator cho đơn hàng
 */
@Service
public class OrderDecoratorManager {

    private final List<OrderDecorator> availableDecorators;

    public OrderDecoratorManager(ShippingDecorator expressShipping) {
        this.availableDecorators = List.of(expressShipping);
    }

    /**
     * Áp dụng một decorator cụ thể cho đơn hàng
     */
    public Order applyDecorator(Order order, OrderDecoratorName decoratorName, BigDecimal fee) {
        OrderDecorator decorator = findDecoratorByName(decoratorName);

        if (decorator.canApply(order)) {
            return decorator.apply(order, fee);
        } else {
            throw new IllegalStateException(
                    "Cannot apply decorator '" + decoratorName + "' to order in status: " + order.getStatus());
        }
    }


    private OrderDecorator findDecoratorByName(OrderDecoratorName name) {
        return availableDecorators.stream()
                .filter(d -> d.getName()
                        .equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Decorator not implements: " + name.getDescription()));
    }

}
