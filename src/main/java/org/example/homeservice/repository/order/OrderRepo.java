package org.example.homeservice.repository.order;

import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.enums.OrderStatus;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends BaseEnitityRepo<Order,Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(OrderStatus orderStatus);
    List<Order> findByStatusIn(List<OrderStatus> orderStatus);
    @Modifying
    @Query("SELECT o FROM Order o " +
           "JOIN FETCH o.choosenService s " +
           "JOIN s.avilableSpecialists sp " +
           "WHERE o.status IN ('WAITING_FOR_SPECIALISTS_OFFERS', 'WAITING_FOR_SPECIALISTS') " +
           "AND sp.id = :specialistId")
    List<Order> findWaitingOrdersBySpecialist(@Param("specialistId") Long specialistId);}
