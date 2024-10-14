package org.example.homeservice.service.review;

import org.example.homeservice.dto.review.SpecialistRateRespone;
import org.example.homeservice.dto.validator.ReviewRequest;

import java.util.List;

public interface ReviewService {
    public void addReview(ReviewRequest review) ;
    public List<SpecialistRateRespone> getRatingsBySpecialistId(Long specialistId);

}
