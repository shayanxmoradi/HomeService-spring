package org.example.homeservice.dto;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

public record CustomerResponseDto(Long id, String firstName, String lastName, String email, LocalDateTime registrationDate,Long walletId) {}

