package org.example.homeservice.service.user;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.UpdatePasswordRequst;
import org.example.homeservice.dto.mapper.CustomerMapper;
import org.example.homeservice.dto.CustomerRequsetDto;
import org.example.homeservice.dto.CustomerResponseDto;
import org.example.homeservice.entites.Customer;
import org.example.homeservice.entites.Order;
import org.example.homeservice.entites.Service;
import org.example.homeservice.repository.user.CustomerRepo;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.repository.service.ServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CustomerServiceImpl extends BaseUserServiceImpl<Customer,CustomerRepo, CustomerRequsetDto, CustomerResponseDto> implements CustomerService {
    private final ServiceRepo serviceRepo;
    private final OrderRepo orderRepo;

    @Autowired
    public CustomerServiceImpl(@Qualifier("customerRepo") CustomerRepo customerRepo,
                               ServiceRepo serviceRepo,
                               OrderRepo orderRepo) {
        super(customerRepo);
        this.serviceRepo = serviceRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public Optional<List<Service>> getAllFirstLayerServices() {
        List<Service> services = serviceRepo.findFirstLayerServices();
        return services.isEmpty() ? Optional.empty() : Optional.of(services);
    }

    @Override
    public Order registerOrder(CustomerRequsetDto customerRequestDto, Order order) {
        if (customerRequestDto == null) {
            throw new ValidationException("Customer cannot be null");
        }
        if (order == null) {
            throw new ValidationException("Order cannot be null");
        }

        Customer customer = baseRepository.findByEmail(customerRequestDto.email())
                .orElseThrow(() -> new ValidationException("Customer not found"));

        customer.addOrder(order);
        order.setCustomer(customer);

        Order savedOrder = orderRepo.save(order);
        baseRepository.save(customer); // Assuming this updates the customer entity
        return savedOrder;
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

        // Convert DTO to entity and save
        Customer customer = CustomerMapper.INSTANCE.toEntity(dto);
        Customer savedCustomer = baseRepository.save(customer);
        return Optional.of(CustomerMapper.INSTANCE.toResponseDto(savedCustomer));
    }

    public Service navigateAndSelectService() throws ServiceNotFoundException {
        List<Service> topLevelServices = serviceRepo.findFirstLayerServices();
        if (topLevelServices.isEmpty()) {
            throw new ServiceNotFoundException("No services available.");
        }
        return navigate(null, topLevelServices);
    }

//    @Override
//    public void updatePassword(UpdatePasswordRequst updatePasswordRequst) {
//        Customer customer = baseRepository.findByEmail(updatePasswordRequst.email())
//                .orElseThrow(() -> new ValidationException("Customer not found"));
//
//        if (!customer.getPassword().equals(updatePasswordRequst.oldPassword())) {
//            throw new ValidationException("Incorrect password");
//        }
//
//        customer.setPassword(updatePasswordRequst.newPassword());
//        baseRepository.save(customer);
//    }

    @Override
    public Optional<CustomerResponseDto> findByEmailAndPass(String email, String password) {
        return baseRepository.findByEmailAndPassword(email, password)
                .map(CustomerMapper.INSTANCE::toResponseDto);
    }

    // Private method to navigate services
    private Service navigate(Service parentService, List<Service> subServices) {
        if (parentService != null && !parentService.isCategory()) {
            return parentService; // Return selected service
        }

        if (parentService == null) {
            // No parent service, showing top-level categories
            throw new ValidationException("No subservices available");
        } else {
            throw new ValidationException("Category: " + parentService.getName());
        }
    }

    @Override
    public boolean emailExists(String email) {
        return baseRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<CustomerResponseDto> login(String email, String password) {
        return Optional.ofNullable(toDto(baseRepository.findByEmailAndPassword(email, password).get()));

    }
}