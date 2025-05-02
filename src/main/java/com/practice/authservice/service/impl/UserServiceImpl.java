package com.practice.authservice.service.impl;

import com.practice.authservice.dto.UserProfileDto;
import com.practice.authservice.exception.UsernameAlreadyExistsException;
import com.practice.authservice.entity.RoleEntity;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.repository.RoleEntityRepository;
import com.practice.authservice.repository.UserEntityRepository;
import com.practice.authservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final FileStorageService fileStorageService;

    @Transactional
    public void register(String username, String email, String role, String password, MultipartFile avatar) throws RoleNotFoundException {
        if (userRepository.existsByEmail(email)) {
            throw new UsernameAlreadyExistsException("Email already exists!");
        }

        String avatarPath = fileStorageService.storeFile(avatar);

        RoleEntity roleUser = roleRepository.findByAuthority(role)
                .orElseThrow(() -> new RoleNotFoundException("Role not found!"));

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(roleUser));
        user.setAvatarPath(avatarPath);

        userRepository.save(user);
    }


    public void authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @Transactional
    public void toggleUserBlock(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            throw new AccessDeniedException("Only admins can block users");
        }

        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
    }

    @Override
    public UserProfileDto getUserProfile(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return UserProfileDto.fromEntity(user);
    }

    @Override
    @Transactional
    public List<UserProfileDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserProfileDto::fromEntity)
                .toList();
    }

}


