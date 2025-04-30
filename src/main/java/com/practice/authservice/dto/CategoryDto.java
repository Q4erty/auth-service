package com.practice.authservice.dto;

import com.practice.authservice.entity.CategoryEntity;

public record CategoryDto(Long id, String name, String description) {
    public static CategoryDto fromEntity(CategoryEntity entity) {
        return new CategoryDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }
}
