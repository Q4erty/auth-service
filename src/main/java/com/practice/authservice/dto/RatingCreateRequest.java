package com.practice.authservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingCreateRequest(
        @NotNull Long orderId,
        @Min(1) @Max(5) int score,
        String comment
) {}