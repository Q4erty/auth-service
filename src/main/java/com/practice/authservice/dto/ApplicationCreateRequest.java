package com.practice.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ApplicationCreateRequest(
        @NotNull Long orderId,
        @NotBlank String proposal
) {
}
