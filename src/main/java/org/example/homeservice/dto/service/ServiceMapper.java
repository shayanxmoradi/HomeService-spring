package org.example.homeservice.dto.service;

import org.example.homeservice.domain.service.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    @Mapping(target = "category", source = "isCategory")
    Service toEntity(ServiceRequest serviceRequestDto);


    @Mapping(target = "parentServiceId", source = "parentService.id")
    ServiceResponse toDto(Service service);

    List<ServiceResponse> toDto(List<Service> service);

    ServiceRequest toDtoReq(Service service);

    ServiceRequest responeToDtoReq(ServiceResponse serviceResponse);




}

