package com.practice.authservice.dto;

import com.practice.authservice.entity.ApplicationEntity;

import java.time.LocalDateTime;

public record ApplicationDto(
        Long id,
        String proposal,
        ApplicationStatus status,
        LocalDateTime createdAt,
        OrderInfo order,
        UserInfo freelancer
) {
    public static ApplicationDto fromEntity(ApplicationEntity entity) {
        return new ApplicationDto(
                entity.getId(),
                entity.getProposal(),
                entity.getStatus(),
                entity.getCreatedAt(),
                new OrderInfo(entity.getOrder().getId(), entity.getOrder().getTitle()),
                new UserInfo(entity.getFreelancer().getId(), entity.getFreelancer().getUsername())
        );
    }

    public record OrderInfo(Long id, String title) {}
    public record UserInfo(Long id, String name) {}
}