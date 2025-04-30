package com.practice.authservice.controller;

import com.practice.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping("/toggle-block/{userId}")
    public ResponseEntity<?> toggleUserBlock(@PathVariable Long userId) {
        userService.toggleUserBlock(userId);
        return ResponseEntity.ok().build();
    }
}

