package org.example.homeservice.service.order;

import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.entity.Order;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.List;

public interface OrderService extends BaseEntityService<Order,Long, OrderRequest, OrderResponse> {
//  Optional< OrderResponse> registerOrder(OrderRequest orderRequest);
List<OrderResponse> findWaitingForOfferAndSpecialist();

}
