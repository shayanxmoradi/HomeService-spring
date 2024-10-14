package org.example.homeservice.dto.review;

import org.example.homeservice.domain.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface SpecialistRateResponeMapper {
    @Mapping(source = "order.id",target = "orderId")
    SpecialistRateRespone dtoToSpecialistRateRespone(Review review);

    List<SpecialistRateRespone> reviewToDto(List<Review> review);

}
