package org.example.homeservice.repository.order;

import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.enums.OrderStatus;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;

import java.util.List;

public interface OrderRepo extends BaseEnitityRepo<Order,Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus orderStatus);
    List<Order> findByStatusIn(List<OrderStatus> orderStatus);
}
