package com.practice.authservice.dto;

public record AuthRequest(String username, String email, String password) {
}
