package org.example.homeservice.service.order;

import jakarta.validation.ValidationException;
import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.domain.enums.SpecialistStatus;
import org.example.homeservice.domain.service.Offer;
import org.example.homeservice.domain.service.Service;
import org.example.homeservice.domain.user.Specialist;
import org.example.homeservice.domain.service.Order;
import org.example.homeservice.dto.address.AddressResponse;
import org.example.homeservice.dto.customer.CustomerResponseDto;
import org.example.homeservice.dto.offer.OfferMapper;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.dto.order.OrderMapper;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistMapper;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.repository.order.OrderRepo;
import org.example.homeservice.service.adress.AddressService;
import org.example.homeservice.service.offer.OfferService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.example.homeservice.service.wallet.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
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
class OrderServiceImplTest {

    @Mock
    private OrderRepo orderRepo;
    @Mock
    private CustomerService customerService;
    @Mock
    private ServiceService serviceService;
    @Mock
    private AddressService addressService;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OfferService offerService;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private SpeciallistService speciallistService;
    @Mock
    private SpecialistMapper specialistMapper;
    @Mock
    private WalletService walletService;

    @InjectMocks
    private OrderServiceImpl orderService;
    //</editor-fold>

    //<editor-fold desc="Test Data">
    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;
    private ServiceResponse serviceResponseDto;
    private ServiceResponse categoryResponseDto;
    private Service serviceEntity; // Added for fixing the NPE
    private AddressResponse addressResponse;
    private CustomerResponseDto customerResponseDto;
    private OfferResponse offerResponse;
    private SpecialistResponse specialistResponse;
    private Offer offer;
    private Specialist specialist;
    //</editor-fold>

    @BeforeEach
    void setUp() {
        orderService.setCustomerService(customerService);
        orderService.setSpeciallistService(speciallistService);

        // Initialize test data
        orderRequest = new OrderRequest(1L, 100L, 200L, "Fix leaky faucet", LocalDateTime.now().plusDays(1), 120.0);

        serviceEntity = new Service();
        serviceEntity.setId(100L);

        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS);
        order.setChoosenService(serviceEntity);

        orderResponse = new OrderResponse(1L, 1L, 100L, 200L, "desc", LocalDateTime.now(), null, 120.0, OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS, null);

        serviceResponseDto = new ServiceResponse(100L, "Plumbing", "desc", 100.0f, null, false, new ArrayList<>(), new ArrayList<>());
        categoryResponseDto = new ServiceResponse(101L, "Home Repairs", "desc", 0.0f, null, true, new ArrayList<>(), new ArrayList<>());
        addressResponse = new AddressResponse(200L, "123 Main St", "Anytown", "Anystate", "12345", 1L);
        customerResponseDto = new CustomerResponseDto(1L, "Jane", "Doe", "jane.doe@example.com",LocalDateTime.now(),1L,true);

        offerResponse = new OfferResponse(300L, LocalDateTime.now().plusDays(1), 110.0, 1L, 100L, null, Duration.ofHours(2), 400L);
        specialistResponse = new SpecialistResponse(400L, "John", "Smith", "j.smith@work.com", SpecialistStatus.APPROVED, 4.9, 10, null, 500L, true);

        offer = new Offer();
        offer.setId(offerResponse.id());
        offer.setSuggestedPrice(offerResponse.suggestedPrice());
        offer.setOfferedTimeToStart(offerResponse.offeredTimeToStart());
        offer.setEstimatedDuration(offerResponse.estimatedDuration());

        specialist = new Specialist();
        specialist.setId(specialistResponse.id());
    }

    @Nested
    @DisplayName("Save Order Tests")
    class SaveOrderTests {

        @Test
        @DisplayName("save should succeed with valid data")
        void save_whenValid_shouldSucceed() {
            // Arrange
            when(orderRepo.findOrderByCustomerIdAndChoosenServiceIdAndServiceTime(anyLong(), anyLong(), any())).thenReturn(Optional.empty());
            when(customerService.findById(anyLong())).thenReturn(Optional.of(customerResponseDto));
            when(serviceService.findById(100L)).thenReturn(Optional.of(serviceResponseDto));
            when(addressService.findById(anyLong())).thenReturn(Optional.of(addressResponse));
            when(orderMapper.toEntity(orderRequest)).thenReturn(order);
            when(orderRepo.save(order)).thenReturn(order);
            // *** FIX for AssertionFailedError ***: Ensure this mock returns a non-null value.
            when(orderMapper.toResponse(order)).thenReturn(orderResponse);

            // Act
            Optional<OrderResponse> result = orderService.save(orderRequest);

            // Assert
            assertTrue(result.isPresent(), "The result should not be an empty Optional.");
            verify(orderRepo).save(order);
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {

        @Test
        @DisplayName("choseOrder should succeed for a valid order and offer")
        void choseOrder_whenValid_shouldUpdateOrder() {
            // Arrange
            when(orderRepo.findById(1L)).thenReturn(Optional.of(order)); // order now has a service attached from setUp()
            when(offerService.findById(300L)).thenReturn(Optional.of(offerResponse));
            when(speciallistService.findById(400L)).thenReturn(Optional.of(specialistResponse));
            when(offerMapper.toEnity(offerResponse)).thenReturn(offer);
            when(specialistMapper.toEntity(specialistResponse)).thenReturn(specialist);
            when(orderRepo.save(any(Order.class))).thenReturn(order);
            when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse); // For the final update() call

            // Act
            orderService.choseOrder(1L, 300L);

            // Assert
            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepo).save(orderCaptor.capture());
            Order updatedOrder = orderCaptor.getValue();

            assertEquals(OrderStatus.WAITING_FOR_SPECIALISTS_DELIVERY, updatedOrder.getStatus());
            assertEquals(offer.getId(), updatedOrder.getChosenOffer().getId());
            assertEquals(specialist.getId(), updatedOrder.getChosenSpecialist().getId());
        }
    }
}