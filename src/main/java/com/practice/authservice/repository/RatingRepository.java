package com.practice.authservice.repository;

import com.practice.authservice.entity.OrderEntity;
import com.practice.authservice.entity.RatingEntity;
import com.practice.authservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    boolean existsByOrderAndFromUser(OrderEntity order, UserEntity user);
}
