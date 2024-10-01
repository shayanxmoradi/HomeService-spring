package org.example.homeservice.service.user.customer;



import org.example.homeservice.dto.*;
import org.example.homeservice.entity.Customer;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.example.homeservice.service.user.BaseUserService;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseUserService<Customer, CustomerRequsetDto, CustomerResponseDto> {

    Optional<OrderResponse> registerOrder(OrderRequest orderRequest);

    List<Order> getCustomerOrders(CustomerRequsetDto customerRequsetDto);

    Optional<CustomerResponseDto> getCustomerByEmail(String email);
    Optional<CustomerResponseDto> findByEmailAndPass(String username, String password);

  //  public void updatePassword(UpdatePasswordRequst updatePasswordRequst);


    //services
    Optional<List<ServiceResponse>> findAllServices();
    List<ServiceResponse> findAllByParentId(Long parentId);
    List<ServiceResponse> findRealServices();
    List<ServiceResponse> findFirstLayerServices();


}