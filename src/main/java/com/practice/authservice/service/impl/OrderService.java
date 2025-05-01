package com.practice.authservice.service.impl;

import com.practice.authservice.dto.ApplicationDto;
import com.practice.authservice.dto.OrderCreateRequest;
import com.practice.authservice.dto.OrderDto;
import com.practice.authservice.dto.OrderStatus;
import com.practice.authservice.entity.ApplicationEntity;
import com.practice.authservice.entity.CategoryEntity;
import com.practice.authservice.entity.OrderEntity;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.exception.ConflictException;
import com.practice.authservice.exception.NotFoundException;
import com.practice.authservice.repository.ApplicationRepository;
import com.practice.authservice.repository.CategoryRepository;
import com.practice.authservice.repository.OrderRepository;
import com.practice.authservice.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ApplicationRepository applicationRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final UserEntityRepository userRepository;

    public Page<OrderDto> getAllOrders(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (categoryId != null) {
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found"));
            return orderRepository.findByCategoryAndFreelancerIsNull(category, pageable)
                    .map(OrderDto::fromEntity);
        }
        return orderRepository.findAll(pageable)
                .map(OrderDto::fromEntity);
    }

    public OrderDto createOrder(OrderCreateRequest request, String userEmail) {
        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        CategoryEntity category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        OrderEntity order = OrderEntity.builder()
                .title(request.title())
                .description(request.description())
                .price(request.price())
                .deadline(request.deadline())
                .category(category)
                .client(client)
                .build();

        return OrderDto.fromEntity(orderRepository.save(order));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<ApplicationDto> getOrderApplications(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        return applicationRepository.findByOrder(order).stream()
                .map(ApplicationDto::fromEntity)
                .toList();
    }

    @Transactional
    public OrderDto acceptApplication(Long applicationId, String userEmail) {
        ApplicationEntity application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("Application not found"));

        OrderEntity order = application.getOrder();

        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new ConflictException("Order already completed");
        }

        UserEntity client = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!order.getClient().equals(client)) {
            throw new AccessDeniedException("Only order owner can accept applications");
        }

        order.setFreelancer(application.getFreelancer());
        order.setStatus(OrderStatus.IN_PROGRESS);

        return OrderDto.fromEntity(orderRepository.save(order));
    }

    @Transactional
    public void completeOrder(Long orderId, String userEmail) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (order.getClient().equals(user)) {
            order.setClientConfirmed(true);
        } else if (order.getFreelancer().equals(user)) {
            order.setFreelancerConfirmed(true);
        } else {
            throw new AccessDeniedException("You can't confirm this order");
        }

        if (order.isClientConfirmed() && order.isFreelancerConfirmed()) {
            order.setStatus(OrderStatus.COMPLETED);
        }

        if (order.getStatus() == OrderStatus.COMPLETED
                && (!order.isClientConfirmed() || !order.isFreelancerConfirmed())) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        }

        orderRepository.save(order);
    }

    @Transactional
    public void cancelConfirmation(Long orderId, String userEmail) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (order.getClient().equals(user)) {
            order.setClientConfirmed(false);
        } else if (order.getFreelancer().equals(user)) {
            order.setFreelancerConfirmed(false);
        } else {
            throw new AccessDeniedException("Access denied");
        }

        if (order.getStatus() == OrderStatus.COMPLETED) {
            order.setStatus(OrderStatus.IN_PROGRESS);
        }

        orderRepository.save(order);
    }
}
