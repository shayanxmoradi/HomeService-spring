package org.example.homeservice.service.offer;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.domain.service.Offer;
import org.example.homeservice.dto.offer.OfferMapper;
import org.example.homeservice.dto.offer.OfferRequest;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.repository.offer.OfferRepo;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private OfferRepo offerRepo;
    @Mock
    private OrderService orderService;
    @Mock
    private ServiceService serviceService;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private SpeciallistService speciallistService;

    @InjectMocks
    private OfferServiceImpl offerService;

    // Test Data Objects defined with real DTOs
    private OfferRequest offerRequest;
    private OrderResponse orderResponse;
    private SpecialistResponse specialistResponse;
    private ServiceResponse serviceResponse;
    private Offer offer;
    private OfferResponse offerResponseDto;

    @BeforeEach
    void setUp() {
        // Manually set lazy-injected dependencies
        offerService.setOrderService(orderService);
        offerService.setSpeciallistService(speciallistService);

        // Initialize common test data using the real DTO constructors
        offerRequest = new OfferRequest(
                LocalDateTime.now().plusDays(1),
                150.0,
                1L, // orderId
                Duration.ofHours(3),
                10L
        );

        orderResponse = new OrderResponse(
                1L, // xxxxxx
                2L, // customerId
                100L, // serviceId
                3L, // addressId
                "Order description",
                LocalDateTime.now().plusHours(24),
                LocalDateTime.now(),
                150.0,
                OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS,
                null
        );

        specialistResponse = new SpecialistResponse(
                10L, // xxxxxx
                "John", "Doe", "john.doe@example.com",
                SpecialistStatus.APPROVED, 4.8, 50, null, 201L, true
        );

        serviceResponse = new ServiceResponse(
                100L, // xxxxxx
                "General Plumbing", "Fixing leaks",
                100.0f, // basePrice
                null, false, new ArrayList<>(), new ArrayList<>()
        );

        offer = new Offer();
        offer.setId(50L);

        offerResponseDto = new OfferResponse(
                50L, LocalDateTime.now().plusDays(1), 150.0, 1L, 100L,
                LocalDate.now().plusDays(1), Duration.ofHours(3), 10L
        );
    }

    @Nested
    @DisplayName("Save Offer Tests")
    class SaveOfferTests {

        @Test
        @DisplayName("save should succeed when all validations pass")
        void save_whenAllValid_shouldSaveOffer() {
            // Arrange
            when(orderService.findById(anyLong())).thenReturn(Optional.of(orderResponse));
            when(speciallistService.findById(anyLong())).thenReturn(Optional.of(specialistResponse));
            when(offerRepo.findBySpecialistIdAndOrderId(anyLong(), anyLong())).thenReturn(Collections.emptyList());
            when(serviceService.isSpecialistAvailableInService(anyLong(), anyLong())).thenReturn(true);
            when(serviceService.findById(anyLong())).thenReturn(Optional.of(serviceResponse));
            when(offerMapper.toEntity(any(OfferRequest.class))).thenReturn(offer);
            when(offerRepo.save(any(Offer.class))).thenReturn(offer);
            when(offerMapper.toResponse(any(Offer.class))).thenReturn(offerResponseDto);

            // Act
            Optional<OfferResponse> result = offerService.save(offerRequest);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(offerResponseDto.id(), result.get().id());
            verify(offerRepo).save(any(Offer.class));
        }

        @Test
        @DisplayName("save should throw exception when order is not found")
        void save_whenOrderNotFound_shouldThrowException() {
            when(orderService.findById(anyLong())).thenReturn(Optional.empty());
            ValidationException ex = assertThrows(ValidationException.class, () -> offerService.save(offerRequest));
            assertTrue(ex.getMessage().contains("No order found with ID:"));
            verify(offerRepo, never()).save(any());
        }

        @Test
        @DisplayName("save should throw exception when suggested price is too low")
        void save_whenPriceIsTooLow_shouldThrowException() {
            // Arrange
            when(orderService.findById(anyLong())).thenReturn(Optional.of(orderResponse));
            when(speciallistService.findById(anyLong())).thenReturn(Optional.of(specialistResponse));
            when(offerRepo.findBySpecialistIdAndOrderId(anyLong(), anyLong())).thenReturn(Collections.emptyList());
            when(serviceService.isSpecialistAvailableInService(anyLong(), anyLong())).thenReturn(true);
            // Service with a base price higher than the offer
            ServiceResponse expensiveService = new ServiceResponse(
                    100L, "Premium Plumbing", "Advanced work", 200.0f, null, false, new ArrayList<>(), new ArrayList<>()
            );
            when(serviceService.findById(anyLong())).thenReturn(Optional.of(expensiveService));

            // Act & Assert
            ValidationException ex = assertThrows(ValidationException.class, () -> offerService.save(offerRequest));
            assertEquals("base price is greater than offered price", ex.getMessage());
        }
    }
}