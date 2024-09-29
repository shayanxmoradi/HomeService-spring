package org.example.homeservice.dto;

import org.example.homeservice.entites.enums.SpecialistStatus;

public record SpecialistResponse(Long id,
                                 String firstName,
                                 String lastName,
                                 String email,
                                 SpecialistStatus specialistStatus,
                                 Double rate,
                                 byte[] personalImage
) {}
