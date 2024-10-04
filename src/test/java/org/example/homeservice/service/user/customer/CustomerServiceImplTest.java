package org.example.homeservice.service.user.customer;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.*;
import org.example.homeservice.dto.mapper.CustomerMapper;
import org.example.homeservice.entity.Customer;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.repository.user.CustomerRepo;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private OrderRepo orderRepo; // Assuming you have a mocked OrderRepo

    @Mock
    private ServiceService serviceService;

    @Mock
    private OrderService orderService;

//    @Mock
//    private CustomerMapper customerMapper;

    private List<OrderResponse> orders;

    @Spy
    private CustomerMapper customerMapper = Mappers.getMapper(CustomerMapper.class);


    @Mock
    private OfferService offerService;

    @InjectMocks
    private CustomerServiceImpl underTest;

    private Customer customer;
    private CustomerRequsetDto customerRequestDto;
    private CustomerResponseDto customerResponseDto;

    @BeforeEach
    public void setUp() {
       // MockitoAnnotations.openMocks(this);

        CustomerRequsetDto customerRequestDto = new CustomerRequsetDto("John", "Doe", "john.doe@example.com", "1234567d");

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        orders = java.util.List.of(
                new OrderResponse(1L, 2L, 3L, "Order 1", LocalDateTime.now(), 100.0),
                new OrderResponse(2L, 3L, 4L, "Order 2", LocalDateTime.now().plusDays(1), 200.0)
        );
    }

@Test
void registerCustomer() {
    CustomerRequsetDto customerRequestDto = new CustomerRequsetDto("John", "Doe", "john.doe@example.com", "1234323d");

    given(customerRepo.findByEmail(customerRequestDto.email())).willReturn(Optional.ofNullable(customer));
    given(orderService.findByCustomerId(customer.getId())).willReturn(orders);
    List<OrderResponse> result = underTest.getCustomerOrders(customerRequestDto);

    assertEquals(orders, result);
    verify(customerRepo, times(1)).findByEmail(customerRequestDto.email());
    verify(orderService, times(1)).findByCustomerId(customer.getId());
    }

    @Test
    void cantGetCustomerOrders() {
        CustomerRequsetDto customerRequestDto = new CustomerRequsetDto("John", "Doe", "john.doe@example.com", "1234323d");

        given(customerRepo.findByEmail(customerRequestDto.email())).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getCustomerOrders(customerRequestDto))
                .isExactlyInstanceOf(ValidationException.class)
                .hasMessage("Customer not found");  // Optionally, you can also check the exception message

        verify(customerRepo, times(1)).findByEmail(customerRequestDto.email());
    }


    @Test
    void itCanRegisterCustomer() {

        CustomerRequsetDto customerRequestDto = new CustomerRequsetDto("John", "Doe", "john.doe@example.com", "1234567d");
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("1234567d");

        CustomerResponseDto customerResponseDto = new CustomerResponseDto(1L, "John", "Doe", "john.doe@example.com", null, new Time(System.currentTimeMillis()));

        given(customerRepo.existsByEmail(customerRequestDto.email())).willReturn(false);
        given(customerMapper.toEntity(customerRequestDto)).willReturn(customer);
        given(customerRepo.save(any(Customer.class))).willReturn(customer);
//        given(customerMapper.toResponseDto(customer)).willReturn(customerResponseDto); // Simulate mapping to response DTO
        given(customerMapper.toResponseDto(any(Customer.class))).willReturn(customerResponseDto);


        Optional<CustomerResponseDto> result = underTest.save(customerRequestDto);


        assertTrue(result.isPresent());
        assertEquals(customerResponseDto, result.get());

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepo).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertNotNull(capturedCustomer);
        assertEquals("John", capturedCustomer.getFirstName());
        assertEquals("Doe", capturedCustomer.getLastName());
        assertEquals("john.doe@example.com", capturedCustomer.getEmail());
