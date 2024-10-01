package org.example.homeservice.dto;

import jakarta.validation.constraints.*;
import org.example.homeservice.entity.Specialist;
import org.example.homeservice.entity.enums.SpecialistStatus;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record ServiceResponse(
        String name,
        String description,
        Float basePrice,
        Long parentServiceId,
        boolean category,
        List<Specialist> availableSpecialists,
        List<ServiceResponse> subServices
){
}
