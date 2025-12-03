package org.example.miniecommerce.entity;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Category extends BaseEntity {
    private String name;

    private String description;

    private transient List<Product> products;
}
