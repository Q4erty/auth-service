package com.practice.authservice.kafka.impl;

import com.practice.authservice.jwt.JwtUtil;
import com.practice.authservice.kafka.EmailVerificationProducer;
import com.practice.common.dto.EmailVerificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationProducerImpl implements EmailVerificationProducer {

    private final KafkaTemplate<String, EmailVerificationEvent> kafkaTemplate;
    private final JwtUtil jwtUtil;

    @Override
    public void sendEmailVerificationEvent(String email) {
        EmailVerificationEvent event = new EmailVerificationEvent(email, jwtUtil.generateEmailVerificationToken(email));
        kafkaTemplate.send("email-verification-topic", event);
    }

}
