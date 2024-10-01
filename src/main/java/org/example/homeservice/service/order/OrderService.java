package org.example.homeservice.service.order;

import org.aspectj.weaver.ast.Or;
import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.example.homeservice.service.baseentity.BaseEntityService;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface OrderService extends BaseEntityService<Order,Long, OrderRequest, OrderResponse> {
//  Optional< OrderResponse> registerOrder(OrderRequest orderRequest);

}
