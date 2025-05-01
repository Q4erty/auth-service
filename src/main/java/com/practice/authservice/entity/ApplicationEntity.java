package com.practice.authservice.entity;

import com.practice.authservice.dto.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_application", schema = "s_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private UserEntity freelancer;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    private String proposal;
    private LocalDateTime createdAt;
}
