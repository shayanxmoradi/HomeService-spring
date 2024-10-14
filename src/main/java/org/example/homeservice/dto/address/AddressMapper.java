package org.example.homeservice.dto.address;

import org.example.homeservice.domain.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    @Mapping(target = "user.id", source = "userId")

    Address toEntity(AddressReqest dto);
    @Mapping(target = "user.id", source = "userId")
    Address toEntity(AddressResponse dto);
    @Mapping(target = "userId", source = "user.id")

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
