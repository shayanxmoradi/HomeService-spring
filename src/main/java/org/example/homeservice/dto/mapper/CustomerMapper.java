package org.example.homeservice.dto.mapper;

import org.example.homeservice.dto.CustomerResponseDto;
import org.example.homeservice.dto.CustomerRequsetDto;
import org.example.homeservice.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    Customer toEntity(CustomerRequsetDto dto);
    @Mapping(source = "wallet.id",target = "walletId")
    CustomerResponseDto toResponseDto(Customer customer);
    List<CustomerResponseDto> toResponseDto(List<Customer> customer);
    CustomerRequsetDto toDto(Customer entity);

}
