package com.practice.authservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(schema = "s_auth", name = "t_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_username", nullable = false, unique = true)
    private String username;

    @Column(name = "c_email", nullable = false, unique = true)
    private String email;

    @Column(name = "c_password", nullable = false)
    private String password;

    @Column(name = "c_is_verified", nullable = false)
    private boolean isVerified;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            schema = "s_auth",
            name = "t_client_role",
            joinColumns = @JoinColumn(name = "id_client"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<RoleEntity> roles;

    @Column(name = "c_avatar_path")
    private String avatarPath;

    @Column(name = "c_is_blocked", nullable = false)
    private boolean isBlocked = false;

    @Column(name = "c_block_reason")
    private String blockReason;

    @Column(name = "c_blocked_at")
    private LocalDateTime blockedAt;

    @Column(name = "average_rating", columnDefinition = "numeric(3,2) default 0.00")
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "rating_count", columnDefinition = "int default 0")
    private int ratingCount = 0;
}
