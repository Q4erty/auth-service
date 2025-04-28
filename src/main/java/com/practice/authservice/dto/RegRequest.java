package com.practice.authservice.dto;

public record RegRequest(String name, String email, String role, String password) {
}
