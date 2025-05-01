package com.practice.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "t_rating", schema = "s_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(1) @Max(5)
    private int score;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "from_user_id")
    private UserEntity fromUser;

    @ManyToOne
    @JoinColumn(name = "to_user_id")
    private UserEntity toUser;

    @CreationTimestamp
    private LocalDateTime createdAt;
}