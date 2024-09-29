package org.example.homeservice.dto;

import java.sql.Date;
import java.sql.Time;

public record CustomerResponseDto(Long id, String firstName, String lastName, String email, Date registrationDate, Time registrationTime) {}

