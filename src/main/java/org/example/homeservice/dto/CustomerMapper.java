package org.example.homeservice.dto;

import org.example.homeservice.entites.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequsetDto dto);
    CustomerResponseDto toResponseDto(Customer customer);
}
