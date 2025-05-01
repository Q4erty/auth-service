package com.practice.authservice.controller;

import com.practice.authservice.dto.RatingCreateRequest;
import com.practice.authservice.service.impl.RatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT', 'FREELANCER')")
    public ResponseEntity<?> createRating(
            @RequestBody @Valid RatingCreateRequest request,
            Principal principal
    ) {
        ratingService.createRating(request, principal.getName());
        return ResponseEntity.ok().build();
    }
}