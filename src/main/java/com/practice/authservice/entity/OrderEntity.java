package com.practice.authservice.entity;

import com.practice.authservice.dto.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "t_order", schema = "s_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private BigDecimal price;
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private UserEntity client;

    @ManyToOne
    @JoinColumn(name = "freelancer_id")
    private UserEntity freelancer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.ACTIVE;

    @Column(name = "client_confirmed")
    private boolean clientConfirmed = false;

    @Column(name = "freelancer_confirmed")
    private boolean freelancerConfirmed = false;
}
