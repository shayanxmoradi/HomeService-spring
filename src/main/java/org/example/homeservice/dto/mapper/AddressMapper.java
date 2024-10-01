package org.example.homeservice.dto.mapper;

import org.example.homeservice.dto.AddressReqest;
import org.example.homeservice.dto.AddressResponse;
import org.example.homeservice.dto.CustomerRequsetDto;
import org.example.homeservice.dto.CustomerResponseDto;
import org.example.homeservice.entity.Address;
import org.example.homeservice.entity.Customer;
import org.example.homeservice.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "user.id", source = "userId")

    Address toEntity(AddressReqest dto);
    Address toEntity(AddressResponse dto);

    AddressResponse toResponseDto(Address address);
    AddressReqest toDtoReq(Address address);
    default Address mapAddressId(Long addressId) {
        if (addressId == null) {
            return null;
        }
        Address address = new Address();
        address.setId(addressId);
        return address;
    }
}
