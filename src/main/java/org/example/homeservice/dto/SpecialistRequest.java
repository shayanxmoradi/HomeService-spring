package org.example.homeservice.dto;

import org.example.homeservice.entites.enums.SpecialistStatus;

public record SpecialistRequest(String firstName,
                                String lastName,
                                String email,
                                String password,
                                SpecialistStatus specialistStatus,
                                Double rate,
                                byte[] personalImage
) {
}
