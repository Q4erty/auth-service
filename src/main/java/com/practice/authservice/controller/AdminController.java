package com.practice.authservice.controller;

import com.practice.authservice.dto.UserProfileDto;
import com.practice.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/toggle-block/{userId}")
    public ResponseEntity<?> toggleUserBlock(@PathVariable Long userId) {
        userService.toggleUserBlock(userId);
        return ResponseEntity.ok().build();
    }
}

