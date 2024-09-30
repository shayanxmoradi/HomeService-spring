package org.example.homeservice.dto.mapper;

import org.example.homeservice.dto.CustomerRequsetDto;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.entites.Customer;
import org.example.homeservice.entites.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    Service toEntity(ServiceRequest serviceRequestDto);
    ServiceResponse toDto(Service service);
//    @Mapping(target = "isCategory", source = "isCategory")
    ServiceRequest toDtoReq(Service service);

}