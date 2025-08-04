package org.example.homeservice.dto;

import java.time.LocalDateTime;

public record ApiResponse(String message, int statusCode, LocalDateTime timestamp){}
