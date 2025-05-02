package com.practice.authservice.dto;

import com.practice.authservice.entity.RoleEntity;
import com.practice.authservice.entity.UserEntity;
import java.math.BigDecimal;
import java.util.Set;

public record UserProfileDto(
        Long id,
        String username,
        String role,
        String email,
        String avatarPath,
        BigDecimal averageRating,
        int ratingCount,
        boolean isBlocked
) {
    public static UserProfileDto fromEntity(UserEntity entity) {
        Set<RoleEntity> roles = entity.getRoles();
        String firstRole = roles.stream()
                .findFirst()
                .map(RoleEntity::getAuthority)
                .orElse("ROLE_USER");

        return new UserProfileDto(
                entity.getId(),
                entity.getUsername(),
                firstRole,
                entity.getEmail(),
                entity.getAvatarPath(),
                entity.getAverageRating(),
                entity.getRatingCount(),
                entity.isBlocked()
        );
    }
}