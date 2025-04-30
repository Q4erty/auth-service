package com.practice.authservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderCreateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @Positive BigDecimal price,
        @Future LocalDate deadline,
        @NotNull Long categoryId
) {
}
