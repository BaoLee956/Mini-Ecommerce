package org.example.miniecommerce.service.order.decorator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderDecoratorName {
    SHIPPING("Shipping fee");
    private final String description;
}