//        assertEquals("password123", capturedCustomer.getPassword());
    }
    @Test
    void itThrowsExceptionWhenEmailExists() {
        CustomerRequsetDto customerRequestDto = new CustomerRequsetDto("John", "Doe", "john.doe@example.com", "1234323d");
        given(customerRepo.existsByEmail(customerRequestDto.email())).willReturn(true);

        assertThatThrownBy(()-> underTest.save(customerRequestDto)).isExactlyInstanceOf(ValidationException.class);

      //  assertThrows(ValidationException.class, () -> underTest.save(customerRequestDto));
    }
    @Test
    void canFindCustomerByEmail() {
        Customer customer = new Customer();
        customer.setEmail("john.doe@example.com");
        CustomerResponseDto responseDto = new CustomerResponseDto(1L, "John", "Doe", "john.doe@example.com", null, null);

        given(customerRepo.findByEmail("john.doe@example.com")).willReturn(Optional.of(customer));
        given(customerMapper.toResponseDto(customer)).willReturn(responseDto);

        Optional<CustomerResponseDto> result = underTest.getCustomerByEmail("john.doe@example.com");

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(responseDto);
    }

    @Test
    void canFindAllServices() {
        List<ServiceResponse> services = List.of(new ServiceResponse(1L, "Service", "Desc", 22.2f, null, false, null,null));
        given(serviceService.findAll()).willReturn(Optional.of(services));

        Optional<List<ServiceResponse>> result = underTest.findAllServices();

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(services);
    }

    @Test
    public void testSave_CustomerExists() {
        CustomerRequsetDto customerRequestDto = new CustomerRequsetDto("John", "Doe", "john.doe@example.com", "1234567d");

        when(customerRepo.existsByEmail(customerRequestDto.email())).thenReturn(true);

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            underTest.save(customerRequestDto);
        });

        assertEquals("Customer with this email already exists", thrown.getMessage());
        verify(customerRepo, never()).save(any(Customer.class));
    }
    @Test
    void toDto() {
        when(customerMapper.toResponseDto(customer)).thenReturn(customerResponseDto);

        CustomerResponseDto actualResponse = underTest.toDto(customer);

        assertEquals(customerResponseDto, actualResponse);
        verify(customerMapper).toResponseDto(customer);
    }

    @Test
    void toEntity() {
        // Arrange
        when(customerMapper.toEntity(customerRequestDto)).thenReturn(customer);

        Customer actualEntity = underTest.toEntity(customerRequestDto);


        assertEquals(customer, actualEntity);
        verify(customerRepo, never()).save(any(Customer.class));
    }
    @Test
    void canStartOrder() {
        Long orderId = 1L;
        OrderResponse orderResponse = new OrderResponse(orderId, 1L, 1L, "description", null, 100.0);
        given(orderService.startOrder(orderId)).willReturn(Optional.of(orderResponse));

        Optional<OrderResponse> result = underTest.startOrder(orderId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderResponse);
        verify(orderService, times(1)).startOrder(orderId);
    }
    @Test
    void canEndOrder() {
        Long orderId = 1L;
        OrderResponse orderResponse = new OrderResponse(orderId, 1L, 1L, "description", null, 100.0);
        given(orderService.endOrder(orderId)).willReturn(Optional.of(orderResponse));

        Optional<OrderResponse> result = underTest.endOrder(orderId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderResponse);
        verify(orderService, times(1)).endOrder(orderId);
    }

    @Test
    void canChoseOrder() {
        Long orderId = 1L;
        Long chosenOfferId = 1L;
        OrderResponse orderResponse = new OrderResponse(orderId, 1L, 1L, "description", null, 100.0);
        given(orderService.choseOrder(orderId, chosenOfferId)).willReturn(Optional.of(orderResponse));

        Optional<OrderResponse> result = underTest.choseOrder(orderId, chosenOfferId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderResponse);
        verify(orderService, times(1)).choseOrder(orderId, chosenOfferId);
    }

    // Tests for other service delegation methods

    @Test
    void canFindRealServices() {
        List<ServiceResponse> services = List.of(new ServiceResponse(1L, "Service", "Desc", null, null, false
                ,null, null));
        given(serviceService.findRealServices()).willReturn(services);

        List<ServiceResponse> result = underTest.findRealServices();

        assertThat(result).isEqualTo(services);
        verify(serviceService, times(1)).findRealServices();
    }

    @Test
    void canFindFirstLayerServices() {
        List<ServiceResponse> services = List.of(new ServiceResponse(1L, "First Layer Service", "Desc", null, null, false,null, null));
        given(serviceService.findFirstLayerServices()).willReturn(services);

        List<ServiceResponse> result = underTest.findFirstLayerServices();

        assertThat(result).isEqualTo(services);
        verify(serviceService, times(1)).findFirstLayerServices();
    }

    @Test
    void canFindOffersByOrderId() {
        Long orderId = 1L;
        List<OfferResponse> offers = List.of(new OfferResponse(1L, LocalDateTime.now(), 100.0, null, null, null,null,1l));
        given(offerService.findOfferByOrderId(orderId)).willReturn(offers);

        List<OfferResponse> result = underTest.findOfferByOrderId(orderId);

        assertThat(result).isEqualTo(offers);
        verify(offerService, times(1)).findOfferByOrderId(orderId);
    }

    @Test
    void canFindOffersByOrderIdOrderedByPrice() {
        Long orderId = 1L;
        List<OfferResponse> offers = List.of(new OfferResponse(1L, LocalDateTime.now(), 100.0, null, null, null,null,1l));
        given(offerService.findByOrderIdOOrderBySuggestedPrice(orderId)).willReturn(offers);

        List<OfferResponse> result = underTest.findByOrderIdOOrderBySuggestedPrice(orderId);

        assertThat(result).isEqualTo(offers);
        verify(offerService, times(1)).findByOrderIdOOrderBySuggestedPrice(orderId);
    }

    // Test for emailExists method

    @Test
    void emailExistsReturnsTrueIfExists() {
        String email = "test@example.com";
        given(customerRepo.findByEmail(email)).willReturn(Optional.of(new Customer()));

        boolean result = underTest.emailExists(email);

        assertThat(result).isTrue();
        verify(customerRepo, times(1)).findByEmail(email);
    }

    @Test
    void emailExistsReturnsFalseIfNotExists() {
        String email = "test@example.com";
        given(customerRepo.findByEmail(email)).willReturn(Optional.empty());

        boolean result = underTest.emailExists(email);

        assertThat(result).isFalse();
        verify(customerRepo, times(1)).findByEmail(email);
    }

    // Test for login method

    @Test
    void canLoginWithValidCredentials() {
        String email = "john.doe@example.com";
        String password = "password";
        Customer customer = new Customer();
        CustomerResponseDto responseDto = new CustomerResponseDto(1L, "John", "Doe", email, null, null);
        given(customerRepo.findByEmailAndPassword(email, password)).willReturn(Optional.of(customer));
        given(customerMapper.toResponseDto(customer)).willReturn(responseDto);

        Optional<CustomerResponseDto> result = underTest.login(email, password);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(responseDto);
        verify(customerRepo, times(1)).findByEmailAndPassword(email, password);
    }

//    @Test
//    void loginReturnsEmptyForInvalidCredentials() {
//        String email = "john.doe@example.com";
//        String password = "wrongpassword";
//        given(customerRepo.findByEmailAndPassword(email, password)).willReturn(Optional.empty());
//
//        Optional<CustomerResponseDto> result = underTest.login(email, password);
//
//        assertThat(result).isEmpty();
//        verify(customerRepo, times(1)).findByEmailAndPassword(email, password);
//    }
}