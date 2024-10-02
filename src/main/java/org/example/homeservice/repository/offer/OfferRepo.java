package org.example.homeservice.repository.offer;

import org.example.homeservice.entity.Offer;
import org.example.homeservice.entity.Order;
import org.example.homeservice.repository.baseentity.BaseEnitityRepo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OfferRepo extends BaseEnitityRepo<Offer,Long> {

    List<Offer> findByOrderId(Long orderId);
    List<Offer> findByOrderIdOrderBySuggestedPrice(Long orderId);

}
