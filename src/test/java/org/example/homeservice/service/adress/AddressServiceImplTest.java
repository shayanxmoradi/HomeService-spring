package org.example.homeservice.service.adress;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.user.Address;
import org.example.homeservice.dto.address.AddressMapper;
import org.example.homeservice.dto.address.AddressReqest;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.repository.address.AddressRepo;
import org.example.homeservice.service.user.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceImplTest {

    @Mock
    private AddressRepo addressRepo;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private CustomerService customerService;
    @Mock
    private org.example.homeservice.service.order.OrderService orderService;

    @InjectMocks
    private AddressServiceImpl addressService;

    private AddressReqest addressReqest;
    private Address address;
    private AddressResponse addressResponse;
    private CustomerResponseDto customerResponseDto;

    @BeforeEach
    void setUp() {
        addressService.setCustomerService(customerService);

        addressReqest = new AddressReqest(
                null,
                "123 Main St",
                "Anytown",
                "Anystate",
                "12345",
                1L
        );

        address = new Address();
        address.setId(100L);
        address.setCity("Anytown");

        addressResponse = new AddressResponse(
                100L,
                "123 Main St",
                "Anytown",
                "Anystate",
                "12345",
                1L
        );


        customerResponseDto = new CustomerResponseDto(1L, "John", "Doe", "john.doe@example.com", LocalDateTime.now(),1L,true);

    }

    @Nested
    @DisplayName("Save Address Tests")
    class SaveAddressTests {

        @Test
        @DisplayName("save should succeed when customer exists")
        void save_whenCustomerExists_shouldSaveAddress() {
            // Arrange
            // This line is now safe because customerResponseDto is initialized
            when(customerService.findById(addressReqest.userId())).thenReturn(Optional.of(customerResponseDto));
            when(addressMapper.toEntity(addressReqest)).thenReturn(address);
            when(addressRepo.save(address)).thenReturn(address);
            when(addressMapper.toResponseDto(address)).thenReturn(addressResponse);

            // Act
            Optional<AddressResponse> result = addressService.save(addressReqest);

            // Assert
            verify(customerService).findById(addressReqest.userId());
            verify(addressRepo).save(address);

            assertTrue(result.isPresent());
            assertEquals(addressResponse, result.get());
            assertEquals("Anytown", result.get().city());
        }

        @Test
        @DisplayName("save should throw ValidationException when customer is not found")
        void save_whenCustomerNotFound_shouldThrowValidationException() {
            // Arrange
            long nonExistentUserId = 99L;
            AddressReqest requestWithInvalidUser = new AddressReqest(
                    null, "Test Street", "Test City", "Test State", "99999", nonExistentUserId
            );

            when(customerService.findById(nonExistentUserId)).thenReturn(Optional.empty());

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                addressService.save(requestWithInvalidUser);
            });

            assertEquals("Customer with " + nonExistentUserId + " not found", exception.getMessage());
            verify(addressRepo, never()).save(any(Address.class));
        }
    }

    @Nested
    @DisplayName("Mapper Delegation Tests")
    class MapperDelegationTests {

        @Test
        @DisplayName("toDto should delegate to AddressMapper")
        void toDto_shouldDelegateToMapper() {
            // Arrange
            when(addressMapper.toResponseDto(address)).thenReturn(addressResponse);

            // Act
            AddressResponse result = ReflectionTestUtils.invokeMethod(addressService, "toDto", address);

            // Assert
            verify(addressMapper).toResponseDto(address);
            assertNotNull(result);
            assertEquals(addressResponse.id(), result.id());
        }

        @Test
        @DisplayName("toEntity should delegate to AddressMapper")
        void toEntity_shouldDelegateToMapper() {
            // Arrange
            when(addressMapper.toEntity(addressReqest)).thenReturn(address);

            // Act
            Address result = ReflectionTestUtils.invokeMethod(addressService, "toEntity", addressReqest);

            // Assert
            verify(addressMapper).toEntity(addressReqest);
            assertNotNull(result);
            assertEquals(address.getCity(), result.getCity());
        }
    }
}

