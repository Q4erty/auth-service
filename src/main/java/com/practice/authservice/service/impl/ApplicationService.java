package com.practice.authservice.service.impl;

import com.practice.authservice.dto.*;
import com.practice.authservice.entity.ApplicationEntity;
import com.practice.authservice.entity.OrderEntity;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.exception.ConflictException;
import com.practice.authservice.exception.NotFoundException;
import com.practice.authservice.repository.ApplicationRepository;
import com.practice.authservice.repository.OrderRepository;
import com.practice.authservice.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final OrderRepository orderRepository;
    private final UserEntityRepository userRepository;

    @Transactional(readOnly = true)
    public List<ApplicationDtoForOnce> getApplicationsByOrder(Long orderId) {
        return applicationRepository.findByOrderId(orderId).stream()
                .map(this::convertToDto)
                .toList();
    }

    public ApplicationDto createApplication(ApplicationCreateRequest request, String userEmail) {
        UserEntity freelancer = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        OrderEntity order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        ApplicationEntity application = ApplicationEntity.builder()
                .order(order)
                .freelancer(freelancer)
                .status(ApplicationStatus.PENDING)
                .proposal(request.proposal())
                .createdAt(LocalDateTime.now())
                .build();

        return ApplicationDto.fromEntity(applicationRepository.save(application));
    }

    public void cancelApplication(Long id, String userEmail) {
        ApplicationEntity application = applicationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        if (!application.getFreelancer().getEmail().equals(userEmail)) {
            throw new AccessDeniedException("You can't cancel this application");
        }

        application.setStatus(ApplicationStatus.CANCELLED);
        applicationRepository.save(application);
    }

    @Transactional
    public void declineApplication(Long applicationId, String clientEmail) {
        ApplicationEntity application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        UserEntity client = userRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!application.getOrder().getClient().equals(client)) {
            throw new AccessDeniedException("Only order owner can decline applications");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ConflictException("Application is not in pending state");
        }

        application.setStatus(ApplicationStatus.DECLINED);
        applicationRepository.save(application);
    }

    public List<ApplicationDto> getApplicationsByFreelancer(Long freelancerId) {
        return applicationRepository.findByFreelancerId(freelancerId).stream()
                .map(ApplicationDto::fromEntity)
                .toList();
    }

    private ApplicationDtoForOnce convertToDto(ApplicationEntity entity) {
        return new ApplicationDtoForOnce(
                entity.getId(),
                entity.getProposal(),
                entity.getStatus().name(),
                entity.getCreatedAt(),
                convertUserToDto(entity.getFreelancer())
        );
    }

    private UserProfileDto convertUserToDto(UserEntity user) {
        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream()
                        .findFirst()
                        .map(r -> r.getAuthority().replace("ROLE_", ""))
                        .orElse("USER"),
                user.getEmail(),
                user.getAvatarPath(),
                user.getAverageRating(),
                user.getRatingCount(),
                user.isBlocked()
                );
    }
}
