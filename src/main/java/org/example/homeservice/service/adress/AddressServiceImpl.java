package org.example.homeservice.service.adress;

import org.example.homeservice.dto.AddressReqest;
import org.example.homeservice.dto.AddressResponse;
import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.mapper.AddressMapper;
import org.example.homeservice.dto.mapper.OrderMapper;
import org.example.homeservice.entity.Address;
import org.example.homeservice.entity.Order;
import org.example.homeservice.repository.address.AddressRepo;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.service.baseentity.BaseEntityService;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl extends BaseEntityServiceImpl<Address, Long, AddressRepo, AddressReqest, AddressResponse> implements AddressService {
    private final AddressMapper addressMapper;
    @Autowired
    public AddressServiceImpl(AddressRepo baseRepository, AddressMapper addressMapper) {
        super(baseRepository);
        this.addressMapper = addressMapper;
    }

//    @Override
//    public Optional<AddressResponse> save(AddressReqest dto) {
//        return Optional.ofNullable(addressMapper.toResponseDto(baseRepository.save(addressMapper.toEntity(dto))));
//    }


    @Override
    protected AddressResponse toDto(Address entity) {
        return addressMapper.toResponseDto(entity);
    }

    @Override
    protected Address toEntity(AddressReqest dto) {
        return addressMapper.toEntity(dto);
    }
}
