package org.example.homeservice.service.review;

import org.example.homeservice.domain.Review;
import org.example.homeservice.dto.validator.ReviewRequest;

public interface ReviewService {
    public void addReview(ReviewRequest review) ;
}
