package org.example.homeservice.service.adress;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.dto.address.AddressMapper;
import org.example.homeservice.domain.Address;
import org.example.homeservice.repository.address.AddressRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class AddressServiceImpl extends BaseEntityServiceImpl<Address, Long, AddressRepo, AddressReqest, AddressResponse> implements AddressService {
    private final AddressMapper addressMapper;
    private  CustomerService customerService;
    @Autowired
    public AddressServiceImpl(AddressRepo baseRepository, AddressMapper addressMapper) {
        super(baseRepository);
        this.addressMapper = addressMapper;
    }
@Autowired
    public void setCustomerService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Optional<AddressResponse> save(AddressReqest dto) {
        customerService.findById(dto.userId()).orElseThrow(() -> new ValidationException("Customer with "+dto.userId()+" not found"));

        return super.save(dto);
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
