package org.example.homeservice.service.offer;

import org.example.homeservice.dto.offer.OfferRequest;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.domain.service.Offer;
import org.example.homeservice.service.baseentity.BaseEntityService;

import java.util.List;

public interface OfferService extends BaseEntityService<Offer, Long, OfferRequest, OfferResponse> {
    List<OfferResponse> findOfferByOrderId(Long orderId);

    List<OfferResponse> findByOrderIdOOrderBySuggestedPrice(Long orderId);

}
