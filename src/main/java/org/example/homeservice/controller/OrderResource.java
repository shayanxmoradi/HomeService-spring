package org.example.homeservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
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
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody @Validated OrderRequest order) {
        Optional<OrderResponse> savingResponse = orderService.save(order);
        //todo check duplicate
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.CREATED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PutMapping("/{id}/{offer}")
    public ResponseEntity<OrderResponse> choseOrder(@PathVariable Long id, @PathVariable Long offer) {
        Optional<OrderResponse> savingResponse = orderService.choseOrder(id, offer);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.ACCEPTED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PutMapping("/start/{id}")
    public ResponseEntity<OrderResponse> startOrder(@PathVariable Long id) {
        Optional<OrderResponse> savingResponse = orderService.startOrder(id);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.ACCEPTED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PutMapping("/end/{id}")
    public ResponseEntity<OrderResponse> endOrder(@PathVariable Long id) {
        Optional<OrderResponse> savingResponse = orderService.endOrder(id);
        return savingResponse
                .map(o -> ResponseEntity.status(HttpStatus.ACCEPTED).body(o))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

//
}
