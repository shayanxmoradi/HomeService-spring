//package org.example.homeservice.service.order;
//
//
//import org.example.homeservice.dto.address.AddressResponse;
//import org.example.homeservice.dto.address.AddressMapper;
//import org.example.homeservice.dto.customer.CustomerResponseDto;
//import org.example.homeservice.dto.offer.OfferMapper;
//import org.example.homeservice.dto.order.OrderMapper;
//import org.example.homeservice.domain.Order;
//import org.example.homeservice.domain.enums.OrderStatus;
//import org.example.homeservice.dto.order.OrderRequest;
//import org.example.homeservice.dto.order.OrderResponse;
//import org.example.homeservice.dto.service.ServiceResponse;
//import org.example.homeservice.repository.order.OrderRepo;
//import org.example.homeservice.service.adress.AddressService;
//import org.example.homeservice.service.offer.OfferService;
//import org.example.homeservice.service.service.ServiceService;
//import org.example.homeservice.service.user.customer.CustomerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class OrderServiceImplTest {
//    @Mock
//    private OrderRepo orderRepo;
//
//    @Mock
//    private CustomerService customerService;
//
//    @Mock
//    private ServiceService serviceService;
//
//    @Mock
//    private AddressService addressService;
//
//    @Mock
//    private OfferService offerService;
//
//    @Mock
//    private OrderMapper orderMapper;
//
//    @Mock
//    private AddressMapper addressMapper;
//
//    @Mock
//    private OfferMapper offerMapper;
//
//    @InjectMocks
//    private OrderServiceImpl orderService;
//    private OrderRequest orderRequest;
//    private Order order;
//    private OrderResponse orderResponse;
//
//    @BeforeEach
//    void setUp() {
//        orderRequest = new OrderRequest(1L, 1L, 1L, "Order description", LocalDateTime.now(), 100.0);
//        order = new Order();
//        order.setId(1L);
//        order.setStatus(OrderStatus.WAITING_FOR_SPECIALISTS_OFFERS);
//
////        orderResponse = new OrderResponse(1L, 1l, 1l,"asdf", LocalDateTime.now().plusDays(2), 222.2);
//    }
//
//    @Test
//    void testSave_Successful() {
//        when(customerService.findById(anyLong())).thenReturn(Optional.of(new CustomerResponseDto(1L, "John", "Doe", "john.doe@example.com",LocalDateTime.now().plusDays(2))));
//        when(serviceService.findById(anyLong())).thenReturn(Optional.of(new ServiceResponse(1L, "Cleaning", "Description", 100f, 1l,true,null,null)));
//        when(addressService.findById(anyLong())).thenReturn(Optional.of(new AddressResponse("chamran", "Street", "City", "12345",1l)));
//        when(orderMapper.toEntity(any(OrderRequest.class))).thenReturn(order);
//        when(orderRepo.save(any(Order.class))).thenReturn(order);
//        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);
//
//        Optional<OrderResponse> result = orderService.save(orderRequest);
//        assertTrue(result.isPresent());
//        assertEquals(orderResponse, result.get());
//
//        verify(orderRepo, times(1)).save(order);
//    }
//
////
////    @Test
////    void testStartOrder_Successful() {
////        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));
//////        order.setChosenOffer(new OfferRequest(LocalDateTime.now().plusDays(2),  22.2, 1l, 1l, null,null,1l));
//////        new SpecialistResponse(1L, "Specialist", "Name", "spec@example.com", SpecialistStatus.APPROVED, 50.0, null)
////        when(orderMapper.toResponse(any(Order.class))).thenReturn(orderResponse);
////
////        Optional<OrderResponse> result = orderService.startOrder(1L);
////
////        assertTrue(result.isPresent());
////        assertEquals(OrderStatus.BEGAN, order.getStatus());
////        verify(orderRepo, times(1)).save(order);
////    }
//
////    @Test
////    void testStartOrder_InvalidStartTime() {
////        order.setChosenOffer(new OfferResponse(1L, LocalDateTime.now().plusMinutes(10), Duration.ofHours(2), 100.0, new SpecialistResponse(1L, "Specialist", "Name", "spec@example.com", SpecialistStatus.APPROVED, 5.2, null)));
////        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));
////
////        assertThrows(ValidationException.class, () -> orderService.startOrder(1L));
////    }
//
////
////    @Test
////    void testSave_CustomerNotFound() {
////        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1L, "sdf", LocalDateTime.now().plusDays(1), 222.2); // Adjust parameters as needed
////        Order order = spy(new Order());
////        when(customerService.findById(orderRequest.customerId())).thenReturn(Optional.empty());
////
////        ValidationException exception = assertThrows(ValidationException.class, () -> orderService.save(orderRequest));
////        assertEquals("Customer with this id not found", exception.getMessage());
////    }
////    @Test
////    void testsave_noAddresrFound() {
////        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1L, "sdf", LocalDateTime.now().plusDays(1), 222.2); // Adjust parameters as needed
////        Order order = spy(new Order());
////        when(addressService.findById(orderRequest.customerId())).thenReturn(Optional.empty());
//////when(customerService.findById(orderRequest.customerId())).thenReturn();
////        ValidationException exception = assertThrows(ValidationException.class, () -> orderService.save(orderRequest));
////        assertEquals("no address with this id found", exception.getMessage());
////    }
//@Test
//void save_shouldReturnOrderResponse_whenValidInput() {
//    // Create a sample OrderRequest
////    OrderRequest orderRequest = new OrderRequest(1L, 1L, 1L, "sdf", LocalDateTime.now().plusDays(1), 222.2);
////
////    // Create mock responses for dependencies
////    ServiceResponse serviceResponse = new ServiceResponse("edari", "nodes", 23.2f, null, false, null, null);
////    AddressResponse addressResponse = new AddressResponse("asdf", "asdf", "ASDF", "12345", 1L);
////   // OrderResponse expectedResponse = new OrderResponse(); // Fill in expected values
////    Order orderToSave = new Order();
////    // Set up orderToSave based on orderRequest, as done previously.
////
////    // Mocking service calls
////    when(customerService.findById(orderRequest.customerId()))
////            .thenReturn(Optional.of(new CustomerResponseDto(1L, "shayan", "moradi", "asdf@gmail.com", null, null)));
////    when(serviceService.findById(orderRequest.serviceId()))
////            .thenReturn(Optional.of(serviceResponse));
////    when(addressService.findById(orderRequest.addressId()))
////            .thenReturn(Optional.of(addressResponse));
////    when(orderMapper.toEntity(orderRequest)).thenReturn(orderToSave);
////    when(baseRepository.save(orderToSave)).thenReturn(orderToSave);
////    when(orderMapper.toResponse(orderToSave)).thenReturn(expectedResponse);
////
////    // Execute the save operation
////    Optional<OrderResponse> actualResponse = orderService.save(orderRequest);
////
////    assertTrue(actualResponse.isPresent(), "Expected a non-empty response.");
////    assertEquals(expectedResponse, actualResponse.get(), "The actual response did not match the expected response.");
//}
//
//}