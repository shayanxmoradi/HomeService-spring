package org.example.homeservice.service.user.customer;



import org.example.homeservice.domain.Customer;
import org.example.homeservice.dto.*;
import org.example.homeservice.service.user.BaseUserService;

import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseUserService<Customer, CustomerRequsetDto, CustomerResponseDto> {

    Optional<OrderResponse> registerOrder(OrderRequest orderRequest);

    List<OrderResponse> getCustomerOrders(CustomerRequsetDto customerRequsetDto);

    Optional<CustomerResponseDto> getCustomerByEmail(String email);
    Optional<CustomerResponseDto> findByEmailAndPass(String username, String password);

  //  public void updatePassword(UpdatePasswordRequst updatePasswordRequst);


    //services
    Optional<List<ServiceResponse>> findAllServices();
    List<ServiceResponse> findAllByParentId(Long parentId);
    List<ServiceResponse> findRealServices();
    List<ServiceResponse> findFirstLayerServices();

    //offer
    List<OfferResponse> findOfferByOrderId(Long orderId);
    List<OfferResponse> findByOrderIdOOrderBySuggestedPrice(Long orderId);

    //order
    Optional<OrderResponse> choseOrder(Long orderId, Long chosenOfferId);//todo in customer side should check for customer auth too?

    Optional<OrderResponse> startOrder(Long orderId);

    Optional<OrderResponse> endOrder(Long orderId);

    List<CustomerResponseDto> filterCustomers(String firstName, String lastName, String email);
}