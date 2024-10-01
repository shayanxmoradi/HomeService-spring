package org.example.homeservice.dto.mapper;

import org.aspectj.weaver.ast.Or;
import org.example.homeservice.dto.OrderRequest;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "choosenService", source = "serviceId")
    @Mapping(target = "address.id", source = "addressId")
    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "address", source = "addressId")
    Order toEntity(OrderRequest orderRequest);

    OrderRequest toDto(Order order);

    default Service mapServiceId(Long serviceId) {
        if (serviceId == null) {
            return null;
        }
        Service service = new Service();
        service.setId(serviceId);
        return service;
    }

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "serviceId", source = "choosenService.id")
    @Mapping(target = "addressId", source = "address.id")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toListOfResponse(List<Order> order);


    //    @Mapping(target = "isCategory", source = "isCategory")
//    OrderRequest toDtoReq(Order order);
//
//    OrderRequest responeToDtoReq(OrderResponse orderResponse);
}
