package com.practice.authservice.kafka;

public interface EmailVerificationProducer {

    void sendEmailVerificationEvent(String email);

}
