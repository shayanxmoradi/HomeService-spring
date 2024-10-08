package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.service.order.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderResource {
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getServiceById(@PathVariable Long id) {
        Optional<OrderResponse> serviceResponse = orderService.findById(id);
        return serviceResponse
                .map(service -> ResponseEntity.status(HttpStatus.CREATED).body(service))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Validated OrderRequest order) {
        Optional<OrderResponse> savingResponse = orderService.save(order);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
    @PutMapping("/{id}/{offer}")
    public ResponseEntity<OrderResponse> choseOrder(@PathVariable Long id, @PathVariable Long offer) {
        Optional<OrderResponse> savingResponse = orderService.choseOrder(id, offer);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }
}
