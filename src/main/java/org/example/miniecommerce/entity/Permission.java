package org.example.miniecommerce.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class Permission extends BaseEntity{
    private Long id;
    private String name;
    private String description;
}
