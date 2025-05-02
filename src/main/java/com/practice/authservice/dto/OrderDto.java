package com.practice.authservice.dto;

import com.practice.authservice.entity.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderDto(
        Long id,
        String title,
        String description,
        BigDecimal price,
        LocalDate deadline,
        Long categoryId,
        Long clientId,
        Long freelancerId,
        OrderStatus status,
        boolean clientConfirmed,
        boolean freelancerConfirmed
) {
    public static OrderDto fromEntity(OrderEntity entity) {
        return new OrderDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getDeadline(),
                entity.getCategory().getId(),
                entity.getClient().getId(),
                entity.getFreelancer() != null ? entity.getFreelancer().getId() : null,
                entity.getStatus(),
                entity.isClientConfirmed(),
                entity.isFreelancerConfirmed()
        );
    }
}

