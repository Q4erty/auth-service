package com.practice.authservice.service;

import com.practice.authservice.dto.UserProfileDto;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

public interface UserService {

    void register(String username, String email, String role, String password, MultipartFile avatar) throws RoleNotFoundException;

    void authenticate(String username, String password);

    void toggleUserBlock(Long userId);

    UserProfileDto getUserProfile(Long userId);

    List<UserProfileDto> getAllUsers();
}
