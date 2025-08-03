package org.example.homeservice.service.user.customer;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.user.Customer;
import org.example.homeservice.dto.customer.CustomerMapper;
import org.example.homeservice.dto.customer.CustomerRequsetDto;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.repository.user.CustomerRepo;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepo customerRepo;
    @Mock
    private ServiceService serviceService;
    @Mock
    private OrderService orderService;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private OfferService offerService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerRequsetDto customerRequestDto;
    private Customer customer;

    @BeforeEach
    void setUp() {

        ReflectionTestUtils.setField(customerService, "passwordEncoder", passwordEncoder);

        customerRequestDto = new CustomerRequsetDto(1L, "Jane", "Doe", "jane.doe@example.com", "password123");
        customer = new Customer();
        customer.setId(1L);
        customer.setEmail(customerRequestDto.email());
    }

    @Nested
    @DisplayName("Save and Add Customer Tests")
    class SaveTests {
        @Test
        @DisplayName("save should fail if email already exists")
        void save_whenEmailExists_shouldThrowException() {
            when(customerRepo.existsByEmail(anyString())).thenReturn(true);
            assertThrows(ValidationException.class, () -> customerService.save(customerRequestDto));
            verify(customerRepo, never()).save(any());
        }

        @Test
        @DisplayName("addCustomer should encode password and call save")
        void addCustomer_shouldEncodePasswordAndCallSave() {
            // Arrange
            String encodedPassword = "encodedPassword";
            when(passwordEncoder.encode("password123")).thenReturn(encodedPassword);
            // Use doReturn for a spy to avoid calling the real save method
            doReturn(Optional.empty()).when(customerService).save(any(CustomerRequsetDto.class));

            // Act
            customerService.addCustomer(customerRequestDto);

            // Assert
            ArgumentCaptor<CustomerRequsetDto> dtoCaptor = ArgumentCaptor.forClass(CustomerRequsetDto.class);
            // Verify that the internal 'save' method was called
            verify(customerService).save(dtoCaptor.capture());
            // Verify that the password in the captured DTO is the encoded one
            assertEquals(encodedPassword, dtoCaptor.getValue().password());
        }
    }

    @Nested
    @DisplayName("Service Delegation Tests")
    class DelegationTests {
        @Test
        @DisplayName("registerOrder should delegate to OrderService")
        void registerOrder_shouldDelegate() {
            customerService.registerOrder(null); // The request object itself doesn't matter for this test
            verify(orderService).save(any());
        }

        @Test
        @DisplayName("findAllServices should delegate to ServiceService")
        void findAllServices_shouldDelegate() {
            customerService.findAllServices();
            verify(serviceService).findAll();
        }

        @Test
        @DisplayName("findOfferByOrderId should delegate to OfferService")
        void findOfferByOrderId_shouldDelegate() {
            customerService.findOfferByOrderId(1L);
            verify(offerService).findOfferByOrderId(1L);
        }
    }
}