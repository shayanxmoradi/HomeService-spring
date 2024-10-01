package org.example.homeservice.service.order;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.AddressResponse;
import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.dto.mapper.AddressMapper;
import org.example.homeservice.dto.mapper.OrderMapper;
import org.example.homeservice.entity.Order;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.service.adress.AddressService;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl extends BaseEntityServiceImpl<Order, Long, OrderRepo, OrderRequest, OrderResponse> implements OrderService {
   private  CustomerService customerService;
   private final ServiceService serviceService;
   private final AddressService addressService;
   private final OrderMapper orderMapper;
   private final AddressMapper addressMapper;
    @Autowired
    public OrderServiceImpl(OrderRepo baseRepository, ServiceService serviceService, AddressService addressService, OrderMapper orderMapper, AddressMapper addressMapper) {
        super(baseRepository);
        this.serviceService = serviceService;
        this.addressService = addressService;
        this.orderMapper = orderMapper;
        this.addressMapper = addressMapper;
    }
    @Autowired
    public void setCustomerService(@Lazy CustomerService customerService) {
        this.customerService = customerService;
    }

//    @Override
//    public Optional<OrderResponse> registerOrder(OrderRequest orderRequest) {
//        if (customerService.findById(orderRequest.customerId()).isEmpty()) {
//            throw new ValidationException("Customer with this id not found");
//        }
//        Optional<ServiceResponse> foundService = serviceService.findById(orderRequest.serviceId());
//        if (foundService.isEmpty()) {
//            throw new ValidationException("Order cannot be null");
//        } else if (foundService.get().category()==true) {
//            throw new ValidationException("chosenService is not really service its just as category for other services");
//
//        }
//        if (addressService.findById(orderRequest.addressId()).isEmpty()) {
//            throw new ValidationException("no address with this id found");
//        }
//        if (foundService.get().basePrice()> orderRequest.offeredPrice()) throw new ValidationException("base price is greater than offered price");
//
////todo saving
//       return Optional.ofNullable(orderMapper.toResponse(baseRepository.save(orderMapper.toEntity(orderRequest))));
//
//    //watchout saved order shoud be added to customer to
//    }

    @Override
    public Optional<OrderResponse> save(OrderRequest orderRequest) {
        if (customerService.findById(orderRequest.customerId()).isEmpty()) {
            throw new ValidationException("Customer with this id not found");
        }
        Optional<ServiceResponse> foundService = serviceService.findById(orderRequest.serviceId());
        if (foundService.isEmpty()) {
            throw new ValidationException("Order cannot be null");
        } else if (foundService.get().category()==true) {
            throw new ValidationException("chosenService is not really service its just as category for other services");

        }
        Optional<AddressResponse> foundedAddress = addressService.findById(orderRequest.addressId());
        if (foundedAddress.isEmpty()) {
            throw new ValidationException("no address with this id found");
        }
        if (foundService.get().basePrice()> orderRequest.offeredPrice()) throw new ValidationException("base price is greater than offered price");

//todo saving in customer side
        //todo tekraari


      return Optional.ofNullable(orderMapper.toResponse(baseRepository.save(orderMapper.toEntity(orderRequest))));
    }

    @Override
    protected OrderResponse toDto(Order entity) {
        return orderMapper.toResponse(entity);
    }

    @Override
    protected Order toEntity(OrderRequest dto) {
        return orderMapper.toEntity(dto);
    }
}
