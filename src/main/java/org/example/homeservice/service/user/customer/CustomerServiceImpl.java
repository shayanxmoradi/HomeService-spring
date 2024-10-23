package org.example.homeservice.service.user.customer;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.customer.CustomerMapper;
import org.example.homeservice.domain.Customer;
import org.example.homeservice.dto.customer.CustomerRequsetDto;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.updatepassword.UpdatePasswordRequst;
import org.example.homeservice.repository.user.CustomerRepo;
import org.example.homeservice.repository.user.UserSpecification;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.BaseUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class CustomerServiceImpl extends BaseUserServiceImpl<Customer, CustomerRepo, CustomerRequsetDto, CustomerResponseDto> implements CustomerService {
    private final ServiceService serviceService;
    private final OrderService orderService;
    private final CustomerMapper customerMapper;
    private final OfferService offerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(@Qualifier("customerRepo") CustomerRepo customerRepo,
                               ServiceService serviceService, OrderService orderService, CustomerMapper customerMapper, OfferService offerService) {
        super(customerRepo);
        this.serviceService = serviceService;
        this.orderService = orderService;
        this.customerMapper = customerMapper;
        this.offerService = offerService;
    }


    @Override
    public Optional<OrderResponse> registerOrder(OrderRequest orderRequest) {
        return orderService.save(orderRequest);
    }

    @Override
    public List<OrderResponse> getCustomerOrders(CustomerRequsetDto customerRequestDto) {
        Customer customer = baseRepository.findByEmail(customerRequestDto.email())
                .orElseThrow(() -> new ValidationException("Customer not found"));
        return orderService.findByCustomerId(customer.getId());
    }

    @Override
    public Optional<CustomerResponseDto> getCustomerByEmail(String email) {
        return Optional.ofNullable(toDto(baseRepository.findByEmail(email).get()));

    }

    @Override
    public Optional<CustomerResponseDto> save(CustomerRequsetDto dto) {

        if (baseRepository.existsByEmail(dto.email())) {
            throw new ValidationException("Customer with this email already exists");
        }
        Customer customer = customerMapper.toEntity(dto);
        Customer savedCustomer = baseRepository.save(customer);

        return Optional.of(customerMapper.toResponseDto(savedCustomer));
    }

    @Override
    public void updatePassword(UpdatePasswordRequst updatePasswordRequst) {

        Customer specialist = baseRepository.findByEmail(updatePasswordRequst.email())
                .orElseThrow(() -> new ValidationException("user with this email not found"));

        if (!specialist.getPassword().equals(updatePasswordRequst.oldPassword())) {
            throw new ValidationException("Incorrect password");
        }

        specialist.setPassword(updatePasswordRequst.newPassword());
        baseRepository.save(specialist);
    }

    @Override
    public Optional<CustomerResponseDto> findByEmailAndPass(String email, String password) {
        return baseRepository.findByEmailAndPassword(email, password)
                .map(customerMapper::toResponseDto);
    }
    @Override
    public Optional<CustomerResponseDto> findByEmail(String email) {
        return baseRepository.findByEmail(email)
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
        return serviceService.findFirstLayerServices();
    }

    @Override
    public List<OfferResponse> findOfferByOrderId(Long orderId) {
        return offerService.findOfferByOrderId(orderId);
    }

    @Override
    public List<OfferResponse> findByOrderIdOOrderBySuggestedPrice(Long orderId) {
        return offerService.findByOrderIdOOrderBySuggestedPrice(orderId);
    }

    @Override
    public Optional<OrderResponse> choseOrder(Long orderId, Long chosenOfferId) {
        return orderService.choseOrder(orderId, chosenOfferId);
    }

    @Override
    public Optional<OrderResponse> startOrder(Long orderId) {
        return orderService.startOrder(orderId);
    }

    @Override
    public Optional<OrderResponse> endOrder(Long orderId) {
        return orderService.endOrder(orderId);
    }

    @Override
    public List<CustomerResponseDto> filterCustomers(String firstName, String lastName, String email) {
        Specification<Customer> spec = Specification.where(UserSpecification.filterByFirstName(firstName))
                .and(UserSpecification.filterByLastName(lastName))
                .and(UserSpecification.filterByEmail(email));

        return customerMapper.toResponseDto(baseRepository.findAll(spec));

    }

    @Override
    public Optional<CustomerResponseDto> addCustomer(CustomerRequsetDto customerRequsetDto) {
        String password = passwordEncoder.encode(customerRequsetDto.password());
        CustomerRequsetDto updatedCustomer = CustomerRequsetDto.updatePassword(customerRequsetDto, password);
        return save(updatedCustomer);
    }

    @Transactional
    @Override
    public Optional<CustomerResponseDto> activateCustomer(Long customerId) {
        Customer customer = findId(customerId).get();
        customer.setIsActive(true);
        return Optional.ofNullable(customerMapper.toResponseDto(customer));
    }
    Optional<Customer> findId(long id){
       return Optional.ofNullable(baseRepository.findById(id).orElseThrow(() -> new ValidationException("Customer not found")));
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