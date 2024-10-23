package org.example.homeservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.homeservice.controller.config.JwtUtil;
import org.example.homeservice.dto.order.OrderMapper;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderResource {
    private final JwtUtil jwtUtil;
    @Value("${secret-key}")
    private String SECRET_KEY;
    private final OrderService orderService;
    private final CustomerService customerService;
    private final SpeciallistService speciallistService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        Optional<OrderResponse> serviceResponse = orderService.findById(id);
        return serviceResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER')") // Allow only users with "CUSTOMER" role

    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Validated OrderRequest order,HttpServletRequest request) {
        System.out.println(jwtUtil.getCurrentUserEmail(request));
        Optional<OrderResponse> savingResponse = orderService.save(order);
        //todo check duplicate
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PutMapping("/{id}/{offer}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderResponse> choseOrder(@PathVariable Long id, @PathVariable Long offer,@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername());
        Optional<OrderResponse> savingResponse = orderService.choseOrder(id, offer);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.ACCEPTED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PutMapping("/start/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderResponse> startOrder(@PathVariable Long id) {
        Optional<OrderResponse> savingResponse = orderService.startOrder(id);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.ACCEPTED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PutMapping("/end/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<OrderResponse> endOrder(@PathVariable Long id) {
        Optional<OrderResponse> savingResponse = orderService.endOrder(id);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.ACCEPTED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/customer")
    public ResponseEntity<List<OrderResponse>> getOrderByCustomerId(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = customerService.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"))
                .id();

        List<OrderResponse> orderResponses = orderService.findByCustomerId(userId);

        if (orderResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(orderResponses);
    }


    @GetMapping("/specialist")
    @PreAuthorize("hasAuthority('SPECIALIST')")

    public ResponseEntity<List<OrderResponse>> getOrderBySpecialistId(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        Long userId = speciallistService.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"))
                .id();

        List<OrderResponse> orderResponses = orderService.findBySpecialistId(userId);

        if (orderResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok(orderResponses);
    }
}
