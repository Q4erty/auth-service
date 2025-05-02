package com.practice.authservice.controller;

import com.practice.authservice.dto.ApplicationCreateRequest;
import com.practice.authservice.dto.ApplicationDto;
import com.practice.authservice.dto.ApplicationDtoForOnce;
import com.practice.authservice.dto.OrderDto;
import com.practice.authservice.service.impl.ApplicationService;
import com.practice.authservice.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;
    private final OrderService orderService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ApplicationDtoForOnce>> getApplicationsForOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(applicationService.getApplicationsByOrder(orderId));
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<ApplicationDto>> getFreelancerApplications(
            @PathVariable Long freelancerId
    ) {
        return ResponseEntity.ok(
                applicationService.getApplicationsByFreelancer(freelancerId)
        );
    }

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
    public ResponseEntity<OrderDto> acceptApplication(
            @PathVariable Long id,
            Principal principal
    ) {
        return ResponseEntity.ok(orderService.acceptApplication(id, principal.getName()));
    }

    @PatchMapping("/{id}/decline")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> declineApplication(
            @PathVariable Long id,
            Principal principal
    ) {
        applicationService.declineApplication(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}