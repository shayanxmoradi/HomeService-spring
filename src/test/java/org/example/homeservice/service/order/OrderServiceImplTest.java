package org.example.homeservice.service.order;


import org.example.homeservice.dto.*;
import org.example.homeservice.dto.mapper.OrderMapper;
import org.example.homeservice.entity.Address;
import org.example.homeservice.entity.Customer;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.example.homeservice.service.adress.AddressService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private CustomerService customerService;

    @Mock
    private ServiceService serviceService;

    @Mock
    private AddressService addressService;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private BaseEnitityRepo baseRepository;

    @InjectMocks
    private OrderServiceImpl orderService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }//

//    @Test
//    void testSave_CustomerNotFound() {
//        OrderRequest orderRequest = new OrderRequest(1L, 1L, 1L, "sdf", LocalDateTime.now().plusDays(1), 222.2); // Adjust parameters as needed
//        Order order = spy(new Order());
//        when(mockCustomerService.findById(orderRequest.customerId())).thenReturn(Optional.empty());
//
//        ValidationException exception = assertThrows(ValidationException.class, () -> orderService.save(orderRequest));
//        assertEquals("Customer with this id not found", exception.getMessage());
  //  }
@Test
void save_shouldReturnOrderResponse_whenValidInput() {
    // Create a sample OrderRequest
//    OrderRequest orderRequest = new OrderRequest(1L, 1L, 1L, "sdf", LocalDateTime.now().plusDays(1), 222.2);
//
//    // Create mock responses for dependencies
//    ServiceResponse serviceResponse = new ServiceResponse("edari", "nodes", 23.2f, null, false, null, null);
//    AddressResponse addressResponse = new AddressResponse("asdf", "asdf", "ASDF", "12345", 1L);
//   // OrderResponse expectedResponse = new OrderResponse(); // Fill in expected values
//    Order orderToSave = new Order();
//    // Set up orderToSave based on orderRequest, as done previously.
//
//    // Mocking service calls
//    when(customerService.findById(orderRequest.customerId()))
//            .thenReturn(Optional.of(new CustomerResponseDto(1L, "shayan", "moradi", "asdf@gmail.com", null, null)));
//    when(serviceService.findById(orderRequest.serviceId()))
//            .thenReturn(Optional.of(serviceResponse));
//    when(addressService.findById(orderRequest.addressId()))
//            .thenReturn(Optional.of(addressResponse));
//    when(orderMapper.toEntity(orderRequest)).thenReturn(orderToSave);
//    when(baseRepository.save(orderToSave)).thenReturn(orderToSave);
//    when(orderMapper.toResponse(orderToSave)).thenReturn(expectedResponse);
//
//    // Execute the save operation
//    Optional<OrderResponse> actualResponse = orderService.save(orderRequest);
//
//    assertTrue(actualResponse.isPresent(), "Expected a non-empty response.");
//    assertEquals(expectedResponse, actualResponse.get(), "The actual response did not match the expected response.");
}

}