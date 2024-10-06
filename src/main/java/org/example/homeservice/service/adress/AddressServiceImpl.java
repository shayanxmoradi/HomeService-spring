package org.example.homeservice.service.adress;

import org.example.homeservice.dto.AddressReqest;
import org.example.homeservice.dto.AddressResponse;
import org.example.homeservice.dto.mapper.AddressMapper;
import org.example.homeservice.domain.Address;
import org.example.homeservice.repository.address.AddressRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends BaseEntityServiceImpl<Address, Long, AddressRepo, AddressReqest, AddressResponse> implements AddressService {
    private final AddressMapper addressMapper;
    @Autowired
    public AddressServiceImpl(AddressRepo baseRepository, AddressMapper addressMapper) {
        super(baseRepository);
        this.addressMapper = addressMapper;
    }

    @Override
    protected AddressResponse toDto(Address entity) {
        return addressMapper.toResponseDto(entity);
    }

    @Override
    protected Address toEntity(AddressReqest dto) {
        return addressMapper.toEntity(dto);
    }
}
