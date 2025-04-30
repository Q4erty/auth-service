package com.practice.authservice.controller;

import com.practice.authservice.dto.ApplicationCreateRequest;
import com.practice.authservice.dto.ApplicationDto;
import com.practice.authservice.service.impl.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ApplicationDto> createApplication(
            @RequestBody ApplicationCreateRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(applicationService.createApplication(request, principal.getName()));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<?> cancelApplication(
            @PathVariable Long id,
            Principal principal
    ) {
        applicationService.cancelApplication(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/accept")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ApplicationDto> acceptApplication(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(applicationService.acceptApplication(id, principal.getName()));
    }
}