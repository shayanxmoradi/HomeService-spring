package org.example.homeservice.service.user.customer;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.*;
import org.example.homeservice.dto.mapper.CustomerMapper;
import org.example.homeservice.entity.Customer;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.example.homeservice.repository.user.CustomerRepo;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.repository.service.ServiceRepo;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.BaseUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CustomerServiceImpl extends BaseUserServiceImpl<Customer, CustomerRepo, CustomerRequsetDto, CustomerResponseDto> implements CustomerService {
    private final OrderRepo orderRepo;//todo change to service
    private final ServiceService serviceService;
    private final OrderService orderService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(@Qualifier("customerRepo") CustomerRepo customerRepo,
                               OrderRepo orderRepo, ServiceService serviceService, OrderService orderService, CustomerMapper customerMapper) {
        super(customerRepo);
        this.orderRepo = orderRepo;
        this.serviceService = serviceService;
        this.orderService = orderService;
        this.customerMapper = customerMapper;
    }


    @Override
    public Optional<OrderResponse> registerOrder(OrderRequest orderRequest) {
        return orderService.save(orderRequest);
    }

    @Override
    public List<Order> getCustomerOrders(CustomerRequsetDto customerRequestDto) {
        Customer customer = baseRepository.findByEmail(customerRequestDto.email())
                .orElseThrow(() -> new ValidationException("Customer not found"));
        return orderRepo.findByCustomerId(customer.getId());
    }

    @Override
    public Optional<CustomerResponseDto> getCustomerByEmail(String email) {
        return Optional.ofNullable(toDto(baseRepository.findByEmail(email).get()));

    }

    @Override
    public Optional<CustomerResponseDto> save(CustomerRequsetDto dto) {
        // Check if email already exists
        if (baseRepository.findByEmail(dto.email()).isPresent()) {
            throw new ValidationException("Customer with this email already exists");
        }
        Customer customer = customerMapper.toEntity(dto);
        Customer savedCustomer = baseRepository.save(customer);
        return Optional.of(customerMapper.toResponseDto(savedCustomer));
    }


    @Override
    public Optional<CustomerResponseDto> findByEmailAndPass(String email, String password) {
        return baseRepository.findByEmailAndPassword(email, password)
                .map(customerMapper::toResponseDto);
    }

    @Override
    public Optional<List<ServiceResponse>> findAllServices() {
        return serviceService.findAll();
    }

    @Override
    public List<ServiceResponse> findAllByParentId(Long parentId) {
        return serviceService.findAllByParentId(parentId);
    }

    @Override
    public List<ServiceResponse> findRealServices() {
        return serviceService.findRealServices();
    }

    @Override
    public List<ServiceResponse> findFirstLayerServices() {
        return  serviceService.findFirstLayerServices();
    }


    @Override
    public boolean emailExists(String email) {
        return baseRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<CustomerResponseDto> login(String email, String password) {
        return Optional.ofNullable(toDto(baseRepository.findByEmailAndPassword(email, password).get()));

    }

    @Override
    protected Customer toEntity(CustomerRequsetDto dto) {
        return customerMapper.toEntity(dto);
    }

    @Override
    protected CustomerResponseDto toDto(Customer entity) {
        return customerMapper.toResponseDto(entity);
    }
}