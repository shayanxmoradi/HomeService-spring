package org.example.homeservice.dto.mapper;

import org.example.homeservice.dto.CustomerRequsetDto;
import org.example.homeservice.dto.CustomerResponseDto;
import org.example.homeservice.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequsetDto dto);
    CustomerResponseDto toResponseDto(Customer customer);
    CustomerRequsetDto toDto(Customer entity);

}
