package com.practice.authservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    void register(String username, String email, String role, String password, MultipartFile avatar);

    void authenticate(String username, String password);

    void toggleUserBlock(Long userId);
}
