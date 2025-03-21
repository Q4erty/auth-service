package com.practice.authservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class MainController {

    @GetMapping("/greetings")
    public ResponseEntity<Map<String, String>> greetings() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "Hello, %s!".formatted(userDetails.getUsername())));
    }

    @GetMapping("/greetings-v2")
    public ResponseEntity<Map<String, String>> greetingsV2(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("message", "Hello, %s!".formatted(userDetails.getUsername())));
    }

}
