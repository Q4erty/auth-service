package com.practice.authservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderUpdateRequest(
        @NotBlank String title,
        @NotBlank String description,
        @Positive BigDecimal price,
        @Future LocalDate deadline
) {
}
