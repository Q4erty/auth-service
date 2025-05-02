package com.practice.authservice.controller;

import com.practice.authservice.dto.AuthRequest;
import com.practice.authservice.dto.UserProfileDto;
import com.practice.authservice.entity.UserEntity;
import com.practice.authservice.jwt.JwtUtil;
import com.practice.authservice.kafka.EmailVerificationProducer;
import com.practice.authservice.repository.UserEntityRepository;
import com.practice.authservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.RoleNotFoundException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailVerificationProducer emailVerificationProducer;
    private final UserEntityRepository userRepository;

    @Value("${jwt.access-token-expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    @Value("${cookie.access-token-name}")
    private String ACCESS_TOKEN;

    @Value("${cookie.refresh-token-name}")
    private String REFRESH_TOKEN;

    @PostMapping("/login")
    public ResponseEntity<UserProfileDto> login(@RequestBody AuthRequest request, HttpServletResponse response) {
        userService.authenticate(request.email(), request.password());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        String accessToken = jwtUtil.generateAccessToken(username);
        String refreshToken = jwtUtil.generateRefreshToken(username);

        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN, accessToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(ACCESS_TOKEN_EXPIRATION / 1000)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .path("/api/auth/refresh")
                .maxAge(REFRESH_TOKEN_EXPIRATION / 1000)
                .build();


        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        Long userId = user.getId();
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<UserProfileDto> register(@RequestPart String name,
                                                   @RequestPart String email,
                                                   @RequestPart String role,
                                                   @RequestPart String password,
                                                   @RequestPart(required = false) MultipartFile avatar,
                                                   HttpServletResponse response) throws RoleNotFoundException {
        userService.register(name, email, role, password, avatar);

        emailVerificationProducer.sendEmailVerificationEvent(email);

        AuthRequest authRequest = new AuthRequest(email, password);
        return login(authRequest, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtUtil.getRefreshToken(request);

        if (refreshToken == null || !jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);

        ResponseCookie accessCookie = ResponseCookie.from(ACCESS_TOKEN, newAccessToken)
                .httpOnly(false)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(ACCESS_TOKEN_EXPIRATION / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());

        return ResponseEntity.ok(Map.of("access_token", newAccessToken));
    }

}
