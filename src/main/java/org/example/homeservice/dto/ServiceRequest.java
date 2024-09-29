package org.example.homeservice.dto;

import java.util.List;

public record ServiceRequest(
    Long id,
    String name,
    String description,
    Float basePrice,
    Long parentServiceId,
    boolean isCategory,
    List<Long> availableSpecialistsIds){ //

}
