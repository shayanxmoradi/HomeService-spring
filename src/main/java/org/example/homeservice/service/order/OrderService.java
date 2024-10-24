package org.example.homeservice.service.order;

import org.example.homeservice.domain.enums.OrderStatus;
import org.example.homeservice.dto.order.OrderRequest;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.domain.Order;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.List;
import java.util.Optional;

public interface OrderService extends BaseEntityService<Order, Long, OrderRequest, OrderResponse> {
    //  Optional< OrderResponse> registerOrder(OrderRequest orderRequest);
    List<OrderResponse> findWaitingForOfferAndSpecialist();

    List<OrderResponse> findByCustomerId(Long customerId);
    List<OrderResponse> findByCustomerIdAndStatus(Long customerId, OrderStatus orderStatus);
    List<OrderResponse> findBySpecialistId(Long specialistId);
    List<OrderResponse> findBySpecialistIdAndStatus(Long specialistId,OrderStatus orderStatus);

    List<OrderResponse> findWaitingOrdersBySpecialist(Long specialistId);

    Optional<OrderResponse> choseOrder(Long orderId, Long chosenOfferId);//todo in customer side should check for customer auth too?

    Optional<OrderResponse> startOrder(Long orderId);

    Optional<OrderResponse> endOrder(Long orderId);
    void deleteByServiceId( Long serviceId);
    void updateOrdersWithNullService(Long serviceId);


    Optional<OrderResponse> onlinePayment(Long orderId, OrderStatus orderStatus);
    public Optional<OrderResponse> setOnlinePaied(Long orderId) ;
    public List <Order> findByAdrressId(Long adrressId);

    }
