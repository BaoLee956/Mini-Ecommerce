package org.example.miniecommerce.entity;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter



public class Product extends BaseEntity {

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private transient Category category;

    private Long categoryId;
}
