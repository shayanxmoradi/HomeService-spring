package org.example.homeservice.dto.specialist;

import org.example.homeservice.domain.enums.SpecialistStatus;

public record SpecialistResponse(Long id,
                                 String firstName,
                                 String lastName,
                                 String email,
                                 SpecialistStatus specialistStatus,
                                 Double rate,
                                 int numberOfRate,
                                 byte[] personalImage,
                                 Long walletId,
                                 Boolean isActive
) {}
