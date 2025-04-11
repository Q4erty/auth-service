package com.practice.authservice.service.impl;

import com.practice.authservice.exception.UsernameAlreadyExistsException;
import com.practice.authservice.entity.RoleEntity;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.repository.RoleEntityRepository;
import com.practice.authservice.repository.UserEntityRepository;
import com.practice.authservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserEntityRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void register(String username, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new UsernameAlreadyExistsException("User already exists!");
        }

        RoleEntity roleUser = roleRepository.findByAuthority("ROLE_USER")
                .orElseGet(() -> {
                    log.warn("ROLE_USER not found, creating a new one.");
                    RoleEntity newRole = new RoleEntity();
                    newRole.setAuthority("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(roleUser));

        userRepository.save(user);
    }


    public void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
