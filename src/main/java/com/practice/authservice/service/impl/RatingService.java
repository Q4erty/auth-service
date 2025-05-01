package com.practice.authservice.service.impl;

import com.practice.authservice.dto.OrderStatus;
import com.practice.authservice.dto.RatingCreateRequest;
import com.practice.authservice.entity.OrderEntity;
import com.practice.authservice.entity.RatingEntity;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.exception.ConflictException;
import com.practice.authservice.exception.NotFoundException;
import com.practice.authservice.repository.OrderRepository;
import com.practice.authservice.repository.RatingRepository;
import com.practice.authservice.repository.UserEntityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserEntityRepository userRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createRating(RatingCreateRequest request, String userEmail) {
        UserEntity fromUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException("User not found"));

        OrderEntity order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.COMPLETED) {
            throw new IllegalStateException("Order is not completed");
        }

        if (!order.getClient().equals(fromUser) && !order.getFreelancer().equals(fromUser)) {
            throw new AccessDeniedException("You can't rate this order");
        }

        UserEntity toUser = order.getClient().equals(fromUser)
                ? order.getFreelancer()
                : order.getClient();

        if (ratingRepository.existsByOrderAndFromUser(order, fromUser)) {
            throw new ConflictException("You already rated this order");
        }

        RatingEntity rating = RatingEntity.builder()
                .score(request.score())
                .comment(request.comment())
                .order(order)
                .fromUser(fromUser)
                .toUser(toUser)
                .build();

        ratingRepository.save(rating);
        updateUserRating(toUser, request.score());
    }

    private void updateUserRating(UserEntity user, int newScore) {
        BigDecimal total = user.getAverageRating()
                .multiply(BigDecimal.valueOf(user.getRatingCount()))
                .add(BigDecimal.valueOf(newScore));

        user.setRatingCount(user.getRatingCount() + 1);
        user.setAverageRating(
                total.divide(BigDecimal.valueOf(user.getRatingCount()), 2, RoundingMode.HALF_UP)
        );

        userRepository.save(user);
    }
}
