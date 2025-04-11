package com.practice.authservice.service.impl;

import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.repository.UserEntityRepository;
import com.practice.authservice.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private final UserEntityRepository userRepository;

    public void verifyUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.isVerified()) {
            throw new IllegalStateException("Email already verified");
        }

        user.setVerified(true);
        userRepository.save(user);
    }
}
