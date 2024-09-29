package org.example.homeservice.dto;

import org.example.homeservice.entites.Service;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toEntity(ServiceRequest serviceRequestDto);
    ServiceResponse toDto(Service service);
}