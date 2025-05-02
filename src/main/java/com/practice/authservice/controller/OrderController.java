package com.practice.authservice.controller;

import com.practice.authservice.dto.OrderCreateRequest;
import com.practice.authservice.dto.OrderDto;
import com.practice.authservice.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    public ResponseEntity<List<OrderDto>> getMyOrders(Principal principal) {
        return ResponseEntity.ok(orderService.getUserOrders(principal.getName()));
    }

    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(categoryId, page, size));
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody OrderCreateRequest request,
            Principal principal
    ) {
        return ResponseEntity.ok(orderService.createOrder(request, principal.getName()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('CLIENT', 'FREELANCER')")
    public ResponseEntity<?> completeOrder(
            @PathVariable Long id,
            Principal principal
    ) {
        orderService.completeOrder(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel-confirmation")
    @PreAuthorize("hasAnyRole('CLIENT', 'FREELANCER')")
    public ResponseEntity<?> cancelConfirmation(
            @PathVariable Long id,
            Principal principal
    ) {
        orderService.cancelConfirmation(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
