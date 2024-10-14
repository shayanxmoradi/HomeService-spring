package org.example.homeservice.dto.service;

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
