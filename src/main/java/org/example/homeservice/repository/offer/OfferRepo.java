package org.example.homeservice.repository.offer;

import org.example.homeservice.domain.service.Offer;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;

import java.util.List;

public interface OfferRepo extends BaseEnitityRepo<Offer,Long> {

    List<Offer> findByOrderId(Long orderId);
    List<Offer> findByOrderIdOrderBySuggestedPrice(Long orderId);
    List<Offer> findBySpecialistIdAndOrderId(Long specialistId, Long orderId);

}
