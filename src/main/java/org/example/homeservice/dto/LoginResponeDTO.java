package org.example.dto;

import lombok.Data;

@Data
public class LoginResponeDTO {
    private Long userId;     // ID of the logged-in user
    private String firstName; // Optionally return user details
    private String lastName;
}
