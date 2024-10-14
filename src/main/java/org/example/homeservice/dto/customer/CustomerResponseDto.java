package org.example.homeservice.dto.customer;

import java.time.LocalDateTime;

public record CustomerResponseDto(Long id, String firstName, String lastName, String email, LocalDateTime registrationDate,Long walletId) {}

