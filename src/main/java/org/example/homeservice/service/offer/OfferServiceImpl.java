package org.example.homeservice.service.offer;

import jakarta.validation.ValidationException;
import org.example.homeservice.dto.offer.OfferMapper;
import org.example.homeservice.domain.service.Offer;
import org.example.homeservice.dto.offer.OfferRequest;
import org.example.homeservice.dto.offer.OfferResponse;
import org.example.homeservice.dto.order.OrderResponse;
import org.example.homeservice.dto.service.ServiceResponse;
import org.example.homeservice.dto.specialist.SpecialistResponse;
import org.example.homeservice.repository.offer.OfferRepo;
import org.example.homeservice.service.baseentity.BaseEntityServiceImpl;
import org.example.homeservice.service.order.OrderService;
import org.example.homeservice.service.service.ServiceService;
import org.example.homeservice.service.user.speciallist.SpeciallistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferServiceImpl extends BaseEntityServiceImpl<Offer, Long, OfferRepo, OfferRequest, OfferResponse> implements OfferService {
    private OrderService orderService;
    private final ServiceService serviceService;
    private final OfferMapper offerMapper;
    private SpeciallistService speciallistService;

    @Autowired
    public OfferServiceImpl(OfferRepo baseRepository, ServiceService serviceService, OfferMapper offerMapper) {
        super(baseRepository);
        this.serviceService = serviceService;
        this.offerMapper = offerMapper;
    }

    @Autowired
    public void setSpeciallistService(@Lazy SpeciallistService speciallistService) {
        this.speciallistService = speciallistService;
    }

    @Autowired
    public void setOrderService(@Lazy OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public Optional<OfferResponse> save(OfferRequest dto) {
        //
        Long orderId = dto.orderId();

        OrderResponse foundedOrder = orderService.findById(orderId)
                .orElseThrow(() -> new ValidationException("No order found with ID: " + orderId));


        SpecialistResponse specialistResponse = speciallistService.findById(dto.specialistId())
                .orElseThrow(() -> new ValidationException("No specialist with this ID found"));


        //checking for duplicated uncomment this
        List<Offer> bySpecialistIdAndOrderId = baseRepository.findBySpecialistIdAndOrderId(dto.specialistId(), orderId);
        if (bySpecialistIdAndOrderId.size() > 0) {
            throw new ValidationException("speciallist with xxxxxx :" + dto.specialistId() + " already summited offer for order : " + orderId);
        }


        //is this service is service of specialist offeredservices?
        if (!serviceService.isSpecialistAvailableInService(foundedOrder.serviceId(), dto.specialistId())) {
            throw new ValidationException("specialist with this Id : " + dto.specialistId() + " is not available in this service with xxxxxx : " + foundedOrder.serviceId());
        }


        Optional<ServiceResponse> foundService = serviceService.findById(foundedOrder.serviceId());

        if (foundService.get().basePrice() > dto.suggestedPrice())
            throw new ValidationException("base price is greater than offered price");


        return Optional.ofNullable(offerMapper.toResponse(baseRepository.save(offerMapper.toEntity(dto))));

    }

    @Override
    public List<OfferResponse> findOfferByOrderId(Long orderId) {
        orderService.findById(orderId);
        if (baseRepository.findByOrderId(orderId) == null || baseRepository.findByOrderId(orderId).isEmpty())
            throw new ValidationException("no offers  for order " +
                                          "with xxxxxx " + orderId + "found");

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
