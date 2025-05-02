package com.practice.authservice.repository;

import com.practice.authservice.dto.ApplicationDtoForOnce;
import com.practice.authservice.dto.ApplicationStatus;
import com.practice.authservice.entity.ApplicationEntity;
import com.practice.authservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    Optional<ApplicationEntity> findByOrder(OrderEntity order);
    List<ApplicationEntity> findByOrderId(Long orderId);
    List<ApplicationEntity> findByOrderIdAndStatus(Long orderId, ApplicationStatus status);
    List<ApplicationEntity> findByFreelancerId(Long freelancerId);
}
