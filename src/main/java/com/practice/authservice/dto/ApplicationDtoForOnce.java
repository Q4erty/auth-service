package com.practice.authservice.dto;

import java.time.LocalDateTime;

public record ApplicationDtoForOnce(Long id,
                                    String proposal,
                                    String status,
                                    LocalDateTime createdAt,
                                    UserProfileDto freelancer) {
}
