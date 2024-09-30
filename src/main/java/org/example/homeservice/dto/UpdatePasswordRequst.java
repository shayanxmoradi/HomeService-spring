package org.example.homeservice.dto;

public record UpdatePasswordRequst(String email,String oldPassword, String newPassword) {
}

