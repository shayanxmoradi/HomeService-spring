package org.example.homeservice.dto.mapper;

import org.example.homeservice.dto.OfferRequest;
import org.example.homeservice.dto.OfferResponse;
import org.example.homeservice.entity.Offer;
import org.example.homeservice.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    @Mapping(target = "order.id", source = "orderId")
   //@Mapping(target = "service.id", source = "serviceId")
//todo this trash omg
    @Mapping(target = "specialist.id", source = "specialistId")
    Offer toEntity(OfferRequest offerRequest);

    @Mapping(target = "order", source = "order.id")
    @Mapping(target = "specialis", source = "specialist.id")
    @Mapping(target = "serviceId", source = "service.id")

    OfferResponse toResponse(Offer offer);

    List<OfferResponse> toResponses(List<Offer> offers);

    @Mapping(target = "order.id", source = "order")

    Offer toEnity(OfferResponse offerRequest);

    default Service mapService(Long serviceId) {
        if (serviceId == null) {
            return null;  // Handle null case
        }
        Service service = new Service();
        service.setId(serviceId);
        return service;  // Return the new Service with only the ID set
    }
}