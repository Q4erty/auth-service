package com.practice.authservice.controller;

import com.practice.authservice.jwt.JwtUtil;
import com.practice.authservice.kafka.EmailVerificationProducer;
import com.practice.authservice.service.EmailVerificationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final JwtUtil jwtUtil;
    private final EmailVerificationProducer emailVerificationProducer;

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerification(@RequestParam("email") String email) {
        emailVerificationProducer.sendEmailVerificationEvent(email);
        return ResponseEntity.ok("Verification email request sent to Kafka");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        try {
            Claims claims = jwtUtil.parseToken(token);

            if (!"email-verification".equals(claims.get("type"))) {
                return ResponseEntity.badRequest().body("Invalid token type");
            }

            String email = claims.getSubject();
            emailVerificationService.verifyUser(email);
            return ResponseEntity.ok("Email successfully verified");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

}
