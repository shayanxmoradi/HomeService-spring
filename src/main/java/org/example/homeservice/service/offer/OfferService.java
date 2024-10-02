package org.example.homeservice.service.offer;

import org.example.homeservice.dto.OfferRequest;
import org.example.homeservice.dto.OfferResponse;
import org.example.homeservice.dto.OrderResponse;
import org.example.homeservice.entity.Offer;
import org.example.homeservice.service.baseentity.BaseEntityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface OfferService extends BaseEntityService<Offer,Long, OfferRequest, OfferResponse>  {
    List<OfferResponse> findOfferByOrderId(Long orderId);
    List<OfferResponse> findByOrderIdOOrderBySuggestedPrice(Long orderId);
  //  Optional<OrderResponse> choseOrder(Long id, Long chosenOfferId);

}
