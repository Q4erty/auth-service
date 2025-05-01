package com.practice.authservice.service.impl;

import com.practice.authservice.dto.ApplicationCreateRequest;
import com.practice.authservice.dto.ApplicationDto;
import com.practice.authservice.dto.ApplicationStatus;
import com.practice.authservice.entity.ApplicationEntity;
import com.practice.authservice.entity.OrderEntity;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.exception.NotFoundException;
import com.practice.authservice.repository.ApplicationRepository;
import com.practice.authservice.repository.OrderRepository;
import com.practice.authservice.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final OrderRepository orderRepository;
    private final UserEntityRepository userRepository;

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

//    @Transactional
//    public ApplicationDto acceptApplication(Long applicationId, String userEmail) {
//        ApplicationEntity application = applicationRepository.findById(applicationId)
//                .orElseThrow(() -> new NotFoundException("Application not found"));
//
//        UserEntity client = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new NotFoundException("User not found"));
//
//        if (!application.getOrder().getClient().getId().equals(client.getId())) {
//            throw new AccessDeniedException("Only order owner can accept applications");
//        }
//
//        application.setStatus(ApplicationStatus.ACCEPTED);
//
//        OrderEntity order = application.getOrder();
//        order.setFreelancer(application.getFreelancer());
//        orderRepository.save(order);
//
//        applicationRepository.findByOrderIdAndStatus(order.getId(), ApplicationStatus.PENDING)
//                .forEach(otherApplication -> {
//                    otherApplication.setStatus(ApplicationStatus.CANCELLED);
//                    applicationRepository.save(otherApplication);
//                });
//
//        return ApplicationDto.fromEntity(applicationRepository.save(application));
//    }
}
