package org.example.homeservice.service.user;



import org.example.homeservice.dto.CustomerRequsetDto;
import org.example.homeservice.dto.CustomerResponseDto;
import org.example.homeservice.dto.UpdatePasswordRequst;
import org.example.homeservice.entites.Customer;
import org.example.homeservice.entites.Order;
import org.example.homeservice.entites.Service;

import javax.management.ServiceNotFoundException;
import java.util.List;
import java.util.Optional;

public interface CustomerService extends BaseUserService<Customer, CustomerRequsetDto, CustomerResponseDto> {
    public Optional<List<Service>> getAllFirstLayerServices();

    Order registerOrder(CustomerRequsetDto customerRequsetDto, Order order);

    List<Order> getCustomerOrders(CustomerRequsetDto customerRequsetDto);

    Optional<CustomerResponseDto> getCustomerByEmail(String email);

    public Service navigateAndSelectService() throws ServiceNotFoundException;

  //  public void updatePassword(UpdatePasswordRequst updatePasswordRequst);

    Optional<CustomerResponseDto> findByEmailAndPass(String username, String password);

}