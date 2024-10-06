package org.example.homeservice.service.offer;

import org.example.homeservice.dto.OfferRequest;
import org.example.homeservice.dto.OfferResponse;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.dto.mapper.OfferMapper;
import org.example.homeservice.domain.Offer;
import org.example.homeservice.repository.offer.OfferRepo;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {


    @Mock
    private OfferRepo offerRepo;
    @Mock
    private ServiceService serviceService;
    @Mock
    private OfferMapper offerMapper;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private OfferServiceImpl offerServiceImpl;

    private OfferRequest offerRequest;
    private OfferResponse offerResponse;
    private Offer offer;
    private OrderResponse orderResponse;
    private ServiceResponse serviceResponse;

    @BeforeEach
    void setUp() {
//        MockitoAnnotations.openMocks(this);

        // Initialize test data
        offerRequest = new OfferRequest(LocalDateTime.now(), 200.2, 1l, null, null, null, null);
        offerResponse = new OfferResponse(1L, LocalDateTime.now(), 200.2, 1l,null,null,null, null);
        offer = new Offer();
        orderResponse = new OrderResponse(1L, 1l, 1l, "Description",null,  200.2);
        serviceResponse = new ServiceResponse(1L, "Service 1", "Description", 200.1f, null, false, null, null);
    }

//    @Test
//    void testSaveOffer_ValidData_Success() {
//        when(orderService.findById(offerRequest.orderId())).thenReturn(Optional.of(orderResponse));
//        when(serviceService.findById(offerRequest.serviceId())).thenReturn(Optional.of(serviceResponse));
//        when(offerMapper.toEntity(offerRequest)).thenReturn(offer);
//        when(offerRepo.save(any(Offer.class))).thenReturn(offer);
//        when(offerMapper.toResponse(offer)).thenReturn(offerResponse);
//
//        Optional<OfferResponse> result = offerServiceImpl.save(offerRequest);
//
//        assertTrue(result.isPresent());
//        assertEquals(offerResponse, result.get());
//        verify(offerRepo).save(offer);
//    }

//    @Test
//    void testSaveOffer_OrderNotFound_ThrowsException() {
//        when(orderService.findById(offerRequest.orderId())).thenReturn(Optional.empty());
//
//        ValidationException exception = assertThrows(ValidationException.class, () -> offerServiceImpl.save(offerRequest));
//        assertEquals("no order found", exception.getMessage());
//        verify(offerRepo, never()).save(any(Offer.class));
//    }
//
//    @Test
//    void testSaveOffer_PriceLessThanBase_ThrowsException() {
//        when(orderService.findById(offerRequest.orderId())).thenReturn(Optional.of(orderResponse));
//        when(serviceService.findById(offerRequest.serviceId())).thenReturn(Optional.of(serviceResponse)); // Base price 150, suggested price 100
//
//        // Act & Assert
//        ValidationException exception = assertThrows(ValidationException.class, () -> offerServiceImpl.save(offerRequest));
//        assertEquals("base price is greater than offered price", exception.getMessage());
//        verify(offerRepo, never()).save(any(Offer.class));
//    }

    @Test
    void testFindOfferByOrderId() {
        when(offerRepo.findByOrderId(1L)).thenReturn(List.of(offer));
        when(offerMapper.toResponses(List.of(offer))).thenReturn(List.of(offerResponse));

        List<OfferResponse> result = offerServiceImpl.findOfferByOrderId(1L);

        assertEquals(1, result.size());
        assertEquals(offerResponse, result.get(0));
        verify(offerRepo).findByOrderId(1L);
    }

    @Test
    void testFindByOrderIdOOrderBySuggestedPrice() {
        when(offerRepo.findByOrderIdOrderBySuggestedPrice(1L)).thenReturn(List.of(offer));
        when(offerMapper.toResponses(List.of(offer))).thenReturn(List.of(offerResponse));

        List<OfferResponse> result = offerServiceImpl.findByOrderIdOOrderBySuggestedPrice(1L);

        assertEquals(1, result.size());
        assertEquals(offerResponse, result.get(0));
        verify(offerRepo).findByOrderIdOrderBySuggestedPrice(1L);
    }
}