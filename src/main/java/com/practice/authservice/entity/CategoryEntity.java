package com.practice.authservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_category", schema = "s_auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
}
