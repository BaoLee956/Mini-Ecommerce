package org.example.miniecommerce.dto.permission;

import lombok.Data;

@Data
public class CreatePermissionRequest {
    private String name;
    private String description;
}
