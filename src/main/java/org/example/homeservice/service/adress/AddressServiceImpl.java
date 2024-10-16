package org.example.homeservice.service.adress;

import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.example.homeservice.domain.Order;
import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.dto.address.AddressMapper;
import org.example.homeservice.domain.Address;
import org.example.homeservice.repository.address.AddressRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class AddressServiceImpl extends BaseEntityServiceImpl<Address, Long, AddressRepo, AddressReqest, AddressResponse> implements AddressService {
    private final AddressMapper addressMapper;
    private  CustomerService customerService;
    private final OrderService orderService;

    @Autowired
    public AddressServiceImpl(AddressRepo baseRepository, AddressMapper addressMapper, @Lazy OrderService orderService) {
        super(baseRepository);
        this.addressMapper = addressMapper;
        this.orderService = orderService;
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

//    @Override
//    public Optional<AddressResponse> update(AddressReqest dto) {
//        return super.update(dto);
//    }
    //    @Transactional
//    @Override
//    public boolean deleteById(Long aLong) {
//        System.out.println("here");
//            // First, find all orders referencing this address
//            List<Order> orders = orderService.findByAdrressId(aLong);
//
//            for (Order order : orders) {
//                order.setAddress(null);
//            }
//
//
//            return super.deleteById(aLong);
//        }


    @Override
    protected AddressResponse toDto(Address entity) {
        return addressMapper.toResponseDto(entity);
    }

    @Override
    protected Address toEntity(AddressReqest dto) {
        return addressMapper.toEntity(dto);
    }
}
