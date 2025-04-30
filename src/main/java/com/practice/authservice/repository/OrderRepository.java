package com.practice.authservice.repository;

import com.practice.authservice.entity.CategoryEntity;
import com.practice.authservice.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByCategoryAndFreelancerIsNull(CategoryEntity category, Pageable pageable);

    Page<OrderEntity> findByClientId(Long clientId, Pageable pageable);

    Page<OrderEntity> findByFreelancerId(Long freelancerId, Pageable pageable);
}
