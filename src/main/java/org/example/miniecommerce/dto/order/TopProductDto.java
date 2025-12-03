package org.example.miniecommerce.dto.order;

import java.math.BigDecimal;

public record TopProductDto(
    Long productId,
    String productName,
    Long timesSold,
    Long totalQuantity,
    BigDecimal totalRevenue,
    BigDecimal avgPrice
) {}

