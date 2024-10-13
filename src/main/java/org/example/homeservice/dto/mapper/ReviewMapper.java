package org.example.homeservice.dto.mapper;

import org.example.homeservice.domain.Review;
import org.example.homeservice.domain.Service;
import org.example.homeservice.dto.ServiceRequest;
import org.example.homeservice.dto.ServiceResponse;
import org.example.homeservice.dto.validator.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ReviewMapper {
    @Mapping(source = "order.id",target = "orderId")
    ReviewRequest toReviewRequest(Review review);

    @Mapping(source = "orderId",target = "order.id")

    Review toReview(ReviewRequest reviewRequest);


//    @Mapping(target = "category",source = "isCategory")
////    @Mapping(target = "parentService.id",source = "parentServiceId")
//    Service toEntity(ServiceRequest serviceRequestDto);
//    //
////    @Mapping(target = "parentServiceId", source = "parentService.id")
////    @Mapping(target = "availableSpecialists", source = "avilableSpecialists")
////    @Mapping(target = "subServices", source = "subServices")
//    @Mapping(target = "parentServiceId",source = "parentService.id")
//    ServiceResponse toDto(Service service);
}
