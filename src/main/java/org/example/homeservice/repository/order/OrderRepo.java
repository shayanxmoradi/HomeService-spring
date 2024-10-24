package org.example.homeservice.repository.order;

import jakarta.transaction.Transactional;
import org.example.homeservice.domain.Order;
import org.example.homeservice.domain.Review;
import org.example.homeservice.domain.Service;
import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends BaseEnitityRepo<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByCustomerIdAndStatus(Long customerId,OrderStatus orderStatus);
    List<Order> findByChosenSpecialistId(Long specialistId);
    List<Order> findByChosenSpecialistIdAndStatus(Long specialistId,OrderStatus orderStatus);

    List<Order> findByStatus(OrderStatus orderStatus);

    List<Order> findByStatusIn(List<OrderStatus> orderStatus);

    @Modifying
    @Query("SELECT o FROM Order o " +
           "JOIN FETCH o.choosenService s " +
           "JOIN s.avilableSpecialists sp " +
           "WHERE o.status IN ('WAITING_FOR_SPECIALISTS_OFFERS', 'WAITING_FOR_SPECIALISTS') " +
           "AND sp.id = :specialistId")
    List<Order> findWaitingOrdersBySpecialist(@Param("specialistId") Long specialistId);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.choosenService.id = :serviceId")
    void deleteByServiceId(@Param("serviceId") Long serviceId);

    @Transactional
    @Modifying
    @Query("UPDATE Order o SET o.choosenService = null WHERE o.choosenService.id = :serviceId")
    void updateOrdersWithNullService(Long serviceId);


    //    Order findOrderByCustomerIdAndStatusAAndChoosenService(Long customerId, OrderStatus orderStatus, Service chosenService);
    Optional<Order> findOrderByCustomerIdAndChoosenServiceIdAndServiceTime(Long customerId, Long chosenService, LocalDateTime serviceTime);
List<Order> findOrderByAddressId(Long addressId);
}
