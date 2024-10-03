package org.example.homeservice.dto;

import jakarta.validation.constraints.*;
import org.example.homeservice.entity.Specialist;
import org.example.homeservice.entity.enums.SpecialistStatus;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

public record ServiceResponse(
        Long id,
        String name,
        String description,
        Float basePrice,
        Long parentServiceId,
        boolean category,
        List<Long> availableSpecialists,
        List<ServiceResponse> subServices
){
    public ServiceResponse {
        if (availableSpecialists == null) {
            availableSpecialists = new ArrayList<>();
        }
        if (subServices == null) {
            subServices = new ArrayList<>();
        }
    }
}
