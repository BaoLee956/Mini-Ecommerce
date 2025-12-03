package org.example.miniecommerce.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Role {
    private Long id;
    private String name;
    private String description;
}
