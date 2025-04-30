package com.practice.authservice.service.impl;

import com.practice.authservice.dto.CategoryCreateRequest;
import com.practice.authservice.dto.CategoryDto;
import com.practice.authservice.entity.CategoryEntity;
import com.practice.authservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .toList();
    }

    public CategoryDto createCategory(CategoryCreateRequest request) {
        CategoryEntity category = CategoryEntity.builder()
                .name(request.name())
                .description(request.description())
                .build();

        return CategoryDto.fromEntity(categoryRepository.save(category));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
