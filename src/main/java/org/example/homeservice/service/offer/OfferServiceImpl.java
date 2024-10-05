package org.example.homeservice.service.offer;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.*;
import org.example.homeservice.dto.mapper.OfferMapper;
import org.example.homeservice.entity.Offer;
import org.example.homeservice.entity.Order;
import org.example.homeservice.entity.enums.OrderStatus;
import org.example.homeservice.repository.offer.OfferRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl extends BaseEntityServiceImpl<Offer, Long, OfferRepo, OfferRequest, OfferResponse> implements OfferService {
   private  OrderService orderService;
   private final ServiceService serviceService;
   private final OfferMapper offerMapper;
    @Autowired
    public OfferServiceImpl(OfferRepo baseRepository, ServiceService serviceService, OfferMapper offerMapper) {
        super(baseRepository);
        this.serviceService = serviceService;
        this.offerMapper = offerMapper;
    }

@Autowired
    public void setOrderService( @Lazy OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Optional<OfferResponse> save(OfferRequest dto) {
        Long idOfOrder = dto.orderId();
        Optional<OrderResponse> foundedOrder = orderService.findById(idOfOrder);
        if (foundedOrder.isEmpty()){
            throw new ValidationException("no order found");
        }

        Optional<ServiceResponse> foundService = serviceService.findById(dto.serviceId());
        if (foundService.get().basePrice() > dto.suggestedPrice()) throw new ValidationException("base price is greater than offered price");


       return Optional.ofNullable(offerMapper.toResponse(baseRepository.save(offerMapper.toEntity(dto))));

    }

    @Override
    public List<OfferResponse> findOfferByOrderId(Long orderId) {
        return offerMapper.toResponses(baseRepository.findByOrderId(orderId));
    }

    @Override
    public List<OfferResponse> findByOrderIdOOrderBySuggestedPrice(Long orderId) {
        return offerMapper.toResponses(baseRepository.findByOrderIdOrderBySuggestedPrice(orderId));
    }

    @Override
    protected Offer toEntity(OfferRequest dto) {
        return offerMapper.toEntity(dto);
    }

    @Override
    protected OfferResponse toDto(Offer entity) {
        return offerMapper.toResponse(entity);
    }
}
